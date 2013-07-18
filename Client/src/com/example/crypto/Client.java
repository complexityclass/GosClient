package com.example.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * ������ �������. ��������� �� ����� ��������� ���������, �.�. ���� ����� ���
 * �) ������������� ������ � ���������� ������� ��� ������� � ������� (�����
 * ���� ������ ��������� ���� ��� ������� � ��� �������, ���� ���� ����� �����
 * ���������� � ����� �� �������) �) �.�. ��� �������� ��������� ������������
 * getDefault(), �� ���� ����������� �������� ����� ��������� ��������, ��
 * ������� ����������� � ����������� �������������).
 * 
 * @author Copyright 2004-2007 Crypto-Pro. All rights reserved.
 * @.Version
 */
public class Client {

	/**/
	public static final String http_header_separator = "\r\n\r\n";
	/**/
	public static final int DEFAULT_TIMEOUT = 1000;
	/**/
	private int timeout = DEFAULT_TIMEOUT;
	/**/
	private String host;
	/**/
	private int port;
	/**/
	private LogCallback logCallback = null;

	/**
	 * �������� ������ �� ��������� ����� � �����.
	 * 
	 * @param hostname
	 *            ����
	 * @param p
	 *            ����
	 */
	public Client(String hostname, int p, LogCallback callback) {
		host = hostname;
		port = p;
		logCallback = callback;
	}

	/**
	 * ������� ������������� timeout �� ������.
	 * 
	 * @param t
	 *            timeout
	 */
	public void setTimeout(int t) {
		timeout = t;
	}

	/**
	 * �������� ������� ������ �������.
	 * 
	 * @param sslContext
	 *            �������� ��� �����������.
	 * @param fileName
	 *            ��� �����
	 * @throws IOException
	 *             ������ �����-������
	 */
	public void get(SSLContext sslContext, String fileName) throws Exception {

		logCallback.log("Client inits socket factory.");

		SSLSocket soc = null;

		try {
			// ������� 1
			// final SSLSocketFactory sslFact = (SSLSocketFactory)
			// SSLSocketFactory.getDefault();

			// ������� 2
			// final SSLSocketFactoryImpl sslFact = new SSLSocketFactoryImpl();

			// ������� 3
			final SSLSocketFactory sslFact = sslContext.getSocketFactory();

			logCallback.log("Client creates socket.");
			soc = (SSLSocket) sslFact.createSocket(host, port);
			soc.setSoTimeout(timeout);

			logCallback.log("Client sends request.");
			proc(soc, fileName);

		} finally {
			if (soc != null)
				soc.close();
		}
	}

	/**
	 * ���������� ������ ������� � ��������.
	 * 
	 * @param soc
	 *            �����
	 * @param file
	 *            ��� �����
	 * @throws IOException
	 *             ������ �����-������
	 */
	public void proc(Socket soc, String file) throws IOException {

		final InputStream in = soc.getInputStream();
		final OutputStream out = soc.getOutputStream();

		// �������� �������
		final String req = "GET /" + file + " HTTP/1.0\r\n\r\n";
		logCallback.log("Client's request: " + req);
		out.write(req.getBytes());
		out.flush();

		// ������ ������
		logCallback.log("Client parses answer.");
		final String answer = new String(readHeader(in, http_header_separator.getBytes()));
		logCallback.log(answer);

		int fileLength = 0;
		try {
			fileLength = parseAnswer(answer);
		} catch (IOException e) {
			// ignore
		}

		final byte[] body = readBody(in, fileLength);
		final String bodyStr = new String(body, Constants.DEFAULT_ENCODING);

		logCallback.log(new String(bodyStr));

	}

	/**
	 * ������ ������ �� ����� ���������.����� ���� ������� � new byte[]
	 * {(byte)' '}
	 * 
	 * @param in
	 *            ������� �����
	 * @param end
	 *            ����� ���������
	 * @return ����� (�������� ������)
	 * @throws IOException
	 *             ������ �����-������
	 */
	public byte[] readHeader(InputStream in, byte[] end) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int conformity = 0;
		int next;
		logCallback.log("Try reading (Client.readHeader)");
		do {
			next = in.read();
			if (next == -1)
				throw new IOException(" Client: Error reading HTTP header");
			baos.write(next);
			if (next == end[conformity])
				conformity++;
			else
				conformity = 0;
		} while (conformity != end.length);
		return baos.toByteArray();
	}

	/**
	 * ������ ���������� ���������� ������.
	 * 
	 * @param in
	 *            InputStream
	 * @param len
	 *            length
	 * @return �����
	 * @throws IOException
	 *             ������ �����-������
	 */
	public static byte[] readBody(InputStream in, int len) throws IOException {

		// ���� ���� ������ ��������� (�������� �� �������),
		// �� ���������� ���.
		if (len > 0) {

			final byte[] buf = new byte[len];
			int next;
			int pos = 0;

			while (pos != len) {

				next = in.read();

				if (next == -1) {
					throw new IOException(" Error reading HTTP body");
				} // if

				buf[pos++] = (byte) next;
			} // while

			return buf;
		} // if
			// ���� ������� ��� (����� ������), �� ������, ���� �� �������
			// ����� �����.
		else {

			ByteArrayOutputStream buf = new ByteArrayOutputStream();

			while (true) {

				int next = in.read();

				if (next == -1) {

					// ������ �� ���������, ����� ����� �����.
					if (buf.size() == 0) {
						throw new IOException(" Error reading HTTP body");
					} // if

					break;
				} // if

				buf.write((byte) next);
			} // while

			return buf.toByteArray();
		} // else
	}

	/**
	 * ������ ������ ������� � ���������� ����� �����
	 * 
	 * @param str
	 *            ������ ������
	 * @return ����� �����
	 * @throws IOException
	 *             ������ �����-������
	 */
	public static int parseAnswer(String str) throws IOException {
		final String[] split = str.split("\r\n");
		if (!split[0].equalsIgnoreCase("HTTP/1.0 200 OK") && !split[0].equalsIgnoreCase("HTTP/1.1 200 OK"))
			throw new IOException(split[0]);
		// throw new IOException("Unknown answer");
		int len = -1;
		for (int i = 1; i < split.length; i++)
			if (split[i].startsWith("Content-Length:")) {
				final String ss = split[i].substring("Content-Length:".length()).trim();
				len = Integer.parseInt(ss);
				break;
			}
		return len;
	}
}
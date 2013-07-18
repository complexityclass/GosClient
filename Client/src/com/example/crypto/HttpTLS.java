package com.example.crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import java.security.Security;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.ssl.Provider;

public class HttpTLS extends TLS {

	static {

		// ���������� �������������� ��������, ����� ��������������
		// ��������� �� cpSSL, � �� Harmony.

		Security.setProperty("ssl.KeyManagerFactory.algorithm", Provider.KEYMANGER_ALG);
		Security.setProperty("ssl.TrustManagerFactory.algorithm", Provider.KEYMANGER_ALG);

		Security.setProperty("ssl.SocketFactory.provider", "ru.CryptoPro.ssl.SSLSocketFactoryImpl");
		Security.setProperty("ssl.ServerSocketFactory.provider", "ru.CryptoPro.ssl.SSLServerSocketFactoryImpl");
	}

	/**
	 * �����������.
	 * 
	 * @param ts
	 *            ��������� ���������� ������������.
	 * @param tsp
	 *            ������ �� ��������� ���������� ������������.
	 * @param clientAuth
	 *            True, ���� �� ������� ��������� �������������� �������.
	 * @param ksp
	 *            ������ ��� ������ ����������� ���������� ��� ��������������
	 *            ��������������.
	 * @param host
	 *            ����� ���������� �����.
	 * @param port
	 *            ���� ���������� �����.
	 * @page ����������� ��������.
	 */
	public HttpTLS(InputStream ts, char[] tsp, boolean clientAuth, char[] ksp, String host, int port, String page) {
		super(ts, tsp, clientAuth, ksp, host, port, page);
	}

	/**
	 * ������ �������.
	 * 
	 * @param callback
	 *            ������.
	 * @return ��������� ������.
	 * @throws Exception
	 */
	public void getResult(LogCallback callback) throws Exception {
		
		IThreadExecuted ithe = new HttpTLSThread();
		
		getResult(callback, ithe);
		System.out.println(ithe.execute(callback));
	}

	/**
	 * ����� SimpleTLSThread ��������� ����������� apache http ������� �� TLS.
	 * 
	 */
	private class HttpTLSThread implements IThreadExecuted {

		/**
		 * ����� ��� ���������� ������ � ������.
		 * 
		 * @param callback
		 *            ������.
		 * @throws Exception
		 */
		public String execute(LogCallback callback) throws Exception {

			HttpClient httpClient = null;

			StringBuilder sb = new StringBuilder();

			try {

				callback.log("Init Apache Http Client Example.");
				System.out.println("Init Apache Http Client Example");

				final String httpAddress = "https://" + remoteHost + ":" + remotePort + File.separator + downloadPage;

				callback.log("Call to: " + httpAddress);

				URI url = new URI(httpAddress);

				// ��������� ��� �����������.

				callback.log("Load trusted stores.");

				/**
				 * ��� ������(!) ����������� ��������� �������� ����������
				 * TrustStore �� JCSP. ����� �� ������������ ������.
				 */
				KeyStore ts = KeyStore.getInstance(JCSP.CERT_STORE_NAME, JCSP.PROVIDER_NAME);
				ts.load(trustedStore, null);

				KeyStore ks = null;

				if (needClientAuth) {
					callback.log("Load key stores.");
					ks = KeyStore.getInstance(JCSP.HD_STORE_NAME, JCSP.PROVIDER_NAME);
					ks.load(null, null);
				} // if

				String keyStorePasswordStr = keyStorePassword == null ? null : String.valueOf(keyStorePassword);

				callback.log("Create socket factory.");

				SSLSocketFactory socketFactory = new SSLSocketFactory(Provider.ALGORITHM, ks, keyStorePasswordStr, ts,
						null, null);

				callback.log("Register https scheme.");

				// ������������ HTTPS �����.
				Scheme httpsScheme = new Scheme("https", socketFactory, remotePort);
				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(httpsScheme);

				callback.log("Set connection options.");

				// ��������� ����������.
				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setSoTimeout(params, MAX_CLIENT_TIMEOUT);
				ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
				httpClient = new DefaultHttpClient(cm, params);

				callback.log("Execute GET request.");

				// GET-������.
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpget);
				HttpEntity entity = response.getEntity();

				callback.log("Response status: " + response.getStatusLine());

				System.out.println("Response :" + response.getStatusLine());

				int status = response.getStatusLine().getStatusCode();
				if (status != 200) {
					callback.setStatus("Bad http response status: " + status);
					return "bad http";
				} // if

				if (entity != null) {

					// �������� ������ ���������.
					InputStream is = entity.getContent();

					BufferedReader in = new BufferedReader(new InputStreamReader(is, Constants.DEFAULT_ENCODING));

					callback.log("Read response:");

					// ������� �����.
					String line;
					while ((line = in.readLine()) != null) {
						callback.log(line);
						sb.append(line);

					} // while

					if (in != null) {
						in.close();
					} // if

					callback.setStatus("OK.");

				} // if

			} catch (Exception e) {
				callback.setStatus("Failed.");
				throw e;
			} finally {
				if (httpClient != null) {

					callback.log("Shutdown http connection.");
					Log.i(Constants.APP_LOGGER_TAG, "Shutdown http connection.");

					// ����� ������� ����������, �.�. HeapWorker ����� ����� jvm
					// ��-�� ��������� �������� � finalize.
					httpClient.getConnectionManager().shutdown();
				} // if
			}

			return sb.toString();

		}

	}
}

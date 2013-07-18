package com.example.crypto;

import ru.CryptoPro.JCP.tools.Encoder;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ��������� ����� LogCallback ������������ ��� ������ � ���� ��������� �
 * ��������� �������.
 * 
 * 30/05/2013
 * 
 */
public class LogCallback {

	/**
	 * ���� ��� ������.
	 */
	private EditText logger = null;

	/**
	 * ���� ��� �������.
	 */
	private TextView showStatus = null;

	/**
	 * �����������.
	 * 
	 * @param log
	 *            ����������� ������.
	 */
	public LogCallback(EditText log, TextView status) {
		logger = log;
		showStatus = status;
	}

	public EditText getLogger() {
		return logger;
	}

	/**
	 * ������ ��������� � ����.
	 * 
	 * @param message
	 *            ���������.
	 */
	public void log(String message) {
		String text = String.valueOf(logger.getText());
		logger.setText(text + "\n" + message);
	}

	/*
	 * ������ ��������� � ����.
	 * 
	 * @param message ���������.
	 * 
	 * @param base64 True, ���� ����� �������������� � base64.
	 */
	public void log(byte[] message, boolean base64) {
		log(base64 ? toBase64(message) : new String(message));
	}

	/**
	 * ����������� � base64.
	 * 
	 * @param data
	 *            �������� ������.
	 * @return ���������������� ������.
	 */
	private String toBase64(byte[] data) {
		Encoder enc = new Encoder();
		return enc.encode(data);
	}

	/**
	 * ������� ����.
	 */
	public void clear() {
		logger.setText("");
		showStatus.setText("Status: unknown");
	}

	/**
	 * ����������� ������ �������.
	 * 
	 * @param status
	 *            ������ �������.
	 */
	public void setStatus(String status) {
		showStatus.setText("Status: " + status);
	}

}
package com.example.crypto;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import android.util.Log;

import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.JCSP.tools.common.Constants;

public abstract class ISignData implements IHashData {

	/**
	 * �������� ���������� ��� �������.
	 */
	public static final String SIGN_CONTAINER_NAME = "clientTL.000";

	/**
	 * ����� ����� �������.
	 */
	public static final String SIGN_KEY_ALIAS = "clientTLS";

	/**
	 * ������ ����� �������.
	 */
	public static final char[] SIGN_KEY_PASSWORD = "1".toCharArray();

	/**
	 * ����������� �������� ���� ��� �������.
	 */
	protected PrivateKey privateKey = null;

	/**
	 * ����������� ���������� ����� ������� ��� �������� �������.
	 */
	protected X509Certificate certificate = null;

	/**
	 * �������� ����� � ����������� �� ����������.
	 * 
	 * @param alias
	 *            ����� �����.
	 * @param password
	 *            ������ � �����.
	 */
	protected void load(String alias, char[] password) throws Exception {

		if (privateKey != null && certificate != null) {
			return;
		} // if

		// �������� �����������.

		KeyStore keyStore = KeyStore.getInstance(JCSP.HD_STORE_NAME, JCSP.PROVIDER_NAME);
		keyStore.load(null, null);

		privateKey = (PrivateKey) keyStore.getKey(alias, password);
		certificate = (X509Certificate) keyStore.getCertificate(alias);

		// ����������� ���������� � �����.

		if (privateKey == null || certificate == null) {
			throw new Exception("Private key or/and certificate is null.");
		} // if
		else {
			Log.i(Constants.APP_LOGGER_TAG, "Certificate: " + certificate.getSubjectDN());
		} // else

	}

	/**
	 * ������ ������� � ������. ����������� ���������� ������ � ��������� ������
	 * (������ ��� ����������� � ���������).
	 * 
	 * @param callback
	 *            ������.
	 * @param task
	 *            ����������� ������.
	 * @throws Exception
	 */
	public void getResult(LogCallback callback, IThreadExecuted task) throws Exception {

		callback.log("Prepare client thread.");

		ClientThread clientThread = new ClientThread(callback, task);
		clientThread.setPriority(Thread.NORM_PRIORITY);

		callback.log("Start client thread.");

		clientThread.start();
		clientThread.join(MAX_THREAD_TIMEOUT);

		callback.log("Client thread finished job.");
	}

}
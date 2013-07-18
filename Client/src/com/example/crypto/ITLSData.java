package com.example.crypto;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.ssl.Provider;
import ru.CryptoPro.ssl.util.cpSSLConfig;

public abstract class ITLSData extends IEncryptDecryptData {

	/**
	 * ������ � ��������� ���������� ������������ �� ���������.
	 */
	public static final char[] DEFAULT_TRUSTED_STORE_PASSWORD = "1".toCharArray();

	/**
	 * ������ ��� ������ �������� ����������� ��� ���������� �������������� ��
	 * ���������.
	 */
	public static final char[] DEFAULT_KEY_STORE_PASSWORD = "1".toCharArray();

	/**
	 * ����� ������� ��� ��������� �������� html-��������.
	 */
	public static final String DEFAULT_HOST = "cpca.cryptopro.ru";

	/**
	 * ���� ��� ����������� � �������.
	 */
	public static final int DEFAULT_PORT = 443;

	/**
	 * ����������� ��������.
	 */
	public static final String DEFAULT_PAGE = "default.htm";

	/**
	 * ����� ������� ��� ��������� �������� html-�������� � ����������
	 * ���������������.
	 */
	public static final String DEFAULT_AUTH_HOST = "www.cryptopro.ru";

	/**
	 * ���� ��� ����������� � ������� � ���������� ���������������.
	 */
	public static final int DEFAULT_AUTH_PORT = 4444;

	/**
	 * ����������� �������� � ������� � ���������� ���������������.
	 */
	public static final String DEFAULT_AUTH_PAGE = "test/tls-cli.asp";

	/**
	 * ����� �� ����� ��������� ���������� ������������.
	 */
	protected InputStream trustedStore = null;

	/**
	 * ������ ��� ������� � ��������� ���������� ������������.
	 */
	protected char[] trustedStorePassword = null;

	/**
	 * ������ ��� ������ ����� ������� ��� ������������ ��������������.
	 */
	protected char[] keyStorePassword = null;

	/**
	 * True, ���� ���������� ������������� ��������������.
	 */
	protected boolean needClientAuth = false;

	/**
	 * ����� ���������� �����.
	 */
	protected String remoteHost = null;

	/**
	 * ���� ���������� �����.
	 */
	protected int remotePort = DEFAULT_PORT;

	/**
	 * ����������� ��������.
	 */
	protected String downloadPage = null;

	static {

		// ��� �������� ������� �����������.
		System.setProperty("com.sun.security.enableCRLDP", "true");
		System.setProperty("com.ibm.security.enableCRLDP", "true");

		// ��������� �����������, �������, ���������� �� ���������.
		cpSSLConfig.setDefaultSSLProvider(JCSP.PROVIDER_NAME);
	}

	/*
	 * �������� SSL ���������.
	 * 
	 * @param callback ������.
	 * 
	 * @return ������� SSL ��������.
	 * 
	 * @throws Exception.
	 */
	protected SSLContext createSSLContext(LogCallback callback) throws Exception {

		callback.log("Init trusted store.");

		/**
		 * ��� ������(!) ����������� ��������� �������� ���������� TrustStore ��
		 * JCSP. ����� �� ������������ ������.
		 */
		KeyStore ts = KeyStore.getInstance(JCSP.CERT_STORE_NAME, JCSP.PROVIDER_NAME);
		ts.load(trustedStore, null);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(Provider.KEYMANGER_ALG, Provider.PROVIDER_NAME);

		if (needClientAuth) {

			callback.log("Init key store. Load containers.");

			KeyStore ks = KeyStore.getInstance(JCSP.HD_STORE_NAME, JCSP.PROVIDER_NAME);

			ks.load(null, null);
			kmf.init(ks, keyStorePassword);
		} // if

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(Provider.KEYMANGER_ALG, Provider.PROVIDER_NAME);
		tmf.init(ts);

		callback.log("Create SSL context.");

		SSLContext sslCtx = SSLContext.getInstance(Provider.ALGORITHM, Provider.PROVIDER_NAME);
		sslCtx.init(needClientAuth ? kmf.getKeyManagers() : null, tmf.getTrustManagers(), null);

		callback.log("SSL context completed.");

		return sslCtx;
	}

}
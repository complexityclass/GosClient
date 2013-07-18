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
	 * Пароль к хранилищу доверенных сертификатов по умолчанию.
	 */
	public static final char[] DEFAULT_TRUSTED_STORE_PASSWORD = "1".toCharArray();

	/**
	 * Пароль для выбора ключевых контейнеров при клиентской аутентификации по
	 * умолчанию.
	 */
	public static final char[] DEFAULT_KEY_STORE_PASSWORD = "1".toCharArray();

	/**
	 * Адрес сервера для получения тестовой html-страницы.
	 */
	public static final String DEFAULT_HOST = "cpca.cryptopro.ru";

	/**
	 * Порт для подключения к серверу.
	 */
	public static final int DEFAULT_PORT = 443;

	/**
	 * Скачиваемая страница.
	 */
	public static final String DEFAULT_PAGE = "default.htm";

	/**
	 * Адрес сервера для получения тестовой html-страницы с клиентской
	 * аутентификацией.
	 */
	public static final String DEFAULT_AUTH_HOST = "www.cryptopro.ru";

	/**
	 * Порт для подключения к серверу с клиентской аутентификацией.
	 */
	public static final int DEFAULT_AUTH_PORT = 4444;

	/**
	 * Скачиваемая страница с сервера с клиентской аутентификацией.
	 */
	public static final String DEFAULT_AUTH_PAGE = "test/tls-cli.asp";

	/**
	 * Поток из файла хранилища доверенных сертификатов.
	 */
	protected InputStream trustedStore = null;

	/**
	 * Пароль для доступа к хранилищу доверенных сертификатов.
	 */
	protected char[] trustedStorePassword = null;

	/**
	 * Пароль для выбора ключа клиента при двусторонней аутентификации.
	 */
	protected char[] keyStorePassword = null;

	/**
	 * True, если необходима двухсторонняя аутентификация.
	 */
	protected boolean needClientAuth = false;

	/**
	 * Адрес удаленного хоста.
	 */
	protected String remoteHost = null;

	/**
	 * Порт удаленного хоста.
	 */
	protected int remotePort = DEFAULT_PORT;

	/**
	 * Скачиваемая страница.
	 */
	protected String downloadPage = null;

	static {

		// Для проверки статуса сертификата.
		System.setProperty("com.sun.security.enableCRLDP", "true");
		System.setProperty("com.ibm.security.enableCRLDP", "true");

		// Провайдер хеширования, подписи, шифрования по умолчанию.
		cpSSLConfig.setDefaultSSLProvider(JCSP.PROVIDER_NAME);
	}

	/*
	 * Создание SSL контекста.
	 * 
	 * @param callback Логгер.
	 * 
	 * @return готовый SSL контекст.
	 * 
	 * @throws Exception.
	 */
	protected SSLContext createSSLContext(LogCallback callback) throws Exception {

		callback.log("Init trusted store.");

		/**
		 * Для чтения(!) доверенного хранилища доступна реализация TrustStore из
		 * JCSP. Можно не использовать пароль.
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
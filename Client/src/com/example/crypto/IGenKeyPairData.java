package com.example.crypto;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.util.Log;

import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCPRequest.GostCertificateRequest;
import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.JCSP.KeyStore.KeyStoreConfig;
import ru.CryptoPro.JCSP.tools.common.Constants;

public abstract class IGenKeyPairData extends ISignData {

	/**
	 * Адрес УЦ для получения сертификата.
	 */
	public static final String CA_ADDRESS = "http://www.cryptopro.ru/certsrv/";

	/**
	 * Название алгоритма ключей.
	 */
	protected String algorithmName = null;

	/**
	 * True, если нужно получить сертификат из УЦ и установить его в контейнер.
	 */
	protected boolean installCert = false;

	/**
	 * Название контейнера.
	 */
	protected String storeAlias = "defaultStore";

	/**
	 * Путь к контейнеру с полным именем (fqcn).
	 */
	protected String storeName = null;

	/**
	 * Конструктор. Тут же происходит создание генератора чисел.
	 * 
	 * @param algName
	 *            Название алгоритма ключей.
	 * @param ic
	 *            True, если необходимо получить сертификат УЦ и установить в
	 *            контейнер.
	 */
	protected IGenKeyPairData(String algName, boolean ic) {

		// Проверка алгоритма.

		if (!algName.equalsIgnoreCase(JCP.GOST_DEGREE_NAME) && !algName.equalsIgnoreCase(JCP.GOST_DH_NAME)) {
			algorithmName = JCP.GOST_DEGREE_NAME;
		} // if
		else {
			algorithmName = algName;
		} // else

		installCert = ic;

		// Сгенерим короткое произвольное имя.

		SecureRandom rndName = null;

		try {
			rndName = SecureRandom.getInstance("CPRandom", JCSP.PROVIDER_NAME);
		} catch (NoSuchAlgorithmException e) {
			Log.e(Constants.APP_LOGGER_TAG, e.getMessage());
		} catch (NoSuchProviderException e) {
			Log.e(Constants.APP_LOGGER_TAG, e.getMessage());
		}

		if (rndName != null) {
			final byte[] name = new byte[8];
			rndName.nextBytes(name);
			storeAlias = toHexString(name);
		} // if

		// Создаем имя контейнера с приставкой
		// \\.\HDIMAGE\ впереди.

		storeName = KeyStoreConfig.getHDImage().makeContainerName(storeAlias);
	}

	/**
	 * Генерация запроса на сертификат (PKCS10) и получение сертификата.
	 * 
	 * @param name
	 *            CN сертификата.
	 * @param privateKey
	 *            Закрытый ключ.
	 * @param publicKey
	 *            Открытый ключ.
	 * @return сформированный сертификат.
	 * @throws Exception
	 */
	protected X509Certificate generateCertificate(String name, PrivateKey privateKey, PublicKey publicKey,
			LogCallback callback) throws Exception {

		callback.log("Prepare certificate request...");

		// Создание запроса на сертификат аутентификации сервера.
		GostCertificateRequest request = new GostCertificateRequest(JCSP.PROVIDER_NAME);

		callback.log("Set certificate request params.");

		request.setKeyUsage(GostCertificateRequest.CRYPT_DEFAULT);
		request.addExtKeyUsage(GostCertificateRequest.INTS_PKIX_CLIENT_AUTH);

		request.setSubjectInfo("CN=" + name + ",C=RU");
		request.setPublicKeyInfo(publicKey);

		callback.log("Create certificate request.");
		request.encodeAndSign(privateKey);

		// Отправка запроса центру сертификации и получение от центра
		// сертификата в DER-кодировке.

		callback.log("Send certificate request to " + CA_ADDRESS + ", retrieve certificate.");
		byte[] encodedCertificate = request.getEncodedCert(CA_ADDRESS);

		CertificateFactory cf = CertificateFactory.getInstance("X509");

		callback.log("Convert byte array to certificate.");
		return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(encodedCertificate));
	}

	/**
	 * Ковертиция массива байтов в hex-строку.
	 * 
	 * @param ba
	 *            Массив байтов.
	 * @return hex-строка.
	 */
	private static String toHexString(byte[] ba) {

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < ba.length; i++) {
			str.append(String.format("%x", ba[i]));
		}

		return str.toString();
	}
}

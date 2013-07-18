package com.example.crypto;

public abstract class IEncryptDecryptData extends ISignData {

	/**
	 * Название контейнера для шифрования на стороне клиента.
	 */
	public static final String CLIENT_CONTAINER_NAME = SIGN_CONTAINER_NAME;

	/**
	 * Алиас ключа шифрования на стороне клиента.
	 */
	public static final String CLIENT_KEY_ALIAS = SIGN_KEY_ALIAS;

	/**
	 * Пароль ключа шифрования на стороне клиента.
	 */
	public static final char[] CLIENT_KEY_PASSWORD = SIGN_KEY_PASSWORD;

	/**
	 * Название контейнера для шифрования на стороне сервера.
	 */
	public static final String SERVER_CONTAINER_NAME = "serverTL.000";

	/**
	 * Алиас ключа шифрования на стороне сервера.
	 */
	public static final String SERVER_KEY_ALIAS = "serverTLS";

	/**
	 * Пароль ключа шифрования на стороне сервера.
	 */
	public static final char[] SERVER_KEY_PASSWORD = "1".toCharArray();

}

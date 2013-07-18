package com.example.crypto;

public abstract class IEncryptDecryptData extends ISignData {

	/**
	 * �������� ���������� ��� ���������� �� ������� �������.
	 */
	public static final String CLIENT_CONTAINER_NAME = SIGN_CONTAINER_NAME;

	/**
	 * ����� ����� ���������� �� ������� �������.
	 */
	public static final String CLIENT_KEY_ALIAS = SIGN_KEY_ALIAS;

	/**
	 * ������ ����� ���������� �� ������� �������.
	 */
	public static final char[] CLIENT_KEY_PASSWORD = SIGN_KEY_PASSWORD;

	/**
	 * �������� ���������� ��� ���������� �� ������� �������.
	 */
	public static final String SERVER_CONTAINER_NAME = "serverTL.000";

	/**
	 * ����� ����� ���������� �� ������� �������.
	 */
	public static final String SERVER_KEY_ALIAS = "serverTLS";

	/**
	 * ������ ����� ���������� �� ������� �������.
	 */
	public static final char[] SERVER_KEY_PASSWORD = "1".toCharArray();

}

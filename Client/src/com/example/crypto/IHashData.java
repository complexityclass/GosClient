package com.example.crypto;


/**
 * ��������� ��������� IHashData ������������ ���
 * ��������� �������� ������ � �����.
 *
 * 27/05/2013
 *
 */
public interface IHashData {

    /**
     * ������������ ������� �������� ������/������ ��������
     * (����).
     */
    public static final int MAX_CLIENT_TIMEOUT = 60 * 60 * 1000;

    /**
     * ������������ ������� �������� ���������� ������ � ��������
     * � ������ ������������� ��������� (����).
     */
    public static final int MAX_THREAD_TIMEOUT = 100 * 60 * 1000;

    /**
     * ������ �������.
     *
     * @param callback ������.
     * @throws Exception
     */
    public void getResult(LogCallback callback) throws Exception;

}

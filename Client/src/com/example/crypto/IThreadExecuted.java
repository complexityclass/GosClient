package com.example.crypto;

public interface IThreadExecuted {

    /**
     * ����� ��� ���������� ������ � ������.
     * ������ ������������ ������ ������.
     *
     * @param callback ������.
     * @throws Exception
     */
    public String execute(LogCallback callback) throws Exception;

}

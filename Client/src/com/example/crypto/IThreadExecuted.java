package com.example.crypto;

public interface IThreadExecuted {

    /**
     * Метод для выполнения задачи в потоке.
     * Задача записывается внутри метода.
     *
     * @param callback Логгер.
     * @throws Exception
     */
    public String execute(LogCallback callback) throws Exception;

}

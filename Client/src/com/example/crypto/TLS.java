package com.example.crypto;

import java.io.InputStream;

import android.util.Log;

/**
 * Класс TLSExample реализует пример обмена
 * по TLS 1.0.
 *
 * 27/05/2013
 *
 */
public class TLS extends ITLSData {

    /**
     * Конструктор.
     *
     * @param ts Хранилище доверенных сертификатов.
     * @param tsp Пароль на хранилище доверенных сертификатов.
     * @param clientAuth True, если на сервере требуется
     * аутентификация клиента.
     * @param ksp Пароль для выбора клиентского контейнера при
     * двухстороннней аутентификации.
     * @param host Адрес удаленного хоста.
     * @param port Порт удаленного хоста.
     * @page Скачиваемая страница.
     */
    public TLS(InputStream ts, char[] tsp,
        boolean clientAuth, char[] ksp, String host,
        int port, String page) {

        trustedStore = ts;
        trustedStorePassword = tsp;
        keyStorePassword = ksp;
        needClientAuth = clientAuth;

        remoteHost = host;
        remotePort = port;
        downloadPage = page;
    }

    /**
     * Работа примера.
     *
     * @param callback Логгер.
     * @throws Exception
     */
    public void getResult(LogCallback callback) throws Exception {
        IThreadExecuted task = new SampleTLSThread();
        getResult(callback, task);
    }

    /**
     * Класс SimpleTLSThread реализует подключение
     * самописного клиента по TLS.
     *
     */
    private class SampleTLSThread implements IThreadExecuted {

        /**
         * Метод для выполнения задачи в потоке.
         *
         * @param callback Логгер.
         * @throws Exception
         */
        public String execute(LogCallback callback) throws Exception {

            callback.log("Init Client: " + remoteHost + ":" + remotePort);
            Client client = new Client(remoteHost, remotePort, callback);
            client.setTimeout(MAX_CLIENT_TIMEOUT);

            callback.log("Get page: " + downloadPage);

            try {
                client.get(createSSLContext(callback), downloadPage);
                callback.setStatus("OK.");
            } catch (Exception e) {
                callback.setStatus("Failed.");
                Log.e(Constants.APP_LOGGER_TAG, e.getMessage());
            }
            
            return "ok";

        }
    }

}

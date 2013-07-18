package com.example.crypto;

import java.io.InputStream;

import android.util.Log;

/**
 * ����� TLSExample ��������� ������ ������
 * �� TLS 1.0.
 *
 * 27/05/2013
 *
 */
public class TLS extends ITLSData {

    /**
     * �����������.
     *
     * @param ts ��������� ���������� ������������.
     * @param tsp ������ �� ��������� ���������� ������������.
     * @param clientAuth True, ���� �� ������� ���������
     * �������������� �������.
     * @param ksp ������ ��� ������ ����������� ���������� ���
     * �������������� ��������������.
     * @param host ����� ���������� �����.
     * @param port ���� ���������� �����.
     * @page ����������� ��������.
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
     * ������ �������.
     *
     * @param callback ������.
     * @throws Exception
     */
    public void getResult(LogCallback callback) throws Exception {
        IThreadExecuted task = new SampleTLSThread();
        getResult(callback, task);
    }

    /**
     * ����� SimpleTLSThread ��������� �����������
     * ����������� ������� �� TLS.
     *
     */
    private class SampleTLSThread implements IThreadExecuted {

        /**
         * ����� ��� ���������� ������ � ������.
         *
         * @param callback ������.
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

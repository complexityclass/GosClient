package com.example.crypto;

import ru.CryptoPro.JCSP.tools.common.Constants;
import android.os.Looper;
import android.util.Log;


/**
 * ��������� ����� ClientThread ��������� ������ � ��������� ������.
 * 
 * 29/05/2013
 * 
 */
public class ClientThread extends Thread {

	/**
	 * ����������� ������.
	 */
	private IThreadExecuted executedTask = null;

	/**
	 * ������.
	 */
	private LogCallback logCallback = null;

	/**
	 * �����������.
	 * 
	 * @param task
	 *            ����������� ������.
	 */
	public ClientThread(LogCallback callback, IThreadExecuted task) {

		logCallback = callback;
		executedTask = task;
	}

	/**
	 * �������� �������. ��������� ���������� �������. � ������ ������ �����
	 * ��������� � ���.
	 * 
	 */
	public void run() {

		/**
		 * ����������� �������, �.�. ����� ������������� ���� ���-���� � ����.
		 */
		Looper.getMainLooper().prepare();

		try {
			executedTask.execute(logCallback);
		} catch (Exception e) {
			Log.e(Constants.APP_LOGGER_TAG, e.getMessage());
		}

	}

}
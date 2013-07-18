package com.example.crypto;

import ru.CryptoPro.JCSP.tools.common.Constants;
import android.os.Looper;
import android.util.Log;


/**
 * Служебный класс ClientThread выполняет задачу в отдельном потоке.
 * 
 * 29/05/2013
 * 
 */
public class ClientThread extends Thread {

	/**
	 * Выполняемая задача.
	 */
	private IThreadExecuted executedTask = null;

	/**
	 * Логгер.
	 */
	private LogCallback logCallback = null;

	/**
	 * Конструктор.
	 * 
	 * @param task
	 *            Выполняемая задача.
	 */
	public ClientThread(LogCallback callback, IThreadExecuted task) {

		logCallback = callback;
		executedTask = task;
	}

	/**
	 * Поточная функция. Запускает выполнение задания. В случае ошибки пишет
	 * сообщение в лог.
	 * 
	 */
	public void run() {

		/**
		 * Обязательно зададим, т.к. может потребоваться ввод пин-кода в окне.
		 */
		Looper.getMainLooper().prepare();

		try {
			executedTask.execute(logCallback);
		} catch (Exception e) {
			Log.e(Constants.APP_LOGGER_TAG, e.getMessage());
		}

	}

}
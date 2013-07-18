package com.example.crypto;

import ru.CryptoPro.JCP.tools.Encoder;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Служебный класс LogCallback предназначен для записи в поле сообщений и
 * установки статуса.
 * 
 * 30/05/2013
 * 
 */
public class LogCallback {

	/**
	 * Поле для записи.
	 */
	private EditText logger = null;

	/**
	 * Поле для статуса.
	 */
	private TextView showStatus = null;

	/**
	 * Конструктор.
	 * 
	 * @param log
	 *            Графический объект.
	 */
	public LogCallback(EditText log, TextView status) {
		logger = log;
		showStatus = status;
	}

	public EditText getLogger() {
		return logger;
	}

	/**
	 * Запись сообщения в поле.
	 * 
	 * @param message
	 *            Сообщение.
	 */
	public void log(String message) {
		String text = String.valueOf(logger.getText());
		logger.setText(text + "\n" + message);
	}

	/*
	 * Запись сообщения в поле.
	 * 
	 * @param message Сообщение.
	 * 
	 * @param base64 True, если нужно конвертировать в base64.
	 */
	public void log(byte[] message, boolean base64) {
		log(base64 ? toBase64(message) : new String(message));
	}

	/**
	 * Конвертация в base64.
	 * 
	 * @param data
	 *            Исходные данные.
	 * @return конвертированная строка.
	 */
	private String toBase64(byte[] data) {
		Encoder enc = new Encoder();
		return enc.encode(data);
	}

	/**
	 * Очистка поля.
	 */
	public void clear() {
		logger.setText("");
		showStatus.setText("Status: unknown");
	}

	/**
	 * Отображение строки статуса.
	 * 
	 * @param status
	 *            Строка статуса.
	 */
	public void setStatus(String status) {
		showStatus.setText("Status: " + status);
	}

}
package com.example.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.http.Connect;
import com.example.http.PostData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TextBrowser extends Activity {

	public static final String url = "http://pgu.khv.gov.ru/";
	public static Integer page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_browser);

		final EditText text = (EditText) findViewById(R.id.textContent);
		Button button = (Button) findViewById(R.id.buttonLoad);

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				NetworkThread netThread = new NetworkThread();
				netThread.execute(url);

				try {
					text.setText(netThread.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_browser, menu);
		return true;
	}

	private class NetworkThread extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String result = "OKI?";

			Connect connect = new Connect(getApplicationContext());

//			System.out.println("--------------------Do 1 GET-------------------------");
//			StringBuffer stringBuffer = connect.topgu(params[0], true);
//			result = stringBuffer.toString();
//
//			System.out.println("--------------------Do 2 GET------------------------");
//			result = connect.doGet("http://pgu.khv.gov.ru/?a=Authorization&category=Snils", true);
//
//			/*
//			 * System.out.println(
//			 * "--------------------Do 3 GET------------------------"); result =
//			 * connect
//			 * .doGet("http://pgu.khv.gov.ru/?a=Authorization&category=Snils");
//			 */
//
//			System.out.println("--------------------Do 4 GET------------------------");
//			result = connect
//					.doGet("https://pgu.khv.gov.ru/saml/module.php/core/khvLogin2.php?ref=&srv=http%3A%2F%2F172.18.205.4%3A7779%2FCore%2Fservices%2F&as=default-sp",
//							true);

			System.out.println("--------------------Do 5 GET------------------------");
			result = connect.doGet("https://esia.gosuslugi.ru/idp/Authn/CommonLogin", true);

			System.out.println("--------------------Do firs POST-------------------------");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("username", "168-216-427+76"));
			nameValuePairs.add(new BasicNameValuePair("password", "2391423choco"));
			nameValuePairs.add(new BasicNameValuePair("answer", ""));
			nameValuePairs.add(new BasicNameValuePair("globalRole", "RF_PERSON"));
			nameValuePairs.add(new BasicNameValuePair("capture", ""));
			nameValuePairs.add(new BasicNameValuePair("phraseId", ""));
			nameValuePairs.add(new BasicNameValuePair("cmsDS", ""));
			nameValuePairs.add(new BasicNameValuePair("isRegCheck", "false"));

			result = connect.doPost("https://esia.gosuslugi.ru/idp/Authn/CommonLogin", nameValuePairs, true);
			

			/*System.out.println("--------------------Do 6 GET------------------------");
			result = connect
					.doGet("https://pgu.khv.gov.ru/saml/module.php/core/khvLogin2.php?as=default-sp&srv=http%253A%252F%252F172.18.205.4%253A7779%252FCore%252Fservices%252F&ref=",
							true);*/
//
//			System.out.println("--------------------Do 7 GET------------------------");
//			result = connect.doGet("https://pgu.khv.gov.ru/", false);
//
//			System.out.println("--------------------Do 8 GET------------------------");
//			result = connect.doGet("https://pgu.khv.gov.ru/?a=PersonCab", false);
//
//		//	System.out.println("--------------------Do 9 GET------------------------");
//		//	result = connect.doGet("https://pgu.khv.gov.ru/?a=PersonCab&category=Details", false);
//
//			System.out.println("--------------------Do data upload POST---------------");
//			List<NameValuePair> dataPairs = new ArrayList<NameValuePair>();
//
//			dataPairs.add(new BasicNameValuePair(PostData.NAME, "������"));
//			dataPairs.add(new BasicNameValuePair(PostData.SURNAME, "������"));
//			dataPairs.add(new BasicNameValuePair(PostData.SECOND_NAME, "�������"));
//			dataPairs.add(new BasicNameValuePair(PostData.BIRTH_DATE, "23.09.1992"));
//			dataPairs.add(new BasicNameValuePair(PostData.SEX, "21"));
//			dataPairs.add(new BasicNameValuePair(PostData.SNILS, "168-216-427 76"));
//			dataPairs.add(new BasicNameValuePair(PostData.INN, ""));
//			dataPairs.add(new BasicNameValuePair(PostData.PHONE, "+7(916)053-24-99"));
//			dataPairs.add(new BasicNameValuePair(PostData.EMAIL, "complexityclass@gmail.com"));
//			dataPairs.add(new BasicNameValuePair(PostData.CERT_TYPE, "5140"));
//			dataPairs.add(new BasicNameValuePair(PostData.CERT_SER, ""));
//			dataPairs.add(new BasicNameValuePair(PostData.CERT_NUMBER, ""));
//			dataPairs.add(new BasicNameValuePair(PostData.CERT_ISSUER, ""));
//			dataPairs.add(new BasicNameValuePair(PostData.CERT_DATE, ""));
//			dataPairs.add(new BasicNameValuePair(PostData.RUSSIA_SUBJECT, "5104"));
//			dataPairs.add(new BasicNameValuePair(PostData.ZIPCODE, "141707"));
//			dataPairs.add(new BasicNameValuePair(PostData.REGION, "���������� �������"));
//			dataPairs.add(new BasicNameValuePair(PostData.ADDRESS, "������������"));
//			dataPairs.add(new BasicNameValuePair(PostData.STREET, "5219"));
//			dataPairs.add(new BasicNameValuePair(PostData.STREET2, "����������"));
//			dataPairs.add(new BasicNameValuePair(PostData.HOUSE, "10"));
//			dataPairs.add(new BasicNameValuePair(PostData.BUILDING, "32"));
//			dataPairs.add(new BasicNameValuePair(PostData.FLAT, "156"));
//
//			result = connect.doPost("https://pgu.khv.gov.ru/?a=PCSD", dataPairs, false); //
//			result = connect.doMultipartPost("https://pgu.khv.gov.ru/?a=PCSD", dataPairs, false);

			return result;

		}
	}

}

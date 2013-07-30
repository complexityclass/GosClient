package com.example.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

import com.example.http.Connect;
import com.example.http.PostData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.SslErrorHandler;

import android.graphics.Bitmap;

import android.net.http.SslError;

public class TextBrowser extends Activity {

	public final static String SURNAME = "PERSON*1[F*2][2664]";
	public final static String NAME = "PERSON*1[I*3][2776]";
	public final static String SECOND_NAME = "PERSON*1[O*4][2778]";
	public final static String BIRTH_DATE = "PERSON*1[BIRTHDATE*5][2782]";
	public final static String SEX = "PERSON*1[SEX*6][2783]";
	public final static String SNILS = "PERSON*1[SNILS*7][3649]";
	public final static String INN = "PERSON*1[INN*8][12803]";
	public final static String PHONE = "PERSON*1[TELEPHONE*9][10612]";
	public final static String EMAIL = "PERSON*1[EMAIL*10][7199]";
	public final static String CERT_TYPE = "CERT*11[TYPE*12][12812]";
	public final static String CERT_SER = "CERT*11[SER*13][12808]";
	public final static String CERT_NUMBER = "CERT*11[NUMBER*14][12810]";
	public final static String CERT_ISSUER = "CERT*11[ISSUER*15][17150]";
	public final static String CERT_DATE = "CERT*11[DATE*16][17152]";
	public final static String RUSSIA_SUBJECT = "REGISTRATION*17[RUSSIA_SUBJECT*18][12797]";
	public final static String ZIPCODE = "REGISTRATION*17[ZIPCODE*19][12801]";
	public final static String REGION = "REGISTRATION*17[REGION*20][12818]";
	public final static String ADDRESS = "REGISTRATION*17[ADDRESS*21][12820]";
	public final static String STREET = "REGISTRATION*17[STREET*22][12823]";
	public final static String STREET2 = "REGISTRATION*17[STREET*22][12824]";
	public final static String HOUSE = "REGISTRATION*17[HOUSE*23][12825]";
	public final static String BUILDING = "REGISTRATION*17[BUILDING*24][12827]";
	public final static String FLAT = "REGISTRATION*17[FLAT*25][12829]";

	public static final String url = "http://pgu.khv.gov.ru/";

	public static final String BASE_URL = "http://pgu.khv.gov.ru/";
	public static final String URL_1 = "https://pgu.khv.gov.ru/saml/module.php/core/khvLogin2.php?ref=&srv=http%3A%2F%2F172.18.205.4%3A7779%2FCore%2Fservices%2F&as=default-sp";

	public static Integer page = 1;

	WebView loginPage;
	CookieStore cookieStore;
	String allcookies = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_browser);

		final EditText text = (EditText) findViewById(R.id.LText);
		Button button = (Button) findViewById(R.id.Lbutton);

		loginPage = (WebView) findViewById(R.id.LView);
		WebSettings webSettings = loginPage.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSaveFormData(true);
		webSettings.setSavePassword(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		loginPage.setWebViewClient(new WebViewClient() {
			public void onPageLoad(WebView view, String url) {
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

			public void onPageFinished(final WebView view, String url) {
				String cookies = CookieManager.getInstance().getCookie(url);
				if (allcookies.equals("")) {
					allcookies += cookies;
				} else {
					allcookies += ("; " + cookies);
				}

			}

			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

		});

		startWebView();

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				cookieStore = getCookieStore(allcookies, "domain");

				NetworkThread netThread = new NetworkThread();
				netThread.execute(cookieStore);

				try {
					text.setText(netThread.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public BasicCookieStore getCookieStore(String cookies, String domain) {
		String[] cookieValues = cookies.split(";");
		BasicCookieStore cs = new BasicCookieStore();

		BasicClientCookie cookie;
		for (int i = 0; i < cookieValues.length; i++) {
			String[] split = cookieValues[i].split("=");
			if (split.length == 2) {
				cookie = new BasicClientCookie(split[0], split[1]);
			} else {
				cookie = new BasicClientCookie(split[0], null);
			}
			cs.addCookie(cookie);
		}
		return cs;
	}

	public void startWebView() {

		loginPage.loadUrl(URL_1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_browser, menu);
		return true;
	}

	private class NetworkThread extends AsyncTask<CookieStore, Integer, String> {

		@Override
		protected String doInBackground(CookieStore... params) {

			String result = "start";
			Connect connect = new Connect(getApplicationContext());

			System.out.println("----------------First GET START-----------------------------------------");
			result = connect.doGetwithSession("https://pgu.khv.gov.ru/?a=PersonCab&category=Details",
					"dimlcjjlb19vd09ou2hfu78cj7");
			System.out.println("----------------FIRST GET END-------------------------------------------");

			System.out.println("----------------MULT POST START------------------------------------------");
			List<NameValuePair> par = new ArrayList<NameValuePair>();
			par.add(new BasicNameValuePair(SURNAME, "Алекс"));
			par.add(new BasicNameValuePair(NAME, "Егор"));
			par.add(new BasicNameValuePair(SECOND_NAME, "Дмитриевич"));
			par.add(new BasicNameValuePair(BIRTH_DATE, "23.09.1992"));
			par.add(new BasicNameValuePair(SEX, "21"));
			par.add(new BasicNameValuePair(SNILS, "168 - 216 - 427 76"));
			par.add(new BasicNameValuePair(INN, ""));
			par.add(new BasicNameValuePair(PHONE, "+ 7(916)053 - 24 - 96"));
			par.add(new BasicNameValuePair(EMAIL, "complexityclass @ yandex.ru"));
			par.add(new BasicNameValuePair(CERT_TYPE, "6173"));
			par.add(new BasicNameValuePair(CERT_SER, "141700"));
			par.add(new BasicNameValuePair(CERT_NUMBER, "345700"));
			par.add(new BasicNameValuePair(CERT_ISSUER, "уап"));
			par.add(new BasicNameValuePair(CERT_DATE, "10.07.2013"));
			par.add(new BasicNameValuePair(RUSSIA_SUBJECT, "6185"));
			par.add(new BasicNameValuePair(ZIPCODE, "141707"));
			par.add(new BasicNameValuePair(REGION, "Московская область"));
			par.add(new BasicNameValuePair(ADDRESS, "Долгопрудный"));
			par.add(new BasicNameValuePair(STREET, "5219"));
			par.add(new BasicNameValuePair(STREET2, "Молодежная"));
			par.add(new BasicNameValuePair(HOUSE, "19"));
			par.add(new BasicNameValuePair(BUILDING, "33"));
			par.add(new BasicNameValuePair(FLAT, "156"));
			
			result = connect.doMultipartPost("https://pgu.khv.gov.ru/?a=PCSD", par, "dimlcjjlb19vd09ou2hfu78cj7");
			System.out.println("----------------MULT POST END--------------------------------------------");

			return result;

		}
	}

	public List<NameValuePair> setData(String... params) {
		List<NameValuePair> lister = new ArrayList<NameValuePair>();

		if (params.length > 10) {

			lister.add(new BasicNameValuePair(NAME, params[0]));
			lister.add(new BasicNameValuePair(SURNAME, params[1]));
			lister.add(new BasicNameValuePair(SECOND_NAME, params[2]));
			lister.add(new BasicNameValuePair(BIRTH_DATE, params[3]));
			lister.add(new BasicNameValuePair(SEX, params[4]));
			lister.add(new BasicNameValuePair(SNILS, params[5]));
			lister.add(new BasicNameValuePair(INN, params[6]));
			lister.add(new BasicNameValuePair(PHONE, params[7]));
			lister.add(new BasicNameValuePair(EMAIL, params[8]));
			lister.add(new BasicNameValuePair(CERT_TYPE, params[9]));
			lister.add(new BasicNameValuePair(CERT_SER, params[10]));
			lister.add(new BasicNameValuePair(CERT_NUMBER, params[11]));
			lister.add(new BasicNameValuePair(CERT_ISSUER, params[12]));
			lister.add(new BasicNameValuePair(CERT_DATE, params[13]));
			lister.add(new BasicNameValuePair(RUSSIA_SUBJECT, params[14]));
			lister.add(new BasicNameValuePair(ZIPCODE, params[15]));
			lister.add(new BasicNameValuePair(REGION, params[16]));
			lister.add(new BasicNameValuePair(ADDRESS, params[17]));
			lister.add(new BasicNameValuePair(STREET, params[18]));
			lister.add(new BasicNameValuePair(STREET2, params[19]));
			lister.add(new BasicNameValuePair(HOUSE, params[20]));
			lister.add(new BasicNameValuePair(BUILDING, params[21]));
			lister.add(new BasicNameValuePair(FLAT, params[22]));

		} else {
			System.out.println("error");
		}

		return lister;

	}

}

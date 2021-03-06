package com.example.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.util.Log;

public class PostData extends AsyncTask<Void, Integer, String> {

	Context context;

	public final static String NAME = "PERSON*1[F*2][2664]";
	public final static String SURNAME = "PERSON*1[I*3][2776]";
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
	public final static String CERT_ISSUER = "CERT*11[ISSUER*15][12814]";
	public final static String CERT_DATE = "CERT*11[DATE*16][12816]";
	public final static String RUSSIA_SUBJECT = "REGISTRATION*17[RUSSIA_SUBJECT*18][12797]";
	public final static String ZIPCODE = "REGISTRATION*17[ZIPCODE*19][12801]";
	public final static String REGION = "REGISTRATION*17[REGION*20][12818]";
	public final static String ADDRESS = "REGISTRATION*17[ADDRESS*21][12820]";
	public final static String STREET = "REGISTRATION*17[STREET*22][12823]";
	public final static String STREET2 = "REGISTRATION*17[STREET*22][12824]";
	public final static String HOUSE = "REGISTRATION*17[HOUSE*23][12825]";
	public final static String BUILDING = "REGISTRATION*17[BUILDING*24][12827]";
	public final static String FLAT = "REGISTRATION*17[FLAT*25][12829]";

	public PostData(Context context) {
		this.context = context;
	}

	public DefaultHttpClient getClient() {

		DefaultHttpClient ret = null;

		// Sets up http parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);

		// Registers schemes for both http and https
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
		sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
		ret = new DefaultHttpClient(manager, params);

		return ret;

	}

	public void postData() {

		HttpClient httpClient = getClient();

		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		HttpPost httppost = new HttpPost("https://esia.gosuslugi.ru/idp/Authn/CommonLogin");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("username", "168-216-427+76"));
			nameValuePairs.add(new BasicNameValuePair("password", "2391423choco"));
			nameValuePairs.add(new BasicNameValuePair("answer", ""));
			nameValuePairs.add(new BasicNameValuePair("globalRole", "RF_PERSON"));
			nameValuePairs.add(new BasicNameValuePair("capture", ""));
			nameValuePairs.add(new BasicNameValuePair("phraseId", ""));
			nameValuePairs.add(new BasicNameValuePair("cmsDS", ""));
			nameValuePairs.add(new BasicNameValuePair("isRegCheck", "false"));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			try {
				HttpResponse response = httpClient.execute(httppost, localContext);

				boolean responseIsNull = (response.getEntity().getContentLength() == 0) ? true : false;

				if (responseIsNull) {
					System.out.println("Response is null");
				} else {
					StringBuilder sb = new StringBuilder();
					HttpEntity entity = response.getEntity();
					try {

						BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()), 65728);
						String line = null;

						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}

						System.out.println("final result ==== " + sb.toString());
						
						Log.d(EMAIL, sb.toString());

						/*
						String filename = "loghtml.txt";

						FileOutputStream outputStream = null;

						try {

							outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
							outputStream.write(sb.toString().getBytes());
							outputStream.close();

						} catch (Exception e) {
							e.printStackTrace();
						}

						File file = new File(System.getProperty("user.dir") + "\\htmllog.txt");
						if (!file.exists()) {
							file.createNewFile();
						}

						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(sb.toString());
						bw.close();
						*/

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * HttpPost httppostData = new
		 * HttpPost("https://pgu.khv.gov.ru/?a=PersonCab&category=Details");
		 * 
		 * MultipartEntity entity = new
		 * MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		 * 
		 * List<NameValuePair> listData = setData("�����", "�������",
		 * "�������������", "23.04.1992", "21", "168-216-427 76", "",
		 * "+7(916)053-24-99", "mail@mail.ru", "	5140", "", "", "", "", "5104",
		 * "141707", "���������� �������", "������������", "5219", "����������",
		 * "10", "32", "156");
		 * 
		 * try { httppostData.setEntity(new UrlEncodedFormEntity(listData)); }
		 * catch (UnsupportedEncodingException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } try { HttpResponse response2 =
		 * httpClient.execute(httppostData, localContext); } catch
		 * (ClientProtocolException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
	}

	@Override
	protected String doInBackground(Void... params) {

		postData();

		return new String("data posted !!!!!!!!");
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

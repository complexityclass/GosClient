package com.example.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.webkit.WebSettings;

/**
 * @author complexityclass
 * 
 *         Class to get/post request to pgu.khv.gov.ru and save cookies
 */
public class Connect {

	public static final String URL = "http://pgu.khv.gov.ru/";

	private HttpClient client;
	private Context context;
	private CookieStore cookieStore;

	public Connect(Context context) {

		/*
		 * DefaultHttpClient ret = null;
		 * 
		 * // Sets up http parameters HttpParams params = new BasicHttpParams();
		 * HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		 * HttpProtocolParams.setContentCharset(params, "utf-8");
		 * params.setBooleanParameter("http.protocol.expect-continue", false);
		 * 
		 * // Registers schemes for both http and https SchemeRegistry registry
		 * = new SchemeRegistry(); registry.register(new Scheme("http",
		 * PlainSocketFactory.getSocketFactory(), 80));
		 * 
		 * final SSLSocketFactory sslSocketFactory =
		 * SSLSocketFactory.getSocketFactory();
		 * sslSocketFactory.setHostnameVerifier
		 * (SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		 * 
		 * registry.register(new Scheme("https", sslSocketFactory, 443));
		 * ThreadSafeClientConnManager manager = new
		 * ThreadSafeClientConnManager(params, registry); ret = new
		 * DefaultHttpClient(manager, params);
		 */

		this.client = getNewHttpClient();
		this.context = context;
		CookieHandler.setDefault(new CookieManager());
		cookieStore = new BasicCookieStore();

	}

	public HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public StringBuffer topgu(String url) {

		HttpGet request = new HttpGet(url);
		HttpProtocolParams.setUserAgent(client.getParams(), "My funcy UA");
		StringBuffer result = null;
		try {

			HttpResponse response = client.execute(request);
			System.out.println("Response Code:" + response.getStatusLine().getStatusCode());

			/* Print response body into String */
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			result = new StringBuffer();
			String line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			Header[] cookiesArray = response.getHeaders("Set-Cookie");

			/*
			 * Header[] allHeaders = response.getAllHeaders(); for (int i = 0; i
			 * < allHeaders.length; i++) {
			 * System.out.println("---------------------Header " + i +
			 * ":-------------------------"); System.out.println("name : " +
			 * allHeaders[i].getName()); System.out.println("value :" +
			 * allHeaders[i].getValue()); System.out.println(
			 * "---------------------------------------------------------------"
			 * );}
			 */

			for (int i = 0; i < cookiesArray.length; i++) {
				Cookie cookie = new BasicClientCookie(cookiesArray[i].getName().toString(), cookiesArray[i].getValue()
						.toString());
				cookieStore.addCookie(cookie);
			}

			List<Cookie> cookieList = cookieStore.getCookies();
			for (int i = 0; i < cookieList.size(); i++) {
				System.out.println("Cookie " + "name :" + cookieList.get(i).getName() + "value :"
						+ cookieList.get(i).getValue());
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public String doGet(String url) {
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		HttpGet request = new HttpGet(url);
		HttpProtocolParams.setUserAgent(client.getParams(), "My funcy UA");

		try {
			HttpResponse response = client.execute(request, localContext);
			System.out.println("Response Code:" + response.getStatusLine().getStatusCode());

			Header[] cookiesArray = response.getHeaders("Set-Cookie");

			for (int i = 0; i < cookiesArray.length; i++) {
				Cookie cookie = new BasicClientCookie(cookiesArray[i].getName().toString(), cookiesArray[i].getValue()
						.toString());
				cookieStore.addCookie(cookie);
			}

			System.out.println();

			List<Cookie> cookieList = cookieStore.getCookies();
			for (int i = 0; i < cookieList.size(); i++) {
				System.out.println("Cookie " + "name :  " + cookieList.get(i).getName() + "   value :"
						+ cookieList.get(i).getValue());
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "second get complited!";

	}

	public String doPost(String url, List<NameValuePair> urlParametrs) {
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		StringBuffer result = null;

		HttpPost request = new HttpPost(url);
		HttpProtocolParams.setUserAgent(client.getParams(), "My funcy UA");

		try {
			request.setEntity(new UrlEncodedFormEntity(urlParametrs));
			try {
				HttpResponse response = client.execute(request, localContext);
				System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

				Header[] cookiesArray = response.getHeaders("Set-Cookie");

				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				result = new StringBuffer();
				String line = "";

				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				for (int i = 0; i < cookiesArray.length; i++) {
					Cookie cookie = new BasicClientCookie(cookiesArray[i].getName().toString(), cookiesArray[i]
							.getValue().toString());
					cookieStore.addCookie(cookie);
				}

				List<Cookie> cookieList = cookieStore.getCookies();
				for (int i = 0; i < cookieList.size(); i++) {
					System.out.println("Cookie " + "name :  " + cookieList.get(i).getName() + "   value :"
							+ cookieList.get(i).getValue());
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result.toString();

	}

	public String doMultipartPost(String url, List<NameValuePair> urlParameters) {

		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		HttpPost request = new HttpPost(url);
		HttpProtocolParams.setUserAgent(client.getParams(), "My funcy UA");

		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		StringBuffer result = null;

		for (NameValuePair nvp : urlParameters) {
			try {
				entity.addPart(nvp.getName(), new StringBody(nvp.getValue()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		request.setEntity(entity);

		try {
			HttpResponse response = client.execute(request, localContext);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			result = new StringBuffer();
			String line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			Header[] cookiesArray = response.getHeaders("Set-Cookie");

			for (int i = 0; i < cookiesArray.length; i++) {
				Cookie cookie = new BasicClientCookie(cookiesArray[i].getName().toString(), cookiesArray[i].getValue()
						.toString());
				cookieStore.addCookie(cookie);
			}

			List<Cookie> cookieList = cookieStore.getCookies();
			for (int i = 0; i < cookieList.size(); i++) {
				System.out.println("Cookie " + "name :  " + cookieList.get(i).getName() + "   value :"
						+ cookieList.get(i).getValue());
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}

}

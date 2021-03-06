package com.complexityclass.cryp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.ssl.Provider;
import ru.CryptoPro.ssl.util.cpSSLConfig;

public class HttpTLSExample {

	private static String remoteHost;
	private static int remotePort;
	private static String downloadPage;

	public InputStream trustedStrore;
	public boolean needClientAuth;
	public char[] keyStorePassword;
	public char[] trustedStorePassword;

	public static final int MAX_CLIENT_TIMEOUT = 60 * 60 * 1000;
	public static final String DEFAULT_ENCODING = "windows-1251";

	static {
		Security.setProperty("ssl.KeyManagerFactory.algorithm", Provider.KEYMANGER_ALG);
		Security.setProperty("ssl.TrustManagerFactory.algorithm", Provider.KEYMANGER_ALG);

		Security.setProperty("ssl.SocketFactory.provider", "ru.CryptoPro.ssl.SSLSocketFactoryImpl");
		Security.setProperty("ssl.ServerSocketFactory.provider", "ru.CryptoPro.ssl.SSLServerSocketFactoryImpl");

	}

	static {

		// ��� �������� ������� �����������.
		System.setProperty("com.sun.security.enableCRLDP", "true");
		System.setProperty("com.ibm.security.enableCRLDP", "true");

		// ��������� �����������, �������, ���������� �� ���������.
		cpSSLConfig.setDefaultSSLProvider(JCSP.PROVIDER_NAME);
	}

	public HttpTLSExample(InputStream ts, char[] tsp, boolean clientAuth, char[] ksp, String host, int port, String page) {
		this.trustedStrore = ts;
		this.trustedStorePassword = tsp;
		this.keyStorePassword = ksp;
		this.needClientAuth = clientAuth;

		this.remoteHost = host;
		this.remotePort = port;
		this.downloadPage = page;

	}

	public String getResult() {
		return new HttpTLSThread().get();
	}

	private class HttpTLSThread {

		public StringBuffer result = new StringBuffer();

		HttpTLSThread() {
			result.append("init : ");
		}

		public void execute() throws Exception {

			HttpClient httpClient = null;

			try {

				final String httpAddress = "https://" + remoteHost + ":" + remotePort + File.separator + downloadPage;
				URI url = new URI(httpAddress);

				KeyStore ts = KeyStore.getInstance(JCSP.CERT_STORE_NAME, JCSP.PROVIDER_NAME);
				ts.load(trustedStrore, null);

				KeyStore ks = null;
				if (needClientAuth) {
					ks = KeyStore.getInstance(JCSP.HD_STORE_NAME, JCSP.PROVIDER_NAME);
					ks.load(null, null);
					System.out.println("Need Auth");
				}

				String keyStorePasswordStr = keyStorePassword == null ? null : String.valueOf(keyStorePassword);

				System.out.println("Create Socket Factory");
				SSLSocketFactory socketFactory = new SSLSocketFactory(Provider.ALGORITHM, ks, keyStorePasswordStr, ts,
						null, null);

				System.out.println("Register https scheme");
				Scheme httpsScheme = new Scheme("https", socketFactory, remotePort);
				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(httpsScheme);

				System.out.println("Set connection options");
				HttpParams params = new BasicHttpParams();
				HttpConnectionParams.setSoTimeout(params, MAX_CLIENT_TIMEOUT);
				ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
				httpClient = new DefaultHttpClient(cm, params);

				System.out.println("Execute GET request");
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpget);
				HttpEntity entity = response.getEntity();

				int status = response.getStatusLine().getStatusCode();
				if (status != 200) {
					System.out.println("Bad request status != 200OK");
					return;
				} else {
					System.out.println("Request 200 OK");
				}

				if (entity != null) {

					// �������� ������ ���������.
					InputStream is = entity.getContent();

					BufferedReader in = new BufferedReader(new InputStreamReader(is, DEFAULT_ENCODING));

					System.out.println("Read response");

					// ������� �����.
					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
						result.append(line);
					} // while

					if (in != null) {
						in.close();
					} // if

					System.out.println("OK");

				} // if

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed.");
				throw e;
			} finally {
				if (httpClient != null) {
					System.out.println("Shutdown http connection.");
					httpClient.getConnectionManager().shutdown();
				}
			}

		}

		public String get() {
			try {
				execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result.toString();
		}

	}

}

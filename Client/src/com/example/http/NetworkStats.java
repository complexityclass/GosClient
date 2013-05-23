package com.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Help class for Internet connection status
 * 
 * @author complexityclass
 * 
 */
public class NetworkStats {

	/**
	 * Return false when network connection is disabled
	 * 
	 * @param Context
	 *            context : app stable
	 * */
	public static boolean isNetworkAvailable(Context context) {
		boolean availabel = false;

		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {
			availabel = true;
		}

		return availabel;
	}

	/**
	 * Write the GET response from server to InputStream
	 * 
	 * @param String
	 *            strUrl : url of web resourse
	 */
	public static InputStream getHttpConnection(String strUrl)
			throws IOException {
		InputStream iStream = null;
		URL url = new URL(strUrl);
		URLConnection connection = url.openConnection();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				iStream = httpConnection.getInputStream();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return iStream;
	}

	/** Obtain InputStream with response and convert it to String(HTML) */
	public static String getOutputFromURL(String url) {

		StringBuffer output = new StringBuffer("");
		try {
			InputStream stream = getHttpConnection(url);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					stream));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				output.append(s);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return output.toString();
	}
	

}

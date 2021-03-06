import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ConnectClass {

	public static final String URL = "http://pgu.khv.gov.ru/";
	public static final String USER_AGENT = "Mozilla/5.0";

	private String cookies;
	private HttpClient client;

	ConnectClass() {
		client = new DefaultHttpClient();
	}

	public static void main(String[] args) {

		String firstURL = "http://pgu.khv.gov.ru/";
		File logOne = new File("firstLog.txt");
		
		String googleURL = "http://www.google.com/search?q=httpClient";
		
		ConnectClass connect = new ConnectClass();
		try {
			connect.doGet(googleURL, logOne);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public void doGet(String url, File logFile) throws Exception {

		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);

		System.out.println("Response Code:"
				+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "Start html";

		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		if (!logFile.exists()) {
			logFile.createNewFile();
		}

		FileWriter fw = new FileWriter(logFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(result.toString());
		bw.close();

		System.out.println("Done");

	}

}

package com.complexityclass.cryp;

import java.io.File;

import java.io.InputStream;
import java.security.Security;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.CryptoPro.JCSP.CSPConfig;
import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.JCSP.tools.common.CSPTool;
import ru.CryptoPro.JCSP.tools.common.RawResource;
import ru.CryptoPro.reprov.RevCheck;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static final String SIGN_CONTAINER_NAME = "clientTL.000";
	public static final String SERVER_CONTAINER_NAME = "serverTL.000";

	public static final String CLIENT_CONTAINER_NAME = SIGN_CONTAINER_NAME;

	public static final char[] DEFAULT_TRUSTED_STORE_PASSWORD = "1".toCharArray();
	public static final String DEFAULT_HOST = "cpca.cryptopro.ru";
	public static final int DEFAULT_PORT = 443;
	public static final String DEFAULT_PAGE = "default.htm";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final int initialized = CSPConfig.initInAndroid(this);
		final EditText textField = (EditText) findViewById(R.id.editText1);
		Button button = (Button) findViewById(R.id.button1);

		System.out.println("Init status =" + initialized);

		final String result = initProviders();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("[***********TLS channel using apache http client************]");

				try {

					TLSConnect tlsconnect = new TLSConnect();
					tlsconnect.execute("connect");

					System.out.println("in response !!!!" + tlsconnect.get());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	private class TLSConnect extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String response = params[0] + " ";
			InputStream trustedStoreStream = getResources().openRawResource(R.raw.truststore);
			boolean clientAuth = false;

			HttpTLSExample tlsExample = new HttpTLSExample(trustedStoreStream, DEFAULT_TRUSTED_STORE_PASSWORD,
					clientAuth, null, DEFAULT_HOST, DEFAULT_PORT, DEFAULT_PAGE);

			return response + tlsExample.getResult();
		}

	}

	private String initProviders() {

		String result = "Provider's initialization : ";

		if (Security.getProvider(JCSP.PROVIDER_NAME) == null) {
			Security.addProvider(new JCSP());
			result += "[ adding new JCSP provider ]";
		} // if

		if (Security.getProvider(ru.CryptoPro.ssl.Provider.PROVIDER_NAME) == null) {
			Security.addProvider(new ru.CryptoPro.ssl.Provider());
			result += "[ adding new Crypto.ssl.Provider ]";
		} // if

		if (Security.getProvider(RevCheck.PROVIDER_NAME) == null) {
			Security.addProvider(new RevCheck());
			result += "[ adding new RevCheck ]";
		} // if

		return result;

	}

	private void installContainers() throws Exception {

		Map<Integer, String> containerFiles = new HashMap<Integer, String>();
		containerFiles.put(Integer.valueOf(R.raw.clienttls_header), "header.key");
		containerFiles.put(Integer.valueOf(R.raw.clienttls_masks), "masks.key");
		containerFiles.put(Integer.valueOf(R.raw.clienttls_masks2), "masks2.key");
		containerFiles.put(Integer.valueOf(R.raw.clienttls_name), "name.key");
		containerFiles.put(Integer.valueOf(R.raw.clienttls_primary), "primary.key");
		containerFiles.put(Integer.valueOf(R.raw.clienttls_primary2), "primary2.key");

		installContainer(CLIENT_CONTAINER_NAME, containerFiles);

		containerFiles = new HashMap<Integer, String>();
		containerFiles.put(Integer.valueOf(R.raw.servertls_header), "header.key");
		containerFiles.put(Integer.valueOf(R.raw.servertls_masks), "masks.key");
		containerFiles.put(Integer.valueOf(R.raw.servertls_masks2), "masks2.key");
		containerFiles.put(Integer.valueOf(R.raw.servertls_name), "name.key");
		containerFiles.put(Integer.valueOf(R.raw.servertls_primary), "primary.key");
		containerFiles.put(Integer.valueOf(R.raw.servertls_primary2), "primary2.key");

		installContainer(SERVER_CONTAINER_NAME, containerFiles);

	}

	private void installContainer(String containerName, Map<Integer, String> containerFiles) throws Exception {

		Log.d("install container", "install container " + containerName);
		CSPTool cspTool = new CSPTool(this);
		RawResource resource = new RawResource(this, cspTool.getAppInfrastructure().getKeysDirectory() + File.separator
				+ userName2Dir() + File.separator + containerName);

		Iterator<Integer> iterator = containerFiles.keySet().iterator();

		while (iterator.hasNext()) {
			Integer index = iterator.next();
			String fileName = containerFiles.get(index);
			if (!resource.copy(index, fileName)) {
				throw new Exception("Couldn't copy " + fileName);
			} // if
		} // while

	}

	private String userName2Dir() throws Exception {

		ApplicationInfo appInfo = getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo;

		return String.valueOf(appInfo.uid) + "." + String.valueOf(appInfo.uid);
	}

}

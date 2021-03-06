package com.example.client;

import java.io.File;
import java.io.InputStream;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;

import com.example.crypto.HttpTLS;
import com.example.crypto.IEncryptDecryptData;
import com.example.crypto.ITLSData;
import com.example.crypto.LogCallback;

import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.CryptoPro.JCSP.CSPConfig;
import ru.CryptoPro.JCSP.JCSP;
import ru.CryptoPro.JCSP.tools.common.CSPTool;
import ru.CryptoPro.JCSP.tools.common.Constants;
import ru.CryptoPro.JCSP.tools.common.RawResource;
import ru.CryptoPro.reprov.RevCheck;

public class CryptoProActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crypto_pro);

		Button cryptoButton = (Button) findViewById(R.id.cryptoButton);
		final TextView cryptoView = (TextView) findViewById(R.id.cryptoView);
		final EditText cryptoEdit = (EditText) findViewById(R.id.editCrypto);

		cryptoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					InputStream trustedStoreStream = getResources().openRawResource(R.raw.truststore);

					boolean clientAuth = false;

					// ���� ��� ���������� ��������������.

					LogCallback call = new LogCallback(cryptoEdit, cryptoView);

					IEncryptDecryptData tlsExample = new HttpTLS(trustedStoreStream,
							ITLSData.DEFAULT_TRUSTED_STORE_PASSWORD, clientAuth, null, ITLSData.DEFAULT_HOST,
							ITLSData.DEFAULT_PORT, ITLSData.DEFAULT_PAGE);

					tlsExample.getResult(call);

				} catch (Exception e) {

					Log.e(Constants.APP_LOGGER_TAG, e.getMessage());

				}

			}
		});

		/* Initialization CSP provider */
		int initialized = CSPConfig.initInAndroid(this);

		if (initialized != CSPConfig.CSP_INIT_OK) {
			switch (initialized) {

			case CSPConfig.CSP_INIT_CONTEXT:
				Log.d("error", "Couldn't initialize context");
				return;
			case CSPConfig.CSP_INIT_CREATE_INFRASTRUCTURE:
				Log.d("error", "Couldn't create CSP infrastructure");
				return;
			case CSPConfig.CSP_INIT_COPY_RESOURCES:
				Log.d("error", "Couldn't copy CSP resources");
				return;
			case CSPConfig.CSP_INIT_CHANGE_WORK_DIR:
				Log.d("error", "Couldn't change CSP working directory");

			}
		}

		initProviders();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crypto, menu);
		return true;
	}

	private void initProviders() {
		if (Security.getProvider(JCSP.PROVIDER_NAME) == null) {
			Security.addProvider(new JCSP());
		}

		if (Security.getProvider(ru.CryptoPro.ssl.Provider.PROVIDER_NAME) == null) {
			Security.addProvider(new ru.CryptoPro.ssl.Provider());
		}

		if (Security.getProvider(RevCheck.PROVIDER_NAME) == null) {
			Security.addProvider(new RevCheck());
		}
	}

}

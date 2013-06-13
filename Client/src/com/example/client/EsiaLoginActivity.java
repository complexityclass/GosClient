package com.example.client;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EsiaLoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esia_login);

		EditText loginField = (EditText) findViewById(R.id.loginText);
		EditText passField = (EditText) findViewById(R.id.passText);
		Button logButton = (Button) findViewById(R.id.logbut);

		//loginField.setHint("СНИЛС/логин");
		//passField.setHint("Пароль");

		logButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "OLOLOLO", Toast.LENGTH_LONG);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.esia_login, menu);
		return true;
	}

}

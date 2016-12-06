package com.example.abc;

import com.example.abc.fragments.PasswordRecoverStep1Fragment;
import com.example.abc.fragments.PasswordRecoverStep2Fragment;

import android.app.Activity;
import android.os.Bundle;

public class PasswordRecoverActivity extends Activity {
	
	PasswordRecoverStep1Fragment fragStep1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment fragStep2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recover);
		getFragmentManager().beginTransaction().add(R.id.container, fragStep1).commit();
		
	}
}

package com.example.abc;

import com.example.abc.fragments.PasswordRecoverStep1Fragment;
import com.example.abc.fragments.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.example.abc.fragments.PasswordRecoverStep2Fragment;

import android.app.Activity;
import android.os.Bundle;

public class PasswordRecoverActivity extends Activity {
	
	PasswordRecoverStep1Fragment fragStep1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment fragStep2 = new PasswordRecoverStep2Fragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recover);
		getFragmentManager().beginTransaction().add(R.id.container, fragStep1).commit();
		fragStep1.setOnGoNextListener(new OnGoNextListener() {
			
			@Override
			public void onGoNext() {
				goStep2();
			}
		});
	}
	
	void goStep2() {
		
		getFragmentManager()
		.beginTransaction()
//		.setCustomAnimations(enter, exit, popEnter, popExit)
		.replace(R.id.container, fragStep2)
		.addToBackStack(null)
		.commit();
		
	}
}

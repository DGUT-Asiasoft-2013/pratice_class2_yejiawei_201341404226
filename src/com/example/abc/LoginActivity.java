package com.example.abc;

import java.io.IOException;

import com.example.abc.api.Server;
import com.example.abc.api.entity.User;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.abc.security.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment fragAccount, fragPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goRegister();
			}
		});
		
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goLogin();
			}
		});
		
		findViewById(R.id.btn_forget).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goRecoverPassword();
			}
		});
		
		fragAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
//		findViewById(R.id.btn_login)
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragAccount.setLabelText("用户名");
		fragAccount.setHintText("请输入用户名");
		fragPassword.setLabelText("密码 ");
		fragPassword.setHintText("请输入密码");
		fragPassword.setIsPassword(true);
	}
	
	
	void goRegister() {
		Intent itnt = new Intent(this, RegisterActivity.class);
		startActivity(itnt);
		
	}
	
	void goLogin() {
		String account = fragAccount.getText();
		String password = fragPassword.getText();
		password = MD5.getMD5(password);
		
		OkHttpClient client = Server.getSharedClient();
//				new OkHttpClient();
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.addFormDataPart("account", account)
				.addFormDataPart("passwordHash", password);
		
		Request request = Server.requestBuilderWithApi("login")
				.method("post", null).post(requestBodyBuilder.build()).build();
//				Server.getSharedClient();
//				new Request.Builder()
//				.url("http://172.27.0.5:8080/membercenter/api/login")
//				.method("post", null).post(requestBodyBuilder.build())
//				.build();
		
		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setMessage("正在登陆");
		dlg.show();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							ObjectMapper mapper = new ObjectMapper();
							final User user = mapper.readValue(arg1.body().string(), User.class);
							
							LoginActivity.this.onResponse(arg0, user.getAccount());
							
						} catch (IOException e) {
							e.printStackTrace();
							LoginActivity.this.onFailture(arg0, e);
						}
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				onFailture(arg0, arg1);
			}
		});
		
//		Intent itnt = new Intent(this, HelloWorldActivity.class);
//		startActivity(itnt);
	}
	
	void goRecoverPassword() {
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
		
	}
	
	void onResponse(Call arg0, String responseBody) {
		new AlertDialog.Builder(this)
		.setTitle("登陆成功")
		.setMessage(responseBody)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent itnt = new Intent(LoginActivity.this, HelloWorldActivity.class);
				startActivity(itnt);
				finish();
			}
		})
		.show();
	}
	
	void onFailture(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("登陆失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
}

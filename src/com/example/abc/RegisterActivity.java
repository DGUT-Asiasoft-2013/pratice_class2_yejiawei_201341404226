package com.example.abc;

import java.io.IOException;

import com.example.abc.fragments.inputcells.PictureInputCellFragment;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.abc.security.MD5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment	fragInputCellEmailAdress;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	SimpleTextInputCellFragment fragInputCellName;
	PictureInputCellFragment fragInputCellAvatar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputCellAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_avatar);
		fragInputCellEmailAdress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
	
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragInputCellAccount.setLabelText("�û���");
		fragInputCellAccount.setHintText("�������û���");
		fragInputCellEmailAdress.setLabelText("��������");
		fragInputCellEmailAdress.setHintText("�������������");
		fragInputCellPassword.setLabelText("����");
		fragInputCellPassword.setHintText("����������");
		fragInputCellPassword.setIsPassword(true);
		fragInputCellPasswordRepeat.setLabelText("�ظ�����");
		fragInputCellPasswordRepeat.setHintText("�������ظ�����");
		fragInputCellPasswordRepeat.setIsPassword(true);
		fragInputCellName.setLabelText("�ǳ�");
		fragInputCellName.setHintText("�������ǳ�");
		fragInputCellAvatar.setLabelText("ѡ��ͼƬ");
		fragInputCellAvatar.setHintText("������ͼƬ");
	}
	
	void submit() {
		String password = fragInputCellPassword.getText();
		String passwordRepeat = fragInputCellPasswordRepeat.getText();
		if(!password.equals(passwordRepeat)) {
			new AlertDialog.Builder(RegisterActivity.this)
			.setMessage("�������벻һ��")
			.setNegativeButton("OK", null)
			.show();
			return;
		}
		
		password = MD5.getMD5(password);
		String account = fragInputCellAccount.getText();
		String email = fragInputCellEmailAdress.getText();
		String name = fragInputCellName.getText();
	
		
		OkHttpClient client = new OkHttpClient();
		
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.addFormDataPart("account", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password);
				
		
		if(fragInputCellAvatar.getPngData() != null) {
			requestBodyBuilder.addFormDataPart("avatar", "avatar", RequestBody
					.create(MediaType.parse("image/png"), fragInputCellAvatar.getPngData()));
		}
		
		Request request = new Request.Builder()
				.url("http://172.27.0.45:8080/membercenter/api/register")
				.method("post", null).post(requestBodyBuilder.build())
				.build();
		
		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("���Ժ�");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							progressDialog.dismiss();
							RegisterActivity.this.onResponse(arg0, arg1.body().string());
						} catch (IOException e) {
							e.printStackTrace();
							RegisterActivity.this.onFailture(arg0, e);
						}
					}
				});
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						onFailure(arg0, arg1);
					}
				});
			}
		});
	}
	
	void onResponse(Call arg0, String responseBody) {
		new AlertDialog.Builder(this)
		.setTitle("ע��ɹ�")
		.setMessage(responseBody)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		})
		.show();
	}
	
	void onFailture(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("ע��ʧ��")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
}

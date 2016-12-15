package com.example.abc;

import java.io.IOException;

import com.example.abc.api.Server;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.abc.security.MD5;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class ChangePasswordActivity extends Activity {
	SimpleTextInputCellFragment fragBeforePassword, fragNewPassword, fragNewPassowrdRepeat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avtivity_change_password);
		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goChange();
			}
		});
		fragBeforePassword = (SimpleTextInputCellFragment) getFragmentManager()
				.findFragmentById(R.id.input_before_passowrd);
		fragNewPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_new_password);
		fragNewPassowrdRepeat = (SimpleTextInputCellFragment) getFragmentManager()
				.findFragmentById(R.id.input_new_password_repeat);

	}

	@Override
	protected void onResume() {
		super.onResume();
		fragBeforePassword.setLabelText("原密码");
		fragBeforePassword.setHintText("请输入原密码");
		fragBeforePassword.setIsPassword(true);
		fragNewPassword.setLabelText("新密码");
		fragNewPassword.setHintText("请输入新密码");
		fragBeforePassword.setIsPassword(true);
		fragNewPassowrdRepeat.setLabelText("重复新密码");
		fragNewPassowrdRepeat.setHintText("请重复输入新密码");
		fragNewPassowrdRepeat.setIsPassword(true);
	}

	void goChange() {
		String beforePassword = fragBeforePassword.getText();
		String newPassword = fragNewPassword.getText();
		String newPasswordRepeat = fragNewPassowrdRepeat.getText();
		if (newPassword.equals(newPasswordRepeat)) {
			MultipartBody.Builder body = new MultipartBody.Builder()
					.addFormDataPart("passwordHash", MD5.getMD5(beforePassword))
					.addFormDataPart("newPasswordHash", MD5.getMD5(newPassword));

			Request request = Server.requestBuilderWithApi("change").method("post", null).post(body.build()).build();
			Server.getSharedClient().newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(final Call arg0, final Response arg1) throws IOException {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							boolean isChange;
							try {
								isChange = new ObjectMapper().readValue(arg1.body().string(), Boolean.class);
								if (isChange) {

									ChangePasswordActivity.this.onResponse(arg0, arg1);

								} else {
									onFail();
								}
							} catch (JsonParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JsonMappingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						}
					});

				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	void onResponse(Call arg0, Response arg1) {
		new AlertDialog.Builder(this).setMessage("修改成功").show();
	}

	void onFail() {
		new AlertDialog.Builder(this).setMessage("修改失败").show();
	}
}

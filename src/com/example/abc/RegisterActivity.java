package com.example.abc;

import com.example.abc.fragments.inputcells.PictureInputCellFragment;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment	fragInputCellEmailAdress;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	
	PictureInputCellFragment fragInputCellPicture;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputCellPicture = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_picture);
		fragInputCellEmailAdress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名");
		fragInputCellEmailAdress.setLabelText("电子邮箱");
		fragInputCellEmailAdress.setHintText("请输入电子邮箱");
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
		fragInputCellPassword.setIsPassword(true);
		fragInputCellPasswordRepeat.setLabelText("重复密码");
		fragInputCellPasswordRepeat.setHintText("请输入重复密码");
		fragInputCellPasswordRepeat.setIsPassword(true);
		fragInputCellPicture.setLabelText("选择图片");
		fragInputCellPicture.setHintText("请输入图片");
	}
}

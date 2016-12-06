package com.example.abc;

import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;

public class FindPasswordActivity extends Activity{

	SimpleTextInputCellFragment fragAccount, fragEmailAddress, fragNewPassword, fragNewPasswordRepeat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
		fragAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragEmailAddress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
		fragNewPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_newpassword);
		fragNewPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_newpassword_repeat);
		
//		findViewById(R.id.btn_submit).setonC
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragAccount.setLabelText("用户名");
		fragAccount.setHintText("请输入用户名");
		fragEmailAddress.setLabelText("电子邮箱");
		fragEmailAddress.setHintText("请输入电子邮箱");
		fragNewPassword.setLabelText("新密码");
		fragNewPassword.setHintText("请输入新密码");
		fragNewPassword.setIsPassword(true);
		fragNewPasswordRepeat.setLabelText("重复新密码");
		fragNewPasswordRepeat.setHintText("请重复输入密码");
		fragNewPasswordRepeat.setIsPassword(true);
	}
}

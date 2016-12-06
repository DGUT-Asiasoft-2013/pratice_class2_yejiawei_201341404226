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
		fragAccount.setLabelText("�û���");
		fragAccount.setHintText("�������û���");
		fragEmailAddress.setLabelText("��������");
		fragEmailAddress.setHintText("�������������");
		fragNewPassword.setLabelText("������");
		fragNewPassword.setHintText("������������");
		fragNewPassword.setIsPassword(true);
		fragNewPasswordRepeat.setLabelText("�ظ�������");
		fragNewPasswordRepeat.setHintText("���ظ���������");
		fragNewPasswordRepeat.setIsPassword(true);
	}
}

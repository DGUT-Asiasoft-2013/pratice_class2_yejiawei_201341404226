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
		fragInputCellPicture.setLabelText("ѡ��ͼƬ");
		fragInputCellPicture.setHintText("������ͼƬ");
	}
}

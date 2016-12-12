package com.example.abc.fragments;

import com.example.abc.R;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PasswordRecoverStep2Fragment extends Fragment{
	View view;
	
	SimpleTextInputCellFragment fragVerifyCode;
	SimpleTextInputCellFragment fragNewPassword;
	SimpleTextInputCellFragment fragNewPasswordRepeat;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null) {
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
			fragVerifyCode = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verifycode);
			fragNewPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_newpassword);
			fragNewPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_newpassword_repeat);
			view.findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onSubmitClicked();
				}
			});
		}
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		fragVerifyCode.setLabelText("验证码");
		fragVerifyCode.setHintText("请输入验证码");
		fragNewPassword.setLabelText("新密码");
		fragNewPassword.setHintText("请输入新密码");
		fragNewPasswordRepeat.setLabelText("重复密码");
		fragNewPasswordRepeat.setHintText("请重复输入密码");
	}
	
	public String getText() {
		return fragNewPassword.getText();
	}
	
	public static interface OnSubmitClickedListener{
		void onSubmitClicked();
	};
	
	OnSubmitClickedListener onSubmitClickeedListener;
	
	public void setOnSubmitClickeedListener(OnSubmitClickedListener onSubmitClickeedListener) {
		this.onSubmitClickeedListener = onSubmitClickeedListener;
	}
	
	void onSubmitClicked() {
		if(fragNewPassword.getText().equals(fragNewPasswordRepeat.getText())) {
			onSubmitClickeedListener.onSubmitClicked();
		} else {
			new AlertDialog.Builder(getActivity())
			.setMessage("两次密码不一致")
			.show();
		}
	}
}

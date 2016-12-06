package com.example.abc.fragments;

import com.example.abc.R;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class PasswordRecoverStep1Fragment extends Fragment{
	View view;
	SimpleTextInputCellFragment fragEmail;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null) {
			view = inflater.inflate(R.layout.fragment_password_recover_step1, null);
			
			fragEmail = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
			
			view.findViewById(R.id.btn_next).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goNext();
				}
			});
			
		}
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		fragEmail.setLabelText("◊¢≤·” œ‰");
		fragEmail.setHintText("«Î ‰»Î◊¢≤·” œ‰");
	}
	
	public static interface OnGoNextListener{
		void onGoNext();
	}
	
	OnGoNextListener onGoNextListener;
		
		public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
			this.onGoNextListener = onGoNextListener;
	}
	void goNext(){
		if(onGoNextListener!=null){
			onGoNextListener.onGoNext();
		}
	}
	
	
}

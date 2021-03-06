package com.example.abc.fragments.pages;

import java.io.IOException;

import com.example.abc.ChangePasswordActivity;
import com.example.abc.R;
import com.example.abc.api.Server;
import com.example.abc.api.entity.User;
import com.example.abc.fragments.widgets.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyProfileFragment extends Fragment {
	View view;
	TextView textView;
	ProgressBar progressBar;
	AvatarView avatar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_my_profile, null);
			textView = (TextView) view.findViewById(R.id.text);
			progressBar = (ProgressBar) view.findViewById(R.id.progress);
			avatar = (AvatarView) view.findViewById(R.id.avatar);
		}
		
		view.findViewById(R.id.btn_changePassword).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(getActivity(), ChangePasswordActivity.class);
				startActivity(itnt);
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		textView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		OkHttpClient client = Server.getSharedClient();
		Request request = Server.requestBuilderWithApi("me")
				.method("get", null)
				.build();
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				final User user = new ObjectMapper().readValue(arg1.body().bytes(), User.class);
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						MyProfileFragment.this.onResponse(arg0,user);
					}
				});
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MyProfileFragment.this.onFailuer(arg0, arg1);
					}
				});
			}
		});
	}
	
	protected void onResponse(Call arg0, User user) {
		progressBar.setVisibility(View.GONE);
		textView.setVisibility(View.VISIBLE);
		textView.setTextColor(Color.BLACK);
		textView.setText("Hello,"+user.getName());
		avatar.load(user);
	}

	void onFailuer(Call call, Exception ex){
		progressBar.setVisibility(View.GONE);
		textView.setVisibility(View.VISIBLE);
		textView.setTextColor(Color.RED);
		textView.setText(ex.getMessage());
}
}

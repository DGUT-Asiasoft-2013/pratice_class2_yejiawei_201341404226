package com.example.abc;

import java.io.IOException;

import com.example.abc.api.Server;
import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNewArticleActivity extends Activity {
	SimpleTextInputCellFragment fragAddNewArticle = new SimpleTextInputCellFragment();
	Button btn_send;
	EditText input_main_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_news);
		input_main_text = (EditText) findViewById(R.id.input_main_text);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
//				finish();
				overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
			}
		});
		fragAddNewArticle = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_add);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fragAddNewArticle.setLabelText("标题");
		fragAddNewArticle.setHintText("请输入要标题");

	}

	void submit() {
		String title = fragAddNewArticle.getText();
		String text = input_main_text.getText().toString();
		OkHttpClient client = Server.getSharedClient();
		// new OkHttpClient();

		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().addFormDataPart("title", title)
				.addFormDataPart("text", text);

		Request request = Server.requestBuilderWithApi("article").method("post", null).post(requestBodyBuilder.build())
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
			}
		});

	}
}

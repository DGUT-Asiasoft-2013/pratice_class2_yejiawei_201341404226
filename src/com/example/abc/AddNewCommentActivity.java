package com.example.abc;

import java.io.IOException;

import com.example.abc.api.Server;
import com.example.abc.api.entity.Article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNewCommentActivity extends Activity{
	EditText text;
	String articleId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_comment);
		Article article = (Article) getIntent().getSerializableExtra("article");
		text = (EditText) findViewById(R.id.comment_text);
		articleId = article.getId();
		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	void submit() {
		
//		String text = input_main_text.getText().toString();
		OkHttpClient client = Server.getSharedClient();
		// new OkHttpClient();

		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.addFormDataPart("text", text.getText().toString());

		Request request = Server.requestBuilderWithApi("article/" +articleId + "/comments").method("post", null).post(requestBodyBuilder.build())
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							AddNewCommentActivity.this.onResponse(arg0, arg1.body().string());
						} catch (IOException e) {
							e.printStackTrace();
						}
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
	
	void onResponse(Call arg0, String responseBody) {
		new AlertDialog.Builder(this)
		.setTitle("发布成功")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		})
		.show();
	}
	
	void onFailure(Call arg0, Exception arg1) {
		
	}
}

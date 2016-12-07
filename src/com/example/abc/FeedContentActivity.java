package com.example.abc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FeedContentActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_content);
		String text;
		text = getIntent().getStringExtra("text");
		((TextView)findViewById(R.id.feed_text)).setText(text);
	}
}

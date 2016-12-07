package com.example.abc;

import com.example.abc.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;

public class AddNewsActivity extends Activity {
	SimpleTextInputCellFragment fragAddNews = new SimpleTextInputCellFragment();
	Button btn_send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_news);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				overridePendingTransition(0, R.anim.slide_in_bottom);
			}
		});
		fragAddNews = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_add);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragAddNews.setLabelText("添加内容");
		fragAddNews.setHintText("请输入要添加的内容");
		
	}
}

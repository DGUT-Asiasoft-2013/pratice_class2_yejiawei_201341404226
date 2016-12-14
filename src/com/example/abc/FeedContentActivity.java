package com.example.abc;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.Inflater;

import com.example.abc.api.Server;
import com.example.abc.api.entity.Article;
import com.example.abc.api.entity.Comment;
import com.example.abc.api.entity.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class FeedContentActivity extends Activity{
	
	ListView commentList;
	
	Article article;
	List<Comment> dataList;
	Button btn_likes; 
	TextView textLoadMore;
	View btnLoadMore;
	int page = 0;
	boolean isLiked = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_content);
		btn_likes = (Button) findViewById(R.id.btn_likes);
		LayoutInflater inflater = this.getLayoutInflater();
		btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
		textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
		commentList = (ListView) findViewById(R.id.comment_list);
		commentList.addFooterView(btnLoadMore);
		article = (Article) getIntent().getSerializableExtra("article");
//		final int articleId = (Integer) getIntent().getSerializableExtra("articleId");
		((TextView)findViewById(R.id.feed_text)).setText(article.getText());
		
		commentList.setAdapter(listAdapter);
		findViewById(R.id.btn_comment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent itnt = new Intent(FeedContentActivity.this, AddNewCommentActivity.class);
				itnt.putExtra("article", article);
				startActivity(itnt);
			}
		});
		
		btnLoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		
		btn_likes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleLike();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		reload();
	}
	
	void reload() {
		onReload();
		checkLike();
		reloadLike();
	}
	
	void loadMore() {
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("加载中...");
		
		Request request = Server.requestBuilderWithApi("article/" + article.getId() + "/comments/" + (page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
				try {
					final Page<Comment> comment= new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>(){});
					if(comment.getNumber() > page) {
						
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(dataList == null) {
									dataList = comment.getContent();
								} else {
									dataList.addAll(comment.getContent());
								}
								page = comment.getNumber();
								listAdapter.notifyDataSetChanged();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	void onReload() {
		Request request = Server
				.requestBuilderWithApi("article/" + article.getId() + "/comments")
				.get().build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final Page<Comment> data = new ObjectMapper()
						.readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {
				});
				runOnUiThread(new Runnable() {
					public void run() {
						FeedContentActivity.this.page = data.getNumber();
						FeedContentActivity.this.dataList = data.getContent();
						Log.d("dataSize", dataList.size() + "");
						listAdapter.notifyDataSetInvalidated();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				onFailture(arg0,arg1);
			}
		});
	}
	
	void onFailture(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
	BaseAdapter listAdapter = new BaseAdapter() {
		@SuppressLint("inflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView == null) {
				LayoutInflater inflate = LayoutInflater.from(parent.getContext());
				view = inflate.inflate(R.layout.my_comment_list_item, null);
			} else {
				view = convertView;
			}
			
			TextView commentText = (TextView) view.findViewById(R.id.comment_text);
			TextView commentAuthor = (TextView) view.findViewById(R.id.comment_author);
			TextView date = (TextView) view.findViewById(R.id.comment_date);
			Comment comment = dataList.get(position);
			commentText.setText(comment.getText());
			commentAuthor.setText(comment.getAuthorName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			date.setText(dateFormat.format(article.getCreateDate()));
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}
		
		@Override
		public int getCount() {
			return dataList == null ? 0 : dataList.size();
		}
	};
	
	void reloadLike() {
		Request request = Server
				.requestBuilderWithApi("article/" + article.getId() + "/likes")
				.get().build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final Integer count = new ObjectMapper().readValue(arg1.body().string(), Integer.class);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						onReloadLike(count);
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						onReloadLike(0);
					}
				});
			}
		});
	}
	
	void checkLike() {
		Request request = Server
				.requestBuilderWithApi("article/" + article.getId() + "/isliked")
				.get().build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				isLiked = new ObjectMapper().readValue(arg1.body().string(), Boolean.class);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
			}
		});
	}
	
	void onReloadLike(int count) {
		if(count > 0) {
			btn_likes.setText("like(" + count + ")");
		} else {
			btn_likes.setText("like");
		}
	}
	
	void toggleLike() {
		
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("likes", String.valueOf(!isLiked))
				.build();
		Log.d("isLiked", String.valueOf(!isLiked));
		Request request = Server
				.requestBuilderWithApi("article/" + article.getId() + "/likes")
				.method("post", null)
				.post(body)
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final Integer count = new ObjectMapper().readValue(arg1.body().string(), Integer.class);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						reload();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						reload();
					}
				});
			}
		});
	}
}

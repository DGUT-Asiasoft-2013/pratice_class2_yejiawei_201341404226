package com.example.abc.fragments.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import com.example.abc.FeedContentActivity;
import com.example.abc.LoginActivity;
import com.example.abc.R;
import com.example.abc.api.Server;
import com.example.abc.api.entity.Article;
import com.example.abc.api.entity.Page;
import com.example.abc.api.entity.User;
import com.example.abc.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedsListFragment extends Fragment {
	View view;
	View btnLoadMore;
	TextView textLoadMore;
	ListView listView;
	List<Article> dataList;
	int page = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null) {
			view = inflater.inflate(R.layout.fragment_page_feed_list, null);
			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
			
			
			listView = (ListView) view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Log.d("ItemSelected", position + "");
					onItemClicked(position);
				}
			});
			
			btnLoadMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadMore();
				}
			});
	
		}

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reload();
		
	}
	
	void reload() {
		Request request = Server.requestBuilderWithApi("feeds")
				.get()
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Article> data = new ObjectMapper()
							.readValue(arg1.body().string(), new TypeReference<Page<Article>>() {
							});
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedsListFragment.this.page = data.getNumber();
							FeedsListFragment.this.dataList = data.getContent();
							listAdapter.notifyDataSetInvalidated();
						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getLocalizedMessage())
							.show();
						}
					});
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	void loadMore() {
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("加载中...");
		
		Request request = Server.requestBuilderWithApi("feeds/" + page+1).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
				try {
					final Page<Article> feeds = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Article>>(){});
					if(feeds.getNumber() > page) {
						
						
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(dataList == null) {
									dataList = feeds.getContent();
								} else {
									dataList.addAll(feeds.getContent());
								}
								page = feeds.getNumber();
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
	
	BaseAdapter listAdapter = new BaseAdapter() {
		
		@SuppressLint("inflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			if(convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.my_list_item, null);
				
			} else {
				view = convertView;
			}
			TextView text1 = (TextView) view.findViewById(R.id.tv_text);
			TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
//			text1.setText(data[position]);
			Article article = dataList.get(position);
			text1.setText(article.getTitle() + ";" + article.getAuthorName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			tv_date.setText(dateFormat.format(article.getCreateDate()));
			avatar.load(Server.serverAdress + article.getAuthorAvatar());
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return position;
		}
		
		@Override
		public int getCount() {
			return dataList == null ? 0 : dataList.size();
			
		}
	};
	
	void onItemClicked (int position) {
		String text = dataList.get(position).getText();
		Intent itnt = new Intent(getActivity(),FeedContentActivity.class);
		itnt.putExtra("text", text);
		
		startActivity(itnt);
	}
	
}

package com.example.abc.fragments.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.abc.FeedContentActivity;
import com.example.abc.R;
import com.example.abc.api.Server;
import com.example.abc.api.entity.Article;
import com.example.abc.api.entity.Page;
import com.example.abc.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SearchPageFragment extends Fragment {
	View view;
	View btnLoadMore;
	TextView textLoadMore;
	ListView feedList;
	String keyword;
	
	List<Article> dataList;
	int page = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_search_page, null);
			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
			feedList = (ListView) view.findViewById(R.id.feed_list);
			feedList.addFooterView(btnLoadMore);
			feedList.setAdapter(listAdapter);
			
			feedList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});
			final EditText editKeyword = (EditText) view.findViewById(R.id.keyword);
			view.findViewById(R.id.btn_search).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					keyword = editKeyword.getText().toString();
					onSearch();
				}
			});;
			
			btnLoadMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadMore();
				}
			});
		}
		return view;
	}
	
	void onItemClicked(int position) {
		Intent itnt = new Intent(getActivity(), FeedContentActivity.class);
		Article article = dataList.get(position);
		itnt.putExtra("article", article);
		
		startActivity(itnt);
	}
	
	void onSearch() {
		Request request = Server.requestBuilderWithApi("/article/s/" + keyword)
				.get()
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final Page<Article> data = new ObjectMapper()
						.readValue(arg1.body().string(), new TypeReference<Page<Article>>() {
				});
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						SearchPageFragment.this.page = data.getNumber();
						SearchPageFragment.this.dataList = data.getContent();
						listAdapter.notifyDataSetInvalidated();
					}
				});
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
			return dataList.get(position);
		}
		
		@Override
		public int getCount() {
			return dataList == null ? 0 : dataList.size();
		}
	};
	
	void loadMore() {
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("加载中...");
		
		Request request = Server
				.requestBuilderWithApi("article/s/" + keyword + "/?page=" + (page+1))
				.get()
				.build();
		
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
				final Page<Article> data = new ObjectMapper()
						.readValue(arg1.body().string(), new TypeReference<Page<Article>>() {
				});
				if(data.getNumber() > 0) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if(dataList == null) {
										dataList = data.getContent();
									} else {
										dataList.addAll(data.getContent());
									}
								}
							});
							page = data.getNumber();
							listAdapter.notifyDataSetChanged();
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
}

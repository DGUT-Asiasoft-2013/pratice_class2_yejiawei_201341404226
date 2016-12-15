package com.example.abc.fragments.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.abc.R;
import com.example.abc.api.Server;
import com.example.abc.api.entity.Comment;
import com.example.abc.api.entity.Page;
import com.example.abc.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class NotesListFragment extends Fragment {
	View view;
	ListView commentList;
	View btnLoadMore;
	TextView textLoadMore;
	int page = 0;
	List<Comment> dataList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_page_note_list, null);
			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
			commentList = (ListView) view.findViewById(R.id.comment_list);
			commentList.addFooterView(btnLoadMore);
			commentList.setAdapter(listAdapter);
		}
		
		btnLoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
		return view;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		onReload();
	}
	
	void onReload() {
		Request request = Server.requestBuilderWithApi("comments")
				.get()
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final Page<Comment> data = new ObjectMapper()
						.readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {
				});
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						NotesListFragment.this.page = data.getNumber();
						NotesListFragment.this.dataList = data.getContent();
						listAdapter.notifyDataSetInvalidated();
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				
			}
		});
	}
	
	
	void loadMore() {
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("加载中...");
		
		Request request = Server.requestBuilderWithApi("comments?page=" + (page + 1))
				.get()
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
				final Page<Comment> data = new ObjectMapper().readValue(arg1.body()
						.string(), new TypeReference<Page<Comment>>() {
				});
				if(data.getNumber() > page) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(dataList == null) {
								dataList = data.getContent();
							} else {
								dataList.addAll(data.getContent());
							}
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
	BaseAdapter listAdapter = new BaseAdapter() {
		@SuppressLint("inflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.my_comment_list_item2, null);
			} else {
				view = convertView;
			}
			AvatarView authorAvatar = (AvatarView) view.findViewById(R.id.avatar);
			TextView aboutArticle = (TextView) view.findViewById(R.id.about);
			TextView commentText = (TextView) view.findViewById(R.id.text);
			TextView commentAuthor = (TextView) view.findViewById(R.id.author);
			TextView date = (TextView) view.findViewById(R.id.date);
			Comment comment = dataList.get(position);
			aboutArticle.setText("关于" + comment.getArticleTitle() + "的评论");
			commentText.setText(comment.getText());
			commentAuthor.setText("作者: " + comment.getAuthorName());
			authorAvatar.load(Server.serverAdress + comment.getAuthorAvatar());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			date.setText("日期: " + dateFormat.format(comment.getCreateDate()));
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return dataList == null ? null : dataList.get(position);
		}
		
		@Override
		public int getCount() {
			return dataList == null ? 0 : dataList.size();
		}
	};
}

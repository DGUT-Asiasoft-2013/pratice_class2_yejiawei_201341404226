package com.example.abc.fragments.pages;

import com.example.abc.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FeedsListFragment extends Fragment {
	View view;
	ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null) {
			view = inflater.inflate(R.layout.fragment_page_feed_list, null);
		
			listView = (ListView) view.findViewById(R.id.list);
			listView.setAdapter(listAdapter);
		}
		return view;
	}
	
	BaseAdapter listAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			if(convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(android.R.layout.simple_list_item_1, null);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				text1.setText("ROW" + position);
			} else {
				view = convertView;
			}
			
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return "";
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 20;
		}
	};
}
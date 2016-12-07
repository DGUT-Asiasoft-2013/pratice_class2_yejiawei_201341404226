package com.example.abc;

import com.example.abc.fragments.MainTabbarFragment;
import com.example.abc.fragments.MainTabbarFragment.OnTabSelectedListener;
import com.example.abc.fragments.pages.FeedsListFragment;
import com.example.abc.fragments.pages.MyProfileFragment;
import com.example.abc.fragments.pages.NotesListFragment;
import com.example.abc.fragments.pages.SearchPageFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class HelloWorldActivity extends Activity {
	FeedsListFragment contentFeedlist = new FeedsListFragment();
	MyProfileFragment contentMyProfile = new MyProfileFragment();
	NotesListFragment contentNoteList = new NotesListFragment();
	SearchPageFragment contentSearchPage = new SearchPageFragment();

	MainTabbarFragment tabbar;
//	int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helloworld);

		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				contentChange(index);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		tabbar.setSelectedItem(0);
	}

	void contentChange(int index) {
		Fragment newFrag = new Fragment();
		switch (index) {
		case 0:
			newFrag = contentFeedlist;
			break;
		case 1:
			newFrag = contentNoteList;
			break;
		case 2:
			newFrag = contentSearchPage;
			break;
		case 3:
			newFrag = contentMyProfile;
			break;
		default:
			break;
		}
		
		getFragmentManager().beginTransaction().replace(R.id.contain, newFrag).commit();
	}
}

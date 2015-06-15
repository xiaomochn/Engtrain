package com.tt.engtrain;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.tt.engtrain.showcontent.ContentListItemActivity;
import com.yalantis.phoenix.PullToRefreshView;
import com.yalantis.phoenix.PullToRefreshView.OnRefreshListener;

public class MainActivity extends Activity {
	private HashMap<String, ArrayList<FileListMode>> mFileMaps;
	private PullToRefreshView pull;
	private FloatingGroupExpandableListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFileMaps = new HashMap<String, ArrayList<FileListMode>>();
		for (int i = 0; i < 5; i++) {
			ArrayList<FileListMode> temp = new ArrayList<FileListMode>();
			for (int j = 0; j < 5; j++) {
				temp.add(new FileListMode("Ó¢Óï", "11111231", "2015-6-5", "2015-6-5", 3, true, 10, "meiy"));
			}
			mFileMaps.put("µÚ" + i + "µ¥Ôª", temp);
		}
		pull = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
		list = (FloatingGroupExpandableListView) findViewById(R.id.sample_activity_list);
		pull.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				// downloadFilelist();
				pull.setRefreshing(false);
			}
		});

		list.setAdapter(new WrapperExpandableListAdapter(new FileServiceAdapter(this, mFileMaps)));
		// list.setOnChildClickListener(new OnChildClickListener() {
		//
		// @Override
		// public boolean onChildClick(ExpandableListView arg0, View arg1, int
		// arg2, int arg3, long arg4) {
		// // TODO Auto-generated method stub
		// startActivity(new Intent(MainActivity.this,
		// ContentListItemActivity.class));
		// return false;
		// }
		// });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			rootView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(PlaceholderFragment.this.getActivity(), ContentListItemActivity.class));
				}
			});
			return rootView;
		}
	}

}

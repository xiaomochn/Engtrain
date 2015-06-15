package com.tt.engtrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tt.engtrain.showcontent.ContentListItemActivity;

public class FileServiceAdapter extends BaseExpandableListAdapter {
	private final Context mContext;
	private final LayoutInflater mLayoutInflater;
	private HashMap<String, ArrayList<FileListMode>> mFileMaps;

	// private final String[][] mChilds = { { "1.5" }, { "1.6" }, { "2.0",
	// "2.0.1", "2.1" }, { "2.2", "2.2.1", "2.2.2", "2.2.3" }, { "2.3", "2.3.1",
	// "2.3.2", "2.3.3", "2.3.4", "2.3.5", "2.3.6", "2.3.7" },
	// { "3.0", "3.1", "3.2", "3.2.1", "3.2.2", "3.2.3", "3.2.4", "3.2.5",
	// "3.2.6" }, { "4.0", "4.0.1", "4.0.2", "4.0.3", "4.0.4" }, { "4.1",
	// "4.1.1", "4.1.2", "4.2", "4.2.1", "4.2.2", "4.3", "4.3.1" }, { "4.4" } };

	/**
	 * @param context
	 */
	public FileServiceAdapter(Context context, HashMap<String, ArrayList<FileListMode>> fileMaps) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFileMaps = fileMaps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return mFileMaps.size();
	}

	public static <T> ArrayList<T> copyIterator(Iterator<T> iter) {
		ArrayList<T> copy = new ArrayList<T>();
		while (iter.hasNext())
			copy.add(iter.next());
		return copy;
	}

	@Override
	public ArrayList<FileListMode> getGroup(int groupPosition) {
		Iterator iterator = mFileMaps.keySet().iterator();
		ArrayList<String> iArrayList = copyIterator(iterator);
		Collections.sort(iArrayList, Collections.reverseOrder());
		try {
			return mFileMaps.get(iArrayList.get(groupPosition));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.fileservice_activity_list_group_item, parent, false);
		}
		final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_group_item_text);
		text.setText(getGroup(groupPosition).get(0).getDaytime());
		final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_expanded_image);
		final int resId = isExpanded ? R.drawable.minus : R.drawable.plus;
		expandedImage.setImageResource(resId);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<FileListMode>) getGroup(groupPosition)).size();
	}

	@Override
	public FileListMode getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.fileservice_activity_list_child_item, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(R.id.action_bar_title, viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag(R.id.action_bar_title);
		}
		final FileListMode mode = getChild(groupPosition, childPosition);
		viewHolder.setDate(mode);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mContext.startActivity(new Intent(mContext, ContentListItemActivity.class));
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView download;
		TextView title;
		View download_layout;
		View parent;
		FileListMode mode;
		TextView speedtv;
		Button sendButton;

		public ViewHolder(View view) {
			parent = view;
			download = (TextView) view.findViewById(R.id.download_text);
			title = (TextView) view.findViewById(R.id.title);
			download_layout = view.findViewById(R.id.download_layout);
			speedtv = (TextView) view.findViewById(R.id.speedtv);
			sendButton = (Button) view.findViewById(R.id.sendbtn);
		}

		public void setDate(final FileListMode mode) {
			this.mode = mode;
			title.setText(mode.getName());
			sendButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mContext.startActivity(new Intent(mContext, ContentListItemActivity.class));
				}
			});
		}

		public FileListMode getMode() {
			return mode;
		}

		public void setMode(FileListMode mode) {
			this.mode = mode;
		}

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}

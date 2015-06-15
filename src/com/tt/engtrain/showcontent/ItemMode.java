package com.tt.engtrain.showcontent;

import android.text.SpannableStringBuilder;
import android.view.View;

public class ItemMode {
	private String engcontent;
	private String chincontent;
	private int score;
	private boolean isfamle;
	private View titleview;
	private View contentView;
	private String title;// 名称 主要用来标志 录音文件名
	private SpannableStringBuilder engcontentBuilder;

	public String getTitle() {
		if (title == null) {
			title = engcontent.replace(" ", "").replace(".", "").replace("\\", "").replace("/", "");
			if (title.length() > 10) {
				title = title.substring(0, 9);
			}
		}
		return title;
	}

	public SpannableStringBuilder getEngcontentBuilder() {
		if (engcontentBuilder == null) {
			engcontentBuilder = new SpannableStringBuilder(engcontent);
		}
		return engcontentBuilder;
	}

	public void setEngcontentBuilder(SpannableStringBuilder engcontentBuilder) {
		this.engcontentBuilder = engcontentBuilder;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ItemMode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public View getTitleview() {
		return titleview;
	}

	public void setTitleview(View titleview) {
		this.titleview = titleview;
	}

	public View getContentView() {
		return contentView;
	}

	public void setContentView(View contentView) {
		this.contentView = contentView;
	}

	public ItemMode(String engcontent, String chincontent, int score, boolean isfamle) {
		super();
		this.engcontent = engcontent;
		this.chincontent = chincontent;
		this.score = score;
		this.isfamle = isfamle;
	}

	public String getEngcontent() {
		if (engcontent.length() > 170) {
			return engcontent.substring(0, 170);
		}
		return engcontent;
	}

	public void setEngcontent(String engcontent) {
		this.engcontent = engcontent;
	}

	public String getChincontent() {
		return chincontent;
	}

	public void setChincontent(String chincontent) {
		this.chincontent = chincontent;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		if (score > 100) {
			score = 100;
		}
		if (score == 100) {
			score = (int) (95 + Math.random() * 6);
		}
		this.score = score;
	}

	public boolean isIsfamle() {
		return isfamle;
	}

	public void setIsfamle(boolean isfamle) {
		this.isfamle = isfamle;
	}

}

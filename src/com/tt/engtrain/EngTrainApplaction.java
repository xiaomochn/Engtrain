package com.tt.engtrain;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;

public class EngTrainApplaction extends Application {
	@Override
	public void onCreate() {
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
		FlowerCollector.setDebugMode(false);
		super.onCreate();
	}
}

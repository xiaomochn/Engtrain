/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tt.engtrain.showcontent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.ise.result.Result;
import com.iflytek.ise.result.entity.Sentence;
import com.iflytek.ise.result.entity.Word;
import com.iflytek.ise.result.util.ResultTranslateUtil;
import com.iflytek.speech.util.XmlResultParser;
import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;
import com.tt.engtrain.R;

public class ContentListItemActivity extends FragmentActivity {
	private ListView mListView;
	private ArrayList<ItemMode> mModelist;
	private MyExpandableListItemAdapter mExpandableListItemAdapter;

	private static String TAG = "TtsDemo";
	private String pcmpath = Environment.getExternalStorageDirectory() + "/engtrain/";
	private static int selectedNumCloud = 0;
	private static int selectedNumLocal = 0;
	// 语音听写对象
	// private SpeechRecognizer mIat;
	// 语音听写UI
	// private RecognizerDialog iatDialog;
	// 听写结果内容
	private EditText mResultText;
	int ret = 0;// 函数调用返回值

	// 语音合成对象
	private SpeechSynthesizer mTts;
	private AudioTrack audioTrack;
	// 默认云端发音人
	public static String voicerCloud = "catherine";
	// 默认本地发音人
	public static String voicerLocal = "xiaoyan";
	// 凯瑟琳
	// 青年女声
	// 英文
	// catherine
	// ￼￼￼￼￼￼￼￼
	// ￼￼￼￼
	// 亨利
	// 青年男声
	// 英文
	// henry
	// ￼￼￼￼￼￼￼￼
	// ￼￼￼￼
	// 玛丽
	// 青年女声
	// 英文
	// vimary
	// 云端发音人列表

	// // 缓冲进度
	// private int mPercentForBuffering = 0;
	// // 播放进度
	// private int mPercentForPlaying = 0;

	private Toast mToast;
	private SpeechEvaluator mSpeechEvaluator;// 语音评测

	// private SharedPreferences mSharedPreferences;
	public static void actionToExpandableList(Context context) {
		Intent intent = new Intent(context, ContentListItemActivity.class);
		context.startActivity(intent);
	}

	public static ArrayList<Integer> getItems() {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < 1000; i++) {
			items.add(i);
		}
		return items;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baselist);
		mSpeechEvaluator = SpeechEvaluator.createEvaluator(ContentListItemActivity.this, null);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		int iMinBufSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT, iMinBufSize, AudioTrack.MODE_STREAM);
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		initData();
		initListView();
		// initspker();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mModelist = new ArrayList<ItemMode>();
		mModelist.add(new ItemMode("One is always on a strange road, watching strange scenery and listening to strange music. Then one day, you will find that the things you try hard to forget are already gone. ",
				"一个人总要走陌生的路，看陌生的风景，听陌生的歌，然后在某个不经意的瞬间，你会发现，原本是费尽心机想要忘记的事情真的就那么忘记了。", 0, true));
		mModelist.add(new ItemMode(
				"Happiness is not about being immortal nor having food or rights in one's hand. It’s about having each tiny wish come true, or having something to eat when you are hungry or having someone's love when you need love. ",
				"幸福，不是长生不老，不是大鱼大肉，不是权倾朝野。幸福是每一个微小的生活愿望达成。当你想吃的时候有得吃，想被爱的时候有人来爱你。 ", 0, false));
		mModelist
				.add(new ItemMode("Love is a lamp, while friendship is the shadow. When the lamp is off,you will find the shadow everywhere. Friend is who can give you strength at last. 　", "爱情是灯，友情是影子，当灯灭了，你会发现你的周围都是影子。朋友，是在最后可以给你力量的人。　 ", 0, true));
		mModelist.add(new ItemMode("I love you not for who you are, but for who I am before you. ", "我爱你不是因为你是谁，而是我在你面前可以是谁。", 20, false));
		mModelist.add(new ItemMode("Love makes man grow up or sink down. 　 ", "爱情，要么让人成熟，要么让人堕落	", 30, true));
		mModelist
				.add(new ItemMode(
						".If you can hold something up and put it down, it is called weight-lifting; if you can hold something up but can never put it down,it's called burden-bearing. Pitifully, most of people are bearing heavy burdens when they are in love.",
						"举得起放得下的叫举重，举得起放不下的叫负重。可惜，大多数人的爱情，都是负重的", 100, true));
		mModelist.add(new ItemMode("A fall into the pit, a gain in your wit.", "吃一堑，长一智。", 0, false));
		mModelist.add(new ItemMode("A fair face may hide a foul heart.", "人不可貌相。", 0, true));
		mModelist.add(new ItemMode("Stay put", "停住不动", 0, false));
		mModelist.add(new ItemMode("get the drift", "明白，了解", 60, true));
		mModelist.add(new ItemMode("pistachio", "开心果", 0, false));
		mModelist.add(new ItemMode(" I don’t think much of the movie ", " 那电影不怎么样", 89, true));
	}

	private void initListView() {
		mListView = (ListView) findViewById(R.id.activity_baselist_listview);
		mListView.setDivider(null);
		mExpandableListItemAdapter = new MyExpandableListItemAdapter(this, mModelist);
		mExpandableListItemAdapter.setLimit(1);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mExpandableListItemAdapter);
		swingBottomInAnimationAdapter.setAbsListView(mListView);
		swingBottomInAnimationAdapter.setInitialDelayMillis(500);
		mListView.setAdapter(swingBottomInAnimationAdapter);
	}

	// private void initspker() {
	// mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
	// // 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
	// iatDialog = new RecognizerDialog(this, mTtsInitListener);
	// //
	// // mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
	// // Activity.MODE_PRIVATE);
	// // mResultText = ((EditText) findViewById(R.id.iat_text));
	// // ### 发音部分
	// mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
	//
	// }

	// 开始评测
	public void startListening(ItemMode mode) {

		if (mSpeechEvaluator == null) {
			return;
		}

		setIseParams(mode);
		mSpeechEvaluator.startEvaluating(mode.getEngcontent(), null, new CustomEvaluatorListener(mode));
		// ######## 换用讯飞的 接口评测
	}

	// 开始合成
	public void startTts(String text, boolean ismale, com.tt.engtrain.showcontent.ContentListItemActivity.MyExpandableListItemAdapter.ContentViewHolder viewHolder) {

		text += "";
		// 设置参数
		setTtsParam(ismale);
		int code = mTts.startSpeaking(text, new CustomSynthesizerListener(viewHolder));
		if (code != ErrorCode.SUCCESS) {
			showTip("语音合成失败,错误码: " + code);
		}
	}

	private void playSelfRecorde(final String path) {// 播放pcm

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED)) {
					audioTrack.stop();
					return;
				}
				audioTrack.play();

				byte[] data = getBytes(path);
				audioTrack.write(data, 0, data.length);
				audioTrack.stop();
			}
		}).start();

	}

	public static byte[] getBytes(String filePath) {// file to bytes[]
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	// adabper 内容比较多 瞎写呗
	private class MyExpandableListItemAdapter extends ExpandableListItemAdapter<Integer> {

		private final Context mContext;
		private LayoutInflater inflater;
		private ArrayList<ItemMode> mModelist;

		/**
		 * Creates a new ExpandableListItemAdapter with the specified list, or
		 * an empty list if items == null.
		 */
		private MyExpandableListItemAdapter(final Context context, List mModelist) {
			super(context, R.layout.item_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, mModelist);
			mContext = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mModelist = (ArrayList<ItemMode>) mModelist;
		}

		@Override
		public View getTitleView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_expandablelistitem_title, parent, false);
				convertView.setTag(new TitleViewHolder(convertView));
			}
			TitleViewHolder viewHolder = (TitleViewHolder) convertView.getTag();
			viewHolder.setData(mModelist.get(position), position);
			return convertView;
		}

		@Override
		public View getContentView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_expandablelistitem_content, parent, false);
				convertView.setTag(new ContentViewHolder(convertView));
			}
			ContentViewHolder viewHolder = (ContentViewHolder) convertView.getTag();
			viewHolder.setData(mModelist.get(position), position);
			return convertView;
		}

		/**
		 * @author qiao 谁都知道这是啥
		 * 
		 */
		class TitleViewHolder {
			View parent;
			TextView engcontentTv;
			TextView chincontentTv;
			TextView scoreTv;
			ImageView headImageView;
			ItemMode mMode;
			int mPostion;

			TitleViewHolder(View view) {
				parent = view;
				engcontentTv = (TextView) parent.findViewById(R.id.engtitle_tv);
				chincontentTv = (TextView) parent.findViewById(R.id.chinestitle_tv);
				headImageView = (ImageView) parent.findViewById(R.id.imageview_head);
				scoreTv = (TextView) parent.findViewById(R.id.score_tv);
			}

			private void setData(ItemMode mode, int postion) {
				this.mMode = mode;
				mPostion = postion;
				mode.setTitleview(parent);
				engcontentTv.setText(mode.getEngcontentBuilder());

				chincontentTv.setText(mode.getChincontent());
				if (mode.getScore() == 100) {
					scoreTv.setBackgroundResource(R.drawable.circle_10_red);
				} else if (mode.getScore() >= 60.0) {
					scoreTv.setBackgroundResource(R.drawable.circle_10_green);
				} else {
					scoreTv.setBackgroundResource(R.drawable.circle_10_gray);
				}
				scoreTv.setText(mode.getScore() + "");
				if (mode.isIsfamle()) {
					headImageView.setImageResource(R.drawable.fmale);
					chincontentTv.setTextColor(Color.rgb(251, 21, 126));
				} else {
					headImageView.setImageResource(R.drawable.male);
					chincontentTv.setTextColor(Color.rgb(42, 168, 240));
				}
			}
		};

		class ContentViewHolder {// 播放按钮== 播放什么的都在这
			View parent;
			HoloCircularProgressBar recodepBar;
			// TextView playRecoderButton;
			Button playSelfRecoderButton;
			ItemMode mMode;
			int mPostion;
			int speekstate = 0; // 0 未播放 1 正在播放 2 暂停
			HoloCircularProgressBar progressBar;

			ContentViewHolder(View view) {
				parent = view;
				recodepBar = (HoloCircularProgressBar) view.findViewById(R.id.lessen_pgb);
				recodepBar.setThumbEnabled(false);
				playSelfRecoderButton = (Button) view.findViewById(R.id.recoderself_btn);
				progressBar = (HoloCircularProgressBar) view.findViewById(R.id.recoder_progress);

				progressBar.setProgress(0);

			}

			private void setData(ItemMode mode, int postion) {
				this.mMode = mode;
				mPostion = postion;
				mode.setContentView(parent);
				recodepBar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (mTts.isSpeaking()) {
							progressBar.setBackgroundResource(R.drawable.pause);
							mTts.resumeSpeaking();
							speekstate = 1;
						}
						startListening(mMode);
					}
				});
				progressBar.setBackgroundResource(R.drawable.play);
				progressBar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!mTts.isSpeaking() || speekstate == 0) {
							progressBar.setBackgroundResource(R.drawable.pause);
							startTts(mMode.getEngcontent(), !mMode.isIsfamle(), ContentViewHolder.this);
							speekstate = 1;
						} else if (speekstate == 1) {

							progressBar.setBackgroundResource(R.drawable.play);
							mTts.pauseSpeaking();
							speekstate = 2;
						} else if (speekstate == 2) {

							progressBar.setBackgroundResource(R.drawable.pause);

							mTts.resumeSpeaking();
							speekstate = 1;
						}
					}
				});
				playSelfRecoderButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (new File(pcmpath + "recode" + mMode.getTitle()).exists()) {
							playSelfRecorde(pcmpath + "recode" + mMode.getTitle());
						} else {
							showTip("还没训练过");
						}

					}
				});
			}
		}

	}

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码：" + code);
			}
		}
	};

	private void showTip(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str + "");
				mToast.show();
			}
		});
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setTtsParam(boolean ismale) {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 设置合成
		// if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
		// 设置使用云端引擎
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置发音人
		if (ismale) {
			mTts.setParameter(SpeechConstant.VOICE_NAME, "henry");
		} else {
			mTts.setParameter(SpeechConstant.VOICE_NAME, "vimary");
		}

		mTts.setParameter(SpeechConstant.SPEED, "50");

		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, "50");

		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, "90");

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
	}

	/**
	 * @param mode
	 *            评测参数
	 */
	private void setIseParams(ItemMode mode) {

		mSpeechEvaluator.setParameter(SpeechConstant.LANGUAGE, "en_us");
		mSpeechEvaluator.setParameter(SpeechConstant.ISE_CATEGORY, "read_sentence");
		mSpeechEvaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
		mSpeechEvaluator.setParameter(SpeechConstant.VAD_BOS, "5000");
		mSpeechEvaluator.setParameter(SpeechConstant.VAD_EOS, "1800");
		mSpeechEvaluator.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
		mSpeechEvaluator.setParameter(SpeechConstant.RESULT_LEVEL, "complete");
		// 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		mSpeechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, pcmpath + "recode" + mode.getTitle());
	}

	class CustomSynthesizerListener implements SynthesizerListener {

		com.tt.engtrain.showcontent.ContentListItemActivity.MyExpandableListItemAdapter.ContentViewHolder mViewHolder;

		public CustomSynthesizerListener(com.tt.engtrain.showcontent.ContentListItemActivity.MyExpandableListItemAdapter.ContentViewHolder viewHolder) {
			mViewHolder = viewHolder;
		}

		@Override
		public void onSpeakBegin() {
			// mViewHolder.playRecoderButton.setText("暂停");
			mViewHolder.progressBar.setBackgroundResource(R.drawable.pause);
			showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			// mViewHolder.playRecoderButton.setText("继续");\

			mViewHolder.progressBar.setBackgroundResource(R.drawable.play);
			showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			mViewHolder.progressBar.setBackgroundResource(R.drawable.pause);
			// mViewHolder.playRecoderButton.setText("暂停");
			showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
			// mPercentForBuffering = percent;
			// mToast.setText(String.format(getString(R.string.tts_toast_format),
			// mPercentForBuffering, mPercentForPlaying));
			// mViewHolder.progressBar.setProgress(mPercentForPlaying);
			// mToast.show();
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// mPercentForPlaying = percent;
			// showTip(String.format(getString(R.string.tts_toast_format),
			// mPercentForBuffering, mPercentForPlaying));
			if (mProgressBarAnimator != null) {
				mProgressBarAnimator.cancel();
			}
			animate(mViewHolder.progressBar, null, percent / 100.0f, 50);
			// mViewHolder.progressBar.setProgress(percent / 100.0f);
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				mViewHolder.progressBar.setBackgroundResource(R.drawable.play);
				// mViewHolder.playRecoderButton.setText("播放");
				// mViewHolder.progressBar.setProgress(0);
				if (mProgressBarAnimator != null) {
					mProgressBarAnimator.cancel();
				}
				animate(mViewHolder.progressBar, null, 0, 1000);
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}

	}

	// ##########评测部分

	class CustomEvaluatorListener implements EvaluatorListener {

		HoloCircularProgressBar progressBar;
		ItemMode mMode;
		long time = 0;
		int beforvolume;
		TextView scoretv;

		CustomEvaluatorListener(ItemMode mode) {
			mMode = mode;
			progressBar = (HoloCircularProgressBar) mMode.getContentView().findViewById(R.id.lessen_pgb);
			scoretv = (TextView) mMode.getTitleview().findViewById(R.id.score_tv);

		}

		@Override
		public void onResult(EvaluatorResult result, boolean isLast) {
			Log.d(TAG, "evaluator result :" + isLast);

			if (isLast) {
				StringBuilder builder = new StringBuilder();
				builder.append(result.getResultString());

				String lastResult = builder.toString();

				// 解析最终结果
				if (!TextUtils.isEmpty(lastResult)) {
					XmlResultParser resultParser = new XmlResultParser();
					Result result1 = resultParser.parse(lastResult);

					if (null != result1) {

						if (null == result1.sentences) {
							return;
						}

						for (Sentence sentence : result1.sentences) {
							if ("噪音".equals(ResultTranslateUtil.getContent(sentence.content)) || "静音".equals(ResultTranslateUtil.getContent(sentence.content))) {
								continue;
							}

							if (null == sentence.words) {
								continue;
							}
							StringBuffer buffer = new StringBuffer();
							for (Word word : sentence.words) {
								if ("噪音".equals(ResultTranslateUtil.getContent(word.content)) || "静音".equals(ResultTranslateUtil.getContent(word.content))) {
									continue;
								}

								buffer.append("\n单词[" + ResultTranslateUtil.getContent(word.content) + "] ").append("朗读：" + ResultTranslateUtil.getDpMessageInfo(word.dp_message)).append(" 得分：" + word.total_score);
								ResultTranslateUtil.getContent(word.content);
								float f = word.total_score;
								// if (null == word.sylls) {
								// buffer.append("\n");
								// continue;
								// }

								// for (Syll syll : word.sylls) {
								// buffer.append("\n└音节[" +
								// ResultTranslateUtil.getContent(syll.getStdSymbol())
								// + "] ");
								// if (null == syll.phones) {
								// continue;
								// }
								//
								// for (Phone phone : syll.phones) {
								// buffer.append("\n\t└音素[" +
								// ResultTranslateUtil.getContent(phone.getStdSymbol())
								// + "] ").append(" 朗读：" +
								// ResultTranslateUtil.getDpMessageInfo(phone.dp_message));
								// }
								//
								// }
								// buffer.append("\n");
							}
						}
						mMode.setScore((int) (result1.total_score * 20));
						scoretv.setText(mMode.getScore() + "");
					} else {
						showTip("结析结果为空");
					}
				}

				showTip("评测结束");
			}
			progressBar.setProgress(0);
			progressBar.setThumbEnabled(false);

		}

		@Override
		public void onError(SpeechError error) {
			// mIseStartButton.setEnabled(true);
			// if (error != null) {
			// showTip("error:" + error.getErrorCode() + "," +
			// error.getErrorDescription());
			// mResultEditText.setText("");
			// mResultEditText.setHint("请点击“开始评测”按钮");
			// } else {
			// Log.d(TAG, "evaluator over");
			// }
			progressBar.setThumbEnabled(false);
			showTip("评测失败");
		}

		@Override
		public void onBeginOfSpeech() {
			Log.d(TAG, "evaluator begin");
			showTip("evaluator begin");
			progressBar.setThumbEnabled(true);
		}

		@Override
		public void onEndOfSpeech() {
			Log.d(TAG, "evaluator stoped");
			showTip("evaluator stoped");
			progressBar.setThumbEnabled(false);
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前音量：" + volume);
			progressBar.setThumbEnabled(true);
			if (Calendar.getInstance().getTimeInMillis() - time > 500) {
				if (mProgressBarAnimator != null) {
					mProgressBarAnimator.cancel();

				}
				animate(progressBar, null, volume * 3f / 100.0f, 500);
				time = Calendar.getInstance().getTimeInMillis();
			} else if (Math.abs(beforvolume - volume) > 5) {
				beforvolume = volume;
				if (mProgressBarAnimator != null) {
					mProgressBarAnimator.cancel();

				}
				animate(progressBar, null, volume * 3f / 100.0f, 500);
				time = Calendar.getInstance().getTimeInMillis();
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// TODO Auto-generated method stub
		}

	}

	// ############# 进度条 动画

	private ObjectAnimator mProgressBarAnimator;

	/**
	 * Animate.
	 * 
	 * @param progressBar
	 *            the progress bar
	 * @param listener
	 *            the listener
	 */

	private void animate(final HoloCircularProgressBar progressBar, final AnimatorListener listener, final float progress, final int duration) {

		mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
		mProgressBarAnimator.setDuration(duration);

		mProgressBarAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationCancel(final Animator animation) {
			}

			@Override
			public void onAnimationEnd(final Animator animation) {
				progressBar.setProgress(progress);
			}

			@Override
			public void onAnimationRepeat(final Animator animation) {
			}

			@Override
			public void onAnimationStart(final Animator animation) {
			}
		});
		if (listener != null) {
			mProgressBarAnimator.addListener(listener);
		}
		mProgressBarAnimator.reverse();
		mProgressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(final ValueAnimator animation) {
				progressBar.setProgress((Float) animation.getAnimatedValue());
			}
		});
		progressBar.setMarkerProgress(progress);
		mProgressBarAnimator.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		// 退出时释放连接
		mTts.destroy();
		// mIat.cancel();
		// mIat.destroy();
		if (null != mSpeechEvaluator) {
			mSpeechEvaluator.cancel(false);
		}
	}
}

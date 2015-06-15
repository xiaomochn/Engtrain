package com.tt.entrain.msc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;
import com.iflytek.speech.util.FucUtil;
import com.iflytek.speech.util.JsonParser;
import com.tt.engtrain.R;

public class TtsDemo extends Activity implements OnClickListener {
	private static String TAG = "TtsDemo";

	// ������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog iatDialog;
	// ��д�������
	private EditText mResultText;
	int ret = 0;// �������÷���ֵ

	// �����ϳɶ���
	private SpeechSynthesizer mTts;

	// Ĭ���ƶ˷�����
	public static String voicerCloud = "xiaoyan";
	// Ĭ�ϱ��ط�����
	public static String voicerLocal = "xiaoyan";

	// �ƶ˷������б�
	private String[] cloudVoicersEntries;
	private String[] cloudVoicersValue;

	// ���ط������б�
	private String[] localVoicersEntries;
	private String[] localVoicersValue;

	// �������
	private int mPercentForBuffering = 0;
	// ���Ž���
	private int mPercentForPlaying = 0;

	// �ƶ�/����ѡ��ť
	private RadioGroup mRadioGroup;
	// ��������
	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	private Toast mToast;
	private SharedPreferences mSharedPreferences;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ttsdemo);
		initLayout();

		// ��ʼ���ϳɶ���
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

		// �ƶ˷����������б�
		cloudVoicersEntries = getResources().getStringArray(
				R.array.voicer_cloud_entries);
		cloudVoicersValue = getResources().getStringArray(
				R.array.voicer_cloud_values);

		// ���ط����������б�
		localVoicersEntries = getResources().getStringArray(
				R.array.voicer_local_entries);
		localVoicersValue = getResources().getStringArray(
				R.array.voicer_local_values);

		mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		{
			// ��ʼ��ʶ�����
			mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
			// ��ʼ����дDialog,���ֻʹ����UI��д����,���贴��SpeechRecognizer
			iatDialog = new RecognizerDialog(this, mInitListener);

			mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
					Activity.MODE_PRIVATE);
			mResultText = ((EditText) findViewById(R.id.iat_text));
		}
	}

	/**
	 * ��ʼ��Layout��
	 */
	private void initLayout() {
		findViewById(R.id.iat_recognize).setOnClickListener(this);
		findViewById(R.id.iat_upload_contacts).setOnClickListener(this);
		findViewById(R.id.iat_upload_userwords).setOnClickListener(this);	
		findViewById(R.id.iat_stop).setOnClickListener(this);
		findViewById(R.id.iat_cancel).setOnClickListener(this);
		
		findViewById(R.id.tts_play).setOnClickListener(this);

		findViewById(R.id.tts_cancel).setOnClickListener(this);
		findViewById(R.id.tts_pause).setOnClickListener(this);
		findViewById(R.id.tts_resume).setOnClickListener(this);
		findViewById(R.id.image_tts_set).setOnClickListener(this);

		findViewById(R.id.tts_btn_person_select);
		findViewById(R.id.tts_btn_person_select).setOnClickListener(this);

		mRadioGroup = ((RadioGroup) findViewById(R.id.tts_rediogroup));
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tts_radioCloud:
					mEngineType = SpeechConstant.TYPE_CLOUD;
					break;
				case R.id.tts_radioLocal:
					mEngineType = SpeechConstant.TYPE_LOCAL;
////					 ������������
//					 String path = ResourceUtil.TTS_RES_PATH+"="+
//					 ResourceLoader.getResPath(TtsDemo.this,mSharedPreferences,"tts")+","+ResourceUtil.ENGINE_START+"=tts";
//					 Boolean ret =
//					 SpeechUtility.getUtility().setParameter(ResourceUtil.ENGINE_START,path);
//					 showTip("����������������"+ret);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.image_tts_set:
			Intent intent = new Intent(TtsDemo.this, TtsSettings.class);
			startActivity(intent);
			break;
		// ��ʼ�ϳ�
		case R.id.tts_play:
			String text = ((EditText) findViewById(R.id.tts_text)).getText()
					.toString();
			// ���ò���
			setParam();
			int code = mTts.startSpeaking(text, mTtsListener);
			if (code != ErrorCode.SUCCESS) {
				showTip("�����ϳ�ʧ��,������: " + code);
			}
			break;
		// ȡ���ϳ�
		case R.id.tts_cancel:
			mTts.stopSpeaking();
			break;
		// ��ͣ����
		case R.id.tts_pause:
			mTts.pauseSpeaking();
			break;
		// ��������
		case R.id.tts_resume:
			mTts.resumeSpeaking();
			break;
		// ѡ������
		case R.id.tts_btn_person_select:
			showPresonSelectDialog();
			// �����������ҳ��
		case R.id.image_iat_set:
			Intent intents = new Intent(TtsDemo.this, IatSettings.class);
			startActivity(intents);
			break;
		// ��ʼ��д
		case R.id.iat_recognize:
			mResultText.setText(null);// �����ʾ����
			// ���ò���
			setParam1();
			boolean isShowDialog = mSharedPreferences.getBoolean(
					getString(R.string.pref_key_iat_show), true);
			if (isShowDialog) {
				// ��ʾ��д�Ի���
				iatDialog.setListener(recognizerDialogListener);
				iatDialog.show();
				showTip(getString(R.string.text_begin));
			} else {
				// ����ʾ��д�Ի���
				ret = mIat.startListening(recognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					showTip("��дʧ��,�����룺" + ret);
				} else {
					showTip(getString(R.string.text_begin));
				}
			}
			break;
		// ֹͣ��д
		case R.id.iat_stop:
			mIat.stopListening();
			showTip("ֹͣ��д");
			break;
		// ȡ����д
		case R.id.iat_cancel:
			mIat.cancel();
			showTip("ȡ����д");
			break;
		// �ϴ���ϵ�� --- �ƶ�
		case R.id.iat_upload_contacts:
//			Toast.makeText(TtsDemo.this,
//					getString(R.string.text_upload_contacts),
//					Toast.LENGTH_SHORT).show();
//			ContactManager mgr = ContactManager.createManager(TtsDemo.this,
//					mContactListener);
//			mgr.asyncQueryAllContactsName();
//			break;
		// �ϴ��û��ʱ�
		case R.id.iat_upload_userwords:
//			Toast.makeText(TtsDemo.this,
//					getString(R.string.text_upload_userwords),
//					Toast.LENGTH_SHORT).show();
//			String contents = FucUtil.readFile(this, "userwords", "utf-8");
//			// ����������
//			mIat.setParameter(SpeechConstant.ENGINE_TYPE,
//					SpeechConstant.TYPE_CLOUD);
//			// �ñ�������
//			mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
//			ret = mIat.updateLexicon("userword", contents, lexiconListener);
//			if (ret != ErrorCode.SUCCESS)
//				showTip("�ϴ��ȴ�ʧ��,�����룺" + ret);
			break;
		}
	}

	private static int selectedNumCloud = 0;
	private static int selectedNumLocal = 0;

	/**
	 * ������ѡ��
	 */
	private void showPresonSelectDialog() {
		switch (mRadioGroup.getCheckedRadioButtonId()) {
		// ѡ�����ߺϳ�
		case R.id.tts_radioCloud:
			new AlertDialog.Builder(this).setTitle("���ߺϳɷ�����ѡ��")
					.setSingleChoiceItems(cloudVoicersEntries, // ��ѡ���м���,����ʲô����
							selectedNumCloud, // Ĭ�ϵ�ѡ��
							new DialogInterface.OnClickListener() { // �����ѡ���Ĵ���
								public void onClick(DialogInterface dialog,
										int which) { // �������һ��
									voicerCloud = cloudVoicersValue[which];
									if ("catherine".equals(voicerCloud)
											|| "henry".equals(voicerCloud)
											|| "vimary".equals(voicerCloud)) {
										((EditText) findViewById(R.id.tts_text))
												.setText(R.string.text_tts_source_en);
									} else {
										((EditText) findViewById(R.id.tts_text))
												.setText(R.string.text_tts_source);
									}
									selectedNumCloud = which;
									dialog.dismiss();
								}
							}).show();
			break;

		// ѡ�񱾵غϳ�
		case R.id.tts_radioLocal:
			new AlertDialog.Builder(this).setTitle("���غϳɷ�����ѡ��")
					.setSingleChoiceItems(localVoicersEntries, // ��ѡ���м���,����ʲô����
							selectedNumLocal, // Ĭ�ϵ�ѡ��
							new DialogInterface.OnClickListener() { // �����ѡ���Ĵ���
								public void onClick(DialogInterface dialog,
										int which) { // �������һ��
									voicerLocal = localVoicersValue[which];
									if ("catherine".equals(voicerLocal)
											|| "henry".equals(voicerLocal)
											|| "vimary".equals(voicerLocal)) {
										((EditText) findViewById(R.id.tts_text))
												.setText(R.string.text_tts_source_en);
									} else {
										((EditText) findViewById(R.id.tts_text))
												.setText(R.string.text_tts_source);
									}
									selectedNumLocal = which;
									dialog.dismiss();
								}
							}).show();
			break;
		default:
			break;
		}
	}

	/**
	 * ��ʼ��������
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("��ʼ��ʧ��,�����룺" + code);
			}
		}
	};

	/**
	 * �ϳɻص�������
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			showTip("��ʼ����");
		}

		@Override
		public void onSpeakPaused() {
			showTip("��ͣ����");
		}

		@Override
		public void onSpeakResumed() {
			showTip("��������");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			mPercentForBuffering = percent;
			mToast.setText(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));

			mToast.show();
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			mPercentForPlaying = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				showTip("�������");
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}
	};

	private void showTip(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}

	/**
	 * ��������
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {
		// ��ղ���
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// ���úϳ�
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			// ����ʹ���ƶ�����
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// ���÷�����
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicerCloud);
		} else {
			// ����ʹ�ñ�������
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// ���÷�������Դ·��
			mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
			// ���÷�����
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
		}

		// ��������
		mTts.setParameter(SpeechConstant.SPEED,
				mSharedPreferences.getString("speed_preference", "50"));

		// ��������
		mTts.setParameter(SpeechConstant.PITCH,
				mSharedPreferences.getString("pitch_preference", "50"));

		// ��������
		mTts.setParameter(SpeechConstant.VOLUME,
				mSharedPreferences.getString("volume_preference", "50"));

		// ���ò�������Ƶ������
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferences.getString("stream_preference", "3"));
	}

	// ��ȡ��������Դ·��
	private String getResourcePath() {
		StringBuffer tempBuffer = new StringBuffer();
		// �ϳ�ͨ����Դ
		tempBuffer.append(ResourceUtil.generateResourcePath(this,
				RESOURCE_TYPE.assets, "tts/common.jet"));
		tempBuffer.append(";");
		// ��������Դ
		tempBuffer.append(ResourceUtil.generateResourcePath(this,
				RESOURCE_TYPE.assets, "tts/" + TtsDemo.voicerLocal + ".jet"));
		return tempBuffer.toString();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		// �˳�ʱ�ͷ�����
		mTts.destroy();
	}

	// ʶ�𲿷�

	/**
	 * ��ʼ����������
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("��ʼ��ʧ��,�����룺" + code);
			}
		}
	};

	/**
	 * �ϴ���ϵ��/�ʱ��������
	 */

	/**
	 * ��д��������
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			showTip("��ʼ˵��");
		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("����˵��");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
			if (isLast) {
				// TODO ���Ľ��
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("��ǰ����˵����������С��" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	/**
	 * ��дUI������
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult result, boolean isLast) {
			Log.d(TAG, "recognizer result��" + result.getResultString());
			String text = JsonParser.parseIatResult(result.getResultString());
			mResultText.append(text);
			mResultText.setSelection(mResultText.length());
		}

		/**
		 * ʶ��ص�����.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	/**
	 * ��ȡ��ϵ�˼�������
	 */


	/**
	 * ��������
	 * 
	 * @param param
	 * @return
	 */
	public void setParam1() {
		// ��ղ���
		mIat.setParameter(SpeechConstant.PARAMS, null);
		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		// ��������
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

		if (lag.equals("en_us")) {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// ��������ǰ�˵�
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// ����������˵�
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// ���ñ�����
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));
		// ������Ƶ����·��
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.pcm");
	}

}

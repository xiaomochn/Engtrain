package com.tt.entrain.msc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.speech.util.FucUtil;
import com.iflytek.speech.util.JsonParser;
import com.tt.engtrain.R;

public class IatDemo extends Activity implements OnClickListener{
	private static String TAG = "IatDemo";
	// ������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog iatDialog;
	// ��д�������
	private EditText mResultText;
	
	private Toast mToast;

	private SharedPreferences mSharedPreferences;
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.iatdemo);
		initLayout();

		// ��ʼ��ʶ�����
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// ��ʼ����дDialog,���ֻʹ����UI��д����,���贴��SpeechRecognizer
		iatDialog = new RecognizerDialog(this,mInitListener);
		
		mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);	
		mResultText = ((EditText)findViewById(R.id.iat_text));
	}

	/**
	 * ��ʼ��Layout��
	 */
	private void initLayout(){
		findViewById(R.id.iat_recognize).setOnClickListener(this);
		findViewById(R.id.iat_upload_contacts).setOnClickListener(this);
		findViewById(R.id.iat_upload_userwords).setOnClickListener(this);	
		findViewById(R.id.iat_stop).setOnClickListener(this);
		findViewById(R.id.iat_cancel).setOnClickListener(this);
		findViewById(R.id.image_iat_set).setOnClickListener(this);
	}

	int ret = 0;// �������÷���ֵ
	@Override
	public void onClick(View view) {				
		switch (view.getId()) {
		// �����������ҳ��
		case R.id.image_iat_set:
			Intent intents = new Intent(IatDemo.this, IatSettings.class);
			startActivity(intents);
			break;
			// ��ʼ��д
		case R.id.iat_recognize:
			mResultText.setText(null);// �����ʾ����
			// ���ò���
			setParam();
			boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), true);
			if (isShowDialog) {
				// ��ʾ��д�Ի���
				iatDialog.setListener(recognizerDialogListener);
				iatDialog.show();
				showTip(getString(R.string.text_begin));
			} else {
				// ����ʾ��д�Ի���
				ret = mIat.startListening(recognizerListener);
				if(ret != ErrorCode.SUCCESS){
					showTip("��дʧ��,�����룺" + ret);
				}else {
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
			Toast.makeText(IatDemo.this, getString(R.string.text_upload_contacts),Toast.LENGTH_SHORT).show();
			ContactManager mgr = ContactManager.createManager(IatDemo.this, mContactListener);	
			mgr.asyncQueryAllContactsName();
			break;
			// �ϴ��û��ʱ�
		case R.id.iat_upload_userwords:
			Toast.makeText(IatDemo.this, getString(R.string.text_upload_userwords),Toast.LENGTH_SHORT).show();
			String contents = FucUtil.readFile(this, "userwords","utf-8");
			//����������
			mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			//�ñ�������
			mIat.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");			
			ret = mIat.updateLexicon("userword", contents, lexiconListener);
			if(ret != ErrorCode.SUCCESS)
				showTip("�ϴ��ȴ�ʧ��,�����룺" + ret);
			break;
		default:
			break;
		}
	}


	/**
	 * ��ʼ����������
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("��ʼ��ʧ��,�����룺"+code);
        	}
		}
	};
	
	/**
	 * �ϴ���ϵ��/�ʱ��������
	 */
	private LexiconListener lexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if(error != null){
				showTip(error.toString());
			}
			else{
				showTip(getString(R.string.text_upload_success));
			}
		}
	};
	
	/**
	 * ��д��������
	 */
	private RecognizerListener recognizerListener=new RecognizerListener(){

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
			if(isLast) {
				//TODO ���Ľ��
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
	private RecognizerDialogListener recognizerDialogListener=new RecognizerDialogListener(){
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
	private ContactListener mContactListener = new ContactListener() {
		@Override
		public void onContactQueryFinish(String contactInfos, boolean changeFlag) {
			//ע��ʵ��Ӧ���г���һ���ϴ�֮�⣬֮��Ӧ��ͨ��changeFlag�ж��Ƿ���Ҫ�ϴ����������ɲ���Ҫ������.
			//			if(changeFlag) {
			//ָ����������
			mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			mIat.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");			
			ret = mIat.updateLexicon("contact", contactInfos, lexiconListener);
			if(ret != ErrorCode.SUCCESS)
				showTip("�ϴ���ϵ��ʧ�ܣ�" + ret);
			//			}		
		}		

	};

	private void showTip(final String str)
	{
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
	 * @param param
	 * @return 
	 */
	public void setParam(){
		// ��ղ���
		mIat.setParameter(SpeechConstant.PARAMS, null);
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		// ��������
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

		if (lag.equals("en_us")) {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// ��������
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}

		// ��������ǰ�˵�
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// ����������˵�
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// ���ñ�����
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// ������Ƶ����·��
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/iflytek/wavaudio.pcm");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �˳�ʱ�ͷ�����
		mIat.cancel();
		mIat.destroy();
	}
}

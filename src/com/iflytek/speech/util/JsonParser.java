package com.iflytek.speech.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Json���������
 */
public class JsonParser {

	public static String parseIatResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// תд����ʣ�Ĭ��ʹ�õ�һ�����
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				�����Ҫ���ѡ������������������ֶ�
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret.toString();
	}
	
	public static String parseGrammarResult(String json, String engType) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			// �ƶ˺ͱ��ؽ�����������
			if ("cloud".equals(engType)) {
				for (int i = 0; i < words.length(); i++) {
					JSONArray items = words.getJSONObject(i).getJSONArray("cw");
					for(int j = 0; j < items.length(); j++)
					{
						JSONObject obj = items.getJSONObject(j);
						if(obj.getString("w").contains("nomatch"))
						{
							ret.append("û��ƥ����.");
							return ret.toString();
						}
						ret.append("�������" + obj.getString("w"));
						ret.append("�����Ŷȡ�" + obj.getInt("sc"));
						ret.append("\n");
					}
				}
			} else if ("local".equals(engType)) {
				ret.append("�������");
				for (int i = 0; i < words.length(); i++) {
					JSONObject wsItem = words.getJSONObject(i);
					JSONArray items = wsItem.getJSONArray("cw");
					if ("<contact>".equals(wsItem.getString("slot"))) {
						// ���ܻ��ж����ϵ�˹�ѡ��������������������Щ��ѡ�������ͬ�����Ŷ�
						ret.append("��");
						for(int j = 0; j < items.length(); j++)
						{
							JSONObject obj = items.getJSONObject(j);
							if(obj.getString("w").contains("nomatch"))
							{
								ret.append("û��ƥ����.");
								return ret.toString();
							}
							ret.append(obj.getString("w")).append("|");						
						}
						ret.setCharAt(ret.length() - 1, '��');
					} else {
						//���ض��ѡ�������Ŷȸߵ�����һ��ѡȡ��һ���������
						JSONObject obj = items.getJSONObject(0);
						if(obj.getString("w").contains("nomatch"))
						{
							ret.append("û��ƥ����.");
							return ret.toString();
						}
						ret.append(obj.getString("w"));						
					}
				}
				ret.append("�����Ŷȡ�" + joResult.getInt("sc"));
				ret.append("\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("û��ƥ����.");
		} 
		return ret.toString();
	}
	
	public static String parseGrammarResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				for(int j = 0; j < items.length(); j++)
				{
					JSONObject obj = items.getJSONObject(j);
					if(obj.getString("w").contains("nomatch"))
					{
						ret.append("û��ƥ����.");
						return ret.toString();
					}
					ret.append("�������" + obj.getString("w"));
					ret.append("�����Ŷȡ�" + obj.getInt("sc"));
					ret.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("û��ƥ����.");
		} 
		return ret.toString();
	}
}

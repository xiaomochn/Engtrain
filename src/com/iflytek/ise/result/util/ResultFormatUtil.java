/**
 * 
 */
package com.iflytek.ise.result.util;

import java.util.ArrayList;

import com.iflytek.ise.result.entity.Phone;
import com.iflytek.ise.result.entity.Sentence;
import com.iflytek.ise.result.entity.Syll;
import com.iflytek.ise.result.entity.Word;

/**
 * <p>
 * Title: ResultFormatUtl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.iflytek.com
 * </p>
 * 
 * @author iflytek
 * @date 2015��1��19�� ����10:01:14
 */
public class ResultFormatUtil {

	/**
	 * ��Ӣ���������鰴��ʽ���
	 * 
	 * @param sentences
	 * @return Ӣ����������
	 */
	public static String formatDetails_EN(ArrayList<Sentence> sentences) {
		StringBuffer buffer = new StringBuffer();
		if (null == sentences) {
			return buffer.toString();
		}

		for (Sentence sentence : sentences) {
			if ("����".equals(ResultTranslateUtil.getContent(sentence.content)) || "����".equals(ResultTranslateUtil.getContent(sentence.content))) {
				continue;
			}

			if (null == sentence.words) {
				continue;
			}
			for (Word word : sentence.words) {
				if ("����".equals(ResultTranslateUtil.getContent(word.content)) || "����".equals(ResultTranslateUtil.getContent(word.content))) {
					continue;
				}

				buffer.append("\n����[" + ResultTranslateUtil.getContent(word.content) + "] ").append("�ʶ���" + ResultTranslateUtil.getDpMessageInfo(word.dp_message)).append(" �÷֣�" + word.total_score);
				if (null == word.sylls) {
					buffer.append("\n");
					continue;
				}

				for (Syll syll : word.sylls) {
					buffer.append("\n������[" + ResultTranslateUtil.getContent(syll.getStdSymbol()) + "] ");
					if (null == syll.phones) {
						continue;
					}

					for (Phone phone : syll.phones) {
						buffer.append("\n\t������[" + ResultTranslateUtil.getContent(phone.getStdSymbol()) + "] ").append(" �ʶ���" + ResultTranslateUtil.getDpMessageInfo(phone.dp_message));
					}

				}
				buffer.append("\n");
			}
		}

		return buffer.toString();
	}

	/**
	 * �������������鰴��ʽ���
	 * 
	 * @param sentences
	 * @return ������������
	 */
	public static String formatDetails_CN(ArrayList<Sentence> sentences) {
		StringBuffer buffer = new StringBuffer();
		if (null == sentences) {
			return buffer.toString();
		}

		for (Sentence sentence : sentences) {
			if (null == sentence.words) {
				continue;
			}

			for (Word word : sentence.words) {
				buffer.append("\n����[" + ResultTranslateUtil.getContent(word.content) + "] " + word.symbol + " ʱ����" + word.time_len);
				if (null == word.sylls) {
					continue;
				}

				for (Syll syll : word.sylls) {
					if ("����".equals(ResultTranslateUtil.getContent(syll.content)) || "����".equals(ResultTranslateUtil.getContent(syll.content))) {
						continue;
					}

					buffer.append("\n������[" + ResultTranslateUtil.getContent(syll.content) + "] " + syll.symbol + " ʱ����" + syll.time_len);
					if (null == syll.phones) {
						continue;
					}

					for (Phone phone : syll.phones) {
						buffer.append("\n\t������[" + ResultTranslateUtil.getContent(phone.content) + "] " + "ʱ����" + phone.time_len).append(" �ʶ���" + ResultTranslateUtil.getDpMessageInfo(phone.dp_message));
					}

				}
				buffer.append("\n");
			}
		}

		return buffer.toString();
	}
}

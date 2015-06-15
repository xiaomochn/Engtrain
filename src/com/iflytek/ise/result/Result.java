/**
 * 
 */
package com.iflytek.ise.result;

import java.util.ArrayList;

import com.iflytek.ise.result.entity.Sentence;

/**
 * <p>
 * Title: Result
 * </p>
 * <p>
 * Description: ������
 * </p>
 * <p>
 * Company: www.iflytek.com
 * </p>
 * 
 * @author iflytek
 * @date 2015��1��12�� ����4:58:38
 */
public class Result {
	/**
	 * �������֣�en��Ӣ�ģ���cn�����ģ�
	 */
	public String language;
	/**
	 * �������ࣺread_syllable��cn���֣���read_word�������read_sentence�����ӣ�
	 */
	public String category;
	/**
	 * ��ʼ֡λ�ã�ÿ֡�൱��10ms
	 */
	public int beg_pos;
	/**
	 * ����֡λ��
	 */
	public int end_pos;
	/**
	 * ��������
	 */
	public String content;
	/**
	 * �ܵ÷�
	 */
	public float total_score;
	/**
	 * ʱ����cn��
	 */
	public int time_len;
	/**
	 * �쳣��Ϣ��en��
	 */
	public String except_info;
	/**
	 * �Ƿ��Ҷ���cn��
	 */
	public boolean is_rejected;
	/**
	 * xml����е�sentence��ǩ
	 */
	public ArrayList<Sentence> sentences;
}

/**
 * 
 */
package com.iflytek.ise.result.entity;

import java.util.ArrayList;

/**
 * <p>
 * Title: Sentence
 * </p>
 * <p>
 * Description: ���ӣ���Ӧ��xml����е�sentence��ǩ
 * </p>
 * <p>
 * Company: www.iflytek.com
 * </p>
 * 
 * @author iflytek
 * @date 2015��1��12�� ����4:10:09
 */
public class Sentence {
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
	 * ʱ������λ��֡��ÿ֡�൱��10ms����cn��
	 */
	public int time_len;
	/**
	 * ���ӵ�������en��
	 */
	public int index;
	/**
	 * ��������en��
	 */
	public int word_count;
	/**
	 * sentence������word
	 */
	public ArrayList<Word> words;
}

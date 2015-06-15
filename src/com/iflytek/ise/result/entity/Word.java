/**
 * 
 */
package com.iflytek.ise.result.entity;

import java.util.ArrayList;

/**
 * <p>
 * Title: Word
 * </p>
 * <p>
 * Description: ���ʣ���Ӧ�ڽ��xml�е�word��ǩ
 * </p>
 * <p>
 * Company: www.iflytek.com
 * </p>
 * 
 * @author iflytek
 * @date 2015��1��12�� ����3:29:30
 */
public class Word {
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
	 * ��©����Ϣ��0����ȷ����16��©������32����������64���ض�����128���滻��
	 */
	public int dp_message;
	/**
	 * ������ȫƪ������en��
	 */
	public int global_index;
	/**
	 * �����ھ����е�������en��
	 */
	public int index;
	/**
	 * ƴ����cn�������ִ���������5��ʾ��������fen1
	 */
	public String symbol;
	/**
	 * ʱ������λ��֡��ÿ֡�൱��10ms����cn��
	 */
	public int time_len;
	/**
	 * ���ʵ÷֣�en��
	 */
	public float total_score;
	/**
	 * Word������Syll
	 */
	public ArrayList<Syll> sylls;

}

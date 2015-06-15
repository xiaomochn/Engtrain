/**
 * 
 */
package com.iflytek.ise.result;

import com.iflytek.ise.result.util.ResultFormatUtil;

/**
 * <p>
 * Title: ReadWordResult
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.iflytek.com
 * </p>
 * 
 * @author iflytek
 * @date 2015��1��12�� ����5:03:50
 */
public class ReadWordResult extends Result {

	public ReadWordResult() {
		category = "read_word";
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		if ("cn".equals(language)) {
			buffer.append("[������]\n").append("�������ݣ�" + content + "\n").append("�ʶ�ʱ����" + time_len + "\n").append("�ܷ֣�" + total_score + "\n\n").append("[�ʶ�����]").append(ResultFormatUtil.formatDetails_CN(sentences));
		} else {
			if (is_rejected) {
				buffer.append("��⵽�Ҷ���").append("except_info:" + except_info + "\n\n"); // except_info����˵�����������������������˵���ĵ���
			}

			buffer.append("[������]\n").append("�������ݣ�" + content + "\n").append("�ܷ֣�" + total_score + "\n\n").append("[�ʶ�����]").append(ResultFormatUtil.formatDetails_EN(sentences));
		}

		return buffer.toString();
	}
}
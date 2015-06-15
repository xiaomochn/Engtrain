package com.tt.engtrain.test;

import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.speech.util.TextComputer;

public class SampleTest extends InstrumentationTestCase {
	private Button button = null;
	private TextView text = null;

	/*
	 * 初始设置
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	/*
	 * // * 垃圾清理与资源回收 // * @see android.test.InstrumentationTestCase#tearDown()
	 * //
	 */
	// @Override
	// protected void tearDown() {
	// sample.finish();
	// try {
	// super.tearDown();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/*
	 * 活动功能测试
	 */
	public void testActivity() throws Exception {
		Log.v("testActivity", "test the Activity");
		SystemClock.sleep(1500);
		getInstrumentation().runOnMainSync(new PerformClick(button));
		SystemClock.sleep(3000);
		assertEquals("Hello Android", text.getText().toString());
	}

	/*
	 * 模拟按钮点击的接口
	 */
	private class PerformClick implements Runnable {
		Button btn;

		public PerformClick(Button button) {
			btn = button;
		}

		public void run() {
			btn.performClick();
		}
	}

	public void testAdd() throws Exception {
		// TextComputer.SimilarDegree("asdfasdfas", "asdfasdfasd");
		// System.out.println();
		Log.v("testActivity",
				TextComputer.SimilarDegree(
						"In case you haven't heard, Ireland passed a historic referendum Friday. For the first time, same-sex marriage legalization was voted on by the people . Here are some of the best celeb reactions to the news. ",
						" news.he first time, same-sex marriage  ")
						+ "");
	}
	// /*
	// * 测试类中的方法
	// */
	// public void testAdd() throws Exception{
	// String tag = "testAdd";
	// Log.v(tag, "test the method");
	// int test = sample.add(1, 1);
	// assertEquals(2, test);
	// }
}
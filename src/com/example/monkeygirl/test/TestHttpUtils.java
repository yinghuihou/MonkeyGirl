package com.example.monkeygirl.test;

import com.example.monkeygirl.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

@SuppressWarnings("deprecation")
public class TestHttpUtils extends AndroidTestCase {
	
	public void test(){
		
		String string1 = HttpUtils.doGet("����Ц��");
		Log.d("xxx", string1);
		
		String string2 = HttpUtils.doGet("���");
		Log.d("xxx", string2);
		String string3 = HttpUtils.doGet("��ɳ�������");
		Log.d("xxx", string3);
	}
}

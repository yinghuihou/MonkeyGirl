package com.example.monkeygirl.test;

import com.example.monkeygirl.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

@SuppressWarnings("deprecation")
public class TestHttpUtils extends AndroidTestCase {
	
	public void test(){
		
		String string1 = HttpUtils.doGet("讲个笑话");
		Log.d("xxx", string1);
		
		String string2 = HttpUtils.doGet("你好");
		Log.d("xxx", string2);
		String string3 = HttpUtils.doGet("长沙天气如何");
		Log.d("xxx", string3);
	}
}

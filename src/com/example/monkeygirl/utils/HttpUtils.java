package com.example.monkeygirl.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * 发送Http请求的工具类
 */

import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.example.monkeygirl.bean.ChatMessage;
import com.example.monkeygirl.bean.ChatMessage.Type;
import com.example.monkeygirl.bean.Result;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import android.util.Log;
public class HttpUtils {
	
	private static final String URL = "http://www.tuling123.com/openapi/api";
	private static final String API_KEY = "93a46e7900d94247880bc32b3bbcecb5";
	
	
	//使用Gson将json字符串转化为Java对象
	public static ChatMessage sendMessage(String msg){
		
		ChatMessage chatMessage = new ChatMessage();
		
		String jsonRes = doGet(msg);
		Gson gson = new Gson();
		Result result = null;
				
		try {
			result = gson.fromJson(jsonRes, Result.class);
			chatMessage.setMsg(result.getText());
		} catch (JsonSyntaxException e) {
			chatMessage.setMsg("连接断了，等会再聊！");
		}
		
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		return chatMessage;
	}
	
	
	public static String doGet(String msg){
		String result = "";
		String url = setParams(msg);
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			URL urlNet = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlNet.openConnection();
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			
			is = connection.getInputStream();
			int len = -1;
			byte[] buf = new byte[128];
			baos = new ByteArrayOutputStream();

			// 当读取到数据时候
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			result = new String(baos.toByteArray());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	//对url进行格式化
	private static String setParams(String mString) {
		String url = "";
		try {
			url = URL + "?key=" + API_KEY + "&info=" +URLEncoder.encode(mString,"UTF-8") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return url;
	}
}

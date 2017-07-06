package com.example.monkeygirl.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * ����Http����Ĺ�����
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
	
	
	//ʹ��Gson��json�ַ���ת��ΪJava����
	public static ChatMessage sendMessage(String msg){
		
		ChatMessage chatMessage = new ChatMessage();
		
		String jsonRes = doGet(msg);
		Gson gson = new Gson();
		Result result = null;
				
		try {
			result = gson.fromJson(jsonRes, Result.class);
			chatMessage.setMsg(result.getText());
		} catch (JsonSyntaxException e) {
			chatMessage.setMsg("���Ӷ��ˣ��Ȼ����ģ�");
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

			// ����ȡ������ʱ��
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

	//��url���и�ʽ��
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

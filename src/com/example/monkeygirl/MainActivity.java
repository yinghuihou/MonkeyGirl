package com.example.monkeygirl;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.example.monkeygirl.bean.ChatMessage;
import com.example.monkeygirl.bean.ChatMessage.Type;
import com.example.monkeygirl.utils.HttpUtils;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView mMsgs;
	private ChatMessageAdapter madapter;
	private List<ChatMessage> mDates;
	private EditText mInputMsg;
	private Button  mSendMsg;
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			//等待接受子线程数据的返回
			ChatMessage message = (ChatMessage) msg.obj;
			mDates.add(message);
			madapter.notifyDataSetChanged();
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initView();
		initDatas();
		initListener();
	}

	private void initListener() {
		
		mSendMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final String toMsg = mInputMsg.getText().toString();
				
				if (TextUtils.isEmpty(toMsg)) {
					Toast.makeText(MainActivity.this,"发送消息不能为空" ,	 
							Toast.LENGTH_SHORT).show();
					return;
				}
				//将我们自己写的内容也加入到listview
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				mDates.add(toMessage);
				madapter.notifyDataSetChanged();
				
				//点击发送按钮之后将输入框文本清空
				mInputMsg.setText("");
				
				//点击按钮将我们的字符串传过去
				new Thread()
				{
					public void run() {
						ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
						Message message = Message.obtain();
						message.obj = fromMessage;
						mHandler.sendMessage(message);
						
					};
					
				}.start();
				
				
			}
		});
	}

	//初始化listview数据源
	private void initDatas() {
		mDates = new ArrayList<ChatMessage>();
		mDates.add(new ChatMessage("你好，我是侯姑娘",Type.INCOMING,new Date()));
		madapter = new ChatMessageAdapter(mDates, this);
		mMsgs.setAdapter(madapter);
	}


	private void initView() {
		
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		
	}

}

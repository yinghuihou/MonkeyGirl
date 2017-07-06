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
			
			//�ȴ��������߳����ݵķ���
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
					Toast.makeText(MainActivity.this,"������Ϣ����Ϊ��" ,	 
							Toast.LENGTH_SHORT).show();
					return;
				}
				//�������Լ�д������Ҳ���뵽listview
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				mDates.add(toMessage);
				madapter.notifyDataSetChanged();
				
				//������Ͱ�ť֮��������ı����
				mInputMsg.setText("");
				
				//�����ť�����ǵ��ַ�������ȥ
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

	//��ʼ��listview����Դ
	private void initDatas() {
		mDates = new ArrayList<ChatMessage>();
		mDates.add(new ChatMessage("��ã����Ǻ����",Type.INCOMING,new Date()));
		madapter = new ChatMessageAdapter(mDates, this);
		mMsgs.setAdapter(madapter);
	}


	private void initView() {
		
		mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		
	}

}

package com.ithink.demo;


import com.ithink.camera.control.ITHKPushMessage;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.umeng.message.PushAgent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class MainActivity extends Activity implements OnClickListener{

	private final String TAG = MainActivity.class.getSimpleName();
	private Context context;
	
	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;

	private Button main_get_list_btn;
	private Button main_bind_btn;
	private Button alarm_msg_btn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_main);
		
		initViews();
		
		initEvents();
		
		ITHKPushMessage pushMsg = new ITHKPushMessage(context);
		
		pushMsg.PushAgentEnable();
		pushMsg.commitAPN();
		
		pushMsg.setCommitApnStatusListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					//注册成功
				} else if (status == ITHKStatus.kIS_Error){
					//参数错误
				} else if (status == ITHKStatus.kIS_NoUsr){
					//用户不存在
				}else if (status == ITHKStatus.kIS_ErrorCode){
					//厂商代码错误
				}
			}
		});
		
		
		pushMsg.setCommitApnStatusListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					Toast.makeText(context, "Push Message Service registered successfully", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_center_txt.setText(R.string.app_name);
		
		main_get_list_btn = (Button) findViewById(R.id.main_get_list_btn);
		main_bind_btn = (Button) findViewById(R.id.main_bind_btn);
		alarm_msg_btn = (Button) findViewById(R.id.alarm_msg_btn);
		
	}
	
	private void initEvents(){
		
		main_get_list_btn.setOnClickListener(this);
		main_bind_btn.setOnClickListener(this);
		alarm_msg_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == main_get_list_btn){
			Intent intent = new Intent();
			intent.setClass(context, DeviceListActivity.class);
			startActivity(intent);
		}
		
		if (v == main_bind_btn){
			Intent intent = new Intent();
			intent.setClass(context, BindActivity.class);
			startActivity(intent);
		}
		
		if (v == alarm_msg_btn){
			Intent intent = new Intent();
			intent.setClass(context, AlarmMessageActivity.class);
			startActivity(intent);
		}
	}
}

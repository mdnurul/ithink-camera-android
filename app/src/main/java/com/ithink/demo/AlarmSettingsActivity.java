package com.ithink.demo;

import com.ithink.camera.control.ITHKDeviceManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.view.CustomDialog;
import com.ithink.view.MyProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmSettingsActivity extends Activity implements OnClickListener{

	private final String TAG = AlarmSettingsActivity.class.getSimpleName();

	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;
	
	private String alarmStatus;
	private String alarmSoundStatus;
	private String alarmStartTime;
	private String alarmEndTime;
	
	
	private TextView alarm_switch;
	private TextView alarmSound_switch;
	private TextView alarmTime_tv;
	
	private View alarm_ll;
	private View alarmSound_ll;
	private View alarmTime_ll;
	
	private ITHKDeviceManager ithkDeviceManager;
	
	private String sid;
	
	private MyProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;
		
		setContentView(R.layout.activity_alarm_settings);
		
		ithkDeviceManager = new ITHKDeviceManager(context);
		
		Bundle bundle = getIntent().getExtras();
		
		sid = bundle.getString("sid");
		alarmStatus = bundle.getString("alarmStatus");
		alarmSoundStatus = bundle.getString("alarmSoundStatus");
		alarmStartTime = bundle.getString("alarmStartTime");
		alarmEndTime = bundle.getString("alarmEndTime");
		
		
		initViews();
		
		initEvents();
		
		initDialog();
		
		if (alarmStatus!=null && alarmStatus.equals("1")){
			alarm_switch.setBackgroundResource(R.drawable.ic_switch_on);
		}else{
			alarm_switch.setBackgroundResource(R.drawable.ic_switch_off);
		}
		
		if (alarmSoundStatus!=null && alarmSoundStatus.equals("1")){
			alarmSound_switch.setBackgroundResource(R.drawable.ic_switch_on);
		}else{
			alarmSound_switch.setBackgroundResource(R.drawable.ic_switch_off);
		}
	}


	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("报警设置");
		title_left_txt.setText(R.string.back);
		
		alarm_switch = (TextView) findViewById(R.id.alarm_switch);
		alarmSound_switch = (TextView) findViewById(R.id.alarmSound_switch);
		
		alarmTime_tv = (TextView) findViewById(R.id.time);
		
		alarm_ll = (View) findViewById(R.id.alarm_ll);
		alarmSound_ll = (View) findViewById(R.id.alarmSound_ll);
		alarmTime_ll = (View) findViewById(R.id.alarmTime_ll);
		
		
		start_time = Integer.parseInt(alarmStartTime);
		end_time = Integer.parseInt(alarmEndTime);
		if (start_time == end_time){
			alarmTime_tv.setText("全天报警");
		}else{
			alarmTime_tv.setText(alarmStartTime + "点~" + alarmEndTime + "点");
		}
		
	}
	
	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		alarm_ll.setOnClickListener(this);
		alarmSound_ll.setOnClickListener(this);
		alarmTime_ll.setOnClickListener(this);
		
		ithkDeviceManager.setChangeDeviceAlarmStatusListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					
				}else{
					if (alarmStatus.equals("1")){
						alarmStatus = "0";
						alarm_switch.setBackgroundResource(R.drawable.ic_switch_off);
					}else{
						alarmStatus = "1";
						alarm_switch.setBackgroundResource(R.drawable.ic_switch_on);
					}
					
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "报警开关 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "报警开关 -> 提交参数错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "设备离线", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "网络超时", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		
		ithkDeviceManager.setChangeDeviceAlarmSoundStatusListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					
				}else{
					if (alarmSoundStatus.equals("1")){
						alarmSoundStatus = "0";
						alarmSound_switch.setBackgroundResource(R.drawable.ic_switch_off);
					}else{
						alarmSoundStatus = "1";
						alarmSound_switch.setBackgroundResource(R.drawable.ic_switch_on);
					}
					
					if (status == ITHKStatus.kIS_ErrorCode){
						
						Toast.makeText(context, "摄像机报警响铃 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "摄像机报警响铃 -> 提交参数错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "设备离线", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "网络超时", Toast.LENGTH_SHORT).show();
					}
				}
					
			}
		});
		
		ithkDeviceManager.setSetAlarmTimeListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				
				if (progressDialog!=null)
					progressDialog.dismiss();
				
				if (status == ITHKStatus.kIS_OK){
					timeDialog.dismiss();
					if (start_time == end_time){
						alarmTime_tv.setText( "全天报警");
					}else{
						alarmTime_tv.setText( start_time + "点~" + end_time + "点");
					}
					
				}else {
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "修改报警时间段 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "修改报警时间段 -> 提交的参数信息错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "修改报警时间段 -> 摄像机离线", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "修改报警时间段 -> 网络超时", Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
	}


	private CustomDialog timeDialog;
	
	private TextView time_tip_tv;
	
	private Button start_sub_btn;
	private Button start_add_btn;
	
	private Button end_sub_btn;
	private Button end_add_btn;
	
	private TextView start_time_tv;
	private TextView end_time_tv;
	
	private int start_time = 0;
	private int end_time = 0;
	
	private Button time_dialog_cancel_btn;
	private Button time_dilog_ok_btn;
	

	private void initDialog() {
		timeDialog = new CustomDialog(context, R.style.MyDialog,
				R.layout.alarm_time_dialog_layout);
		Window window = timeDialog.getWindow();
		window.setContentView(R.layout.alarm_time_dialog_layout);
		window.setGravity(Gravity.CENTER);
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); // 这样即可
		
		initDialogViews(window);
		initDialogEvents();
	}
	
	private void initDialogViews(Window window){
		
		time_tip_tv = (TextView) window.findViewById(R.id.time_tip_tv); 
		
		start_add_btn = (Button) window.findViewById(R.id.start_add_btn);
		start_sub_btn = (Button) window.findViewById(R.id.start_sub_btn);
		
		end_add_btn = (Button) window.findViewById(R.id.end_add_btn);
		end_sub_btn = (Button) window.findViewById(R.id.end_sub_btn);
		
		start_time_tv = (TextView) window.findViewById(R.id.start_time_tv);
		end_time_tv = (TextView) window.findViewById(R.id.end_time_tv);
		
		time_dialog_cancel_btn = (Button) window.findViewById(R.id.cancel_btn);
		time_dilog_ok_btn = (Button) window.findViewById(R.id.OK_btn);
	}
	
	private void initDialogEvents(){
		start_add_btn.setOnClickListener(this);
		start_sub_btn.setOnClickListener(this);
		
		end_add_btn.setOnClickListener(this);
		end_sub_btn.setOnClickListener(this);
		
		time_dialog_cancel_btn.setOnClickListener(this);
		time_dilog_ok_btn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
		
		if (v == alarm_ll){
			if (alarmStatus.equals("1")){
				ithkDeviceManager.changeDeviceAlarmStatusForSid(sid, "off");
				alarmStatus = "0";
				alarm_switch.setBackgroundResource(R.drawable.ic_switch_off);
			}else{
				ithkDeviceManager.changeDeviceAlarmStatusForSid(sid, "on");
				alarmStatus = "1";
				alarm_switch.setBackgroundResource(R.drawable.ic_switch_on);
			}
			
		}
		
		if (v == alarmSound_ll){
			if (alarmSoundStatus.equals("1")){
				ithkDeviceManager.changeDeviceAlarmSoundStatusForSid(sid, "off");
				alarmSoundStatus = "0";
				alarmSound_switch.setBackgroundResource(R.drawable.ic_switch_off);
			}else{
				ithkDeviceManager.changeDeviceAlarmSoundStatusForSid(sid, "on");
				alarmSoundStatus = "1";
				alarmSound_switch.setBackgroundResource(R.drawable.ic_switch_on);
			}
		}
		
		if (v == alarmTime_ll){
			start_time_tv.setText(alarmStartTime);
			end_time_tv.setText(alarmEndTime);
			start_time = Integer.parseInt(alarmStartTime);
			end_time = Integer.parseInt(alarmEndTime);
			
			time_tip_tv.setText("报警时间段："+ start_time + "点~" + end_time + "点");
			
			timeDialog.show();
		}
		
		if (v == start_add_btn){
			if (start_time < 23){
				start_time_tv.setText(++start_time + "");
			}else{
				start_time = 0;
				start_time_tv.setText(start_time + "");
			}
			
			if (start_time == end_time){
				time_tip_tv.setText("报警时间段："+ "全天报警");
			}else{
				time_tip_tv.setText("报警时间段："+ start_time + "点~" + end_time + "点");
			}
		}
		
		if (v == start_sub_btn){
			if (start_time > 0){
				start_time_tv.setText(--start_time + "");
			}else{
				start_time = 23;
				start_time_tv.setText(start_time + "");
			}
			
			if (start_time == end_time){
				time_tip_tv.setText("报警时间段："+ "全天报警");
			}else{
				time_tip_tv.setText("报警时间段："+ start_time + "点~" + end_time + "点");
			}
			
		}
		
		if (v == end_add_btn){
			if (end_time < 23){
				end_time_tv.setText(++end_time + "");
			}else{
				end_time = 0;
				end_time_tv.setText(end_time + "");
			}
			
			if (start_time == end_time){
				time_tip_tv.setText("报警时间段："+ "全天报警");
			}else{
				time_tip_tv.setText("报警时间段："+ start_time + "点~" + end_time + "点");
			}
		}
		
		if (v == end_sub_btn){
			if (end_time > 0){
				end_time_tv.setText(--end_time + "");
			}else{
				end_time = 23;
				end_time_tv.setText(end_time + "");
			}
			
			if (start_time == end_time){
				time_tip_tv.setText("报警时间段："+ "全天报警");
			}else{
				time_tip_tv.setText("报警时间段："+ start_time + "点~" + end_time + "点");
			}
		}
		
		if (v == time_dialog_cancel_btn){
			timeDialog.dismiss();
		}
		
		if (v == time_dilog_ok_btn){
			showProgressDialog();
			timeDialog.dismiss();
			ithkDeviceManager.setAlarmTimeForSid(sid, start_time, end_time);
		}
	}
	
	private void showProgressDialog() {
		progressDialog = new MyProgressDialog(context);
		progressDialog.show();
		progressDialog.setProgressBarGone(View.VISIBLE);
		progressDialog.changeDialogTitle("");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);
	}
}

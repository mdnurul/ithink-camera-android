package com.ithink.demo;

import com.ithink.camera.control.ITHKDeviceManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.view.MyProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceInfoActivity extends Activity implements OnClickListener {

	private final String TAG = DeviceInfoActivity.class.getSimpleName();

	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;

	private TextView name_tv;
	private TextView alarm_tv;
	private TextView video_tv;
	private TextView workMode_tv;

	private View name_ll;
	private View status_ll;
	private View led_ll;
	private View voice_ll;
	private View alarm_ll;
	private View video_ll;
	private View subAccount_ll;
	private View workModel_ll;

	private TextView led_switch;
	private TextView voice_switch;
	/**
	 * 删除设备按钮
	 */
	private Button deleteBtn;

	private String sid;

	private String name, remark, ledStatus, soundLeadStatus, alarmStatus, ver,
			cloudStatus, innerIP, mac;

	/**
	 * 报警声音开关状态
	 */
	private String alarmSoundStatus;

	/**
	 * SD卡剩余空间
	 */
	private String sdcardAvailable;
	/**
	 * SD卡整体空间
	 */
	private String sdcardTotal;
	/**
	 * SD状态 （是否插入SD卡 1 是 0 否）
	 */
	private String sdcardStatus;
	/**
	 * 开始报警时间
	 */
	private String alarmStartTime;
	/**
	 * 结束报警时间
	 */
	private String alarmEndTime;
	private String init = "";
	private String type = "";

	/**
	 * 是否显示报警 0否 1是
	 */
	private String showAlarm;
	/**
	 * 是否显示离线模式 0否 1是
	 */
	private String showOffLineModel;
	/**
	 * 离线存储开启状态 0关 1开
	 */
	private String offlineModeStatus;
	/**
	 * 离线视频存储清晰度（1：流畅；2：标清；3：高清）
	 */
	private String definition;
	/**
	 * 录像开关状态 变量 definition 为清晰度 0 未开启 1流畅 2 标清 3高清
	 */
	private String storeStatus;

	/**
	 * 是否显示录像本地存储功能 0 不显示 1 显示
	 */
	private String showLocalStore;

	private ITHKDeviceManager ithkDeviceManager;
	
	private MyProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;

		setContentView(R.layout.activity_device_info);

		ithkDeviceManager = new ITHKDeviceManager(context);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			sid = bundle.getString("sid");
			name = bundle.getString("name");
			remark = bundle.getString("remark");
			ver = bundle.getString("ver");
			ledStatus = bundle.getString("ledStatus");
			soundLeadStatus = bundle.getString("soundLeadStatus");
			alarmStatus = bundle.getString("alarmStatus");
			alarmSoundStatus = bundle.getString("alarmSoundStatus");
			alarmStartTime = bundle.getString("alarmStartTime");
			alarmEndTime = bundle.getString("alarmEndTime");
			cloudStatus = bundle.getString("cloudStatus");
			// status = bundle.getString("status");
			innerIP = bundle.getString("innerIP");
			mac = bundle.getString("mac");
			init = bundle.getString("init");
			type = bundle.getString("type");
			showAlarm = bundle.getString("showAlarm");
			showOffLineModel = bundle.getString("showOffLineModel");
			offlineModeStatus = bundle.getString("offLineModeStatus");
			definition = bundle.getString("definition");
			sdcardTotal = bundle.getString("sdcardTotal");
			sdcardAvailable = bundle.getString("sdcardAvailable");
			sdcardStatus = bundle.getString("sdcardStatus");
			storeStatus = bundle.getString("storeStatus");
			showLocalStore = bundle.getString("showLocalStore");
		}

		initViews();

		initEvents();

	}
	
	private void initViews() {
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("摄像机功能");
		title_left_txt.setText(R.string.back);

		name_tv = (TextView) findViewById(R.id.tvDevName);
		alarm_tv = (TextView) findViewById(R.id.tvAlarm);
		video_tv = (TextView) findViewById(R.id.videoStatus);
		workMode_tv = (TextView) findViewById(R.id.workMode_tv);

		led_switch = (TextView) findViewById(R.id.led_switch);
		voice_switch = (TextView) findViewById(R.id.voice_switch);
		
		if (ledStatus != null && ledStatus.equals("1")){
			led_switch.setBackgroundResource(R.drawable.ic_switch_on);
		}else{
			led_switch.setBackgroundResource(R.drawable.ic_switch_off);
		}
		
		if (soundLeadStatus != null && soundLeadStatus.equals("1")){
			voice_switch.setBackgroundResource(R.drawable.ic_switch_on);
		}else{
			voice_switch.setBackgroundResource(R.drawable.ic_switch_off);
		}
		
		name_ll = (View) findViewById(R.id.devName_ll);
		status_ll = (View) findViewById(R.id.devStatus_ll);
		led_ll = (View) findViewById(R.id.led_LL);
		voice_ll = (View) findViewById(R.id.voice_ll);
		alarm_ll = (View) findViewById(R.id.alarm_ll);
		video_ll = (View) findViewById(R.id.video_ll);
		subAccount_ll = (View) findViewById(R.id.subAccount_ll);
		workModel_ll = (View) findViewById(R.id.workModel_ll);

		deleteBtn = (Button) findViewById(R.id.dev_info_delete_btn);
		
		
		name_tv.setText(name);
		initAlarmTime();
		
		String videoQuality = "";
		
		if (storeStatus.equals("0")){
			videoQuality = "关";
		}else{
			if (definition.equals("1")){
			videoQuality = "流畅";
			}else if (definition.equals("2")){
				videoQuality = "标清";
			}else if (definition.equals("3")){
				videoQuality = "高清";
			}
		}
		video_tv.setText(videoQuality);
		
		if (offlineModeStatus != null && !offlineModeStatus.equals("")) {
			if (offlineModeStatus.equals("0")) {
				workMode_tv.setText("联网模式");
			} else {
				workMode_tv.setText("断网模式");
			}
		}
		
		
	}

	private void initEvents() {
		
		title_left_ll.setOnClickListener(this);
		name_ll.setOnClickListener(this);
		status_ll.setOnClickListener(this);
		led_ll.setOnClickListener(this);
		voice_ll.setOnClickListener(this);
		alarm_ll.setOnClickListener(this);
		video_ll.setOnClickListener(this);
		subAccount_ll.setOnClickListener(this);
		workModel_ll.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);

		ithkDeviceManager.setChangeDevLedStatusListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					
				}else {
					if (ledStatus.equals("1")){
						ledStatus = "0";
						led_switch.setBackgroundResource(R.drawable.ic_switch_off);
					}else{
						ledStatus = "1";
						led_switch.setBackgroundResource(R.drawable.ic_switch_on);
					}
					if (status == ITHKStatus.kIS_ErrorCode){
						
						Toast.makeText(context, "开关LED -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
						
					}else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "开关LED -> 提交参数错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "设备离线", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "网络超时", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		ithkDeviceManager.setChangeDevVoiceGuideListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					
				}else {
					if (soundLeadStatus.equals("1")){
						soundLeadStatus = "0";
						voice_switch.setBackgroundResource(R.drawable.ic_switch_off);
					}else{
						soundLeadStatus = "1";
						voice_switch.setBackgroundResource(R.drawable.ic_switch_on);
					}
					if (status == ITHKStatus.kIS_ErrorCode){
						
						Toast.makeText(context, "语音播放 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
						
					}else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "语音播放 -> 提交参数错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "设备离线", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "网络超时", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		ithkDeviceManager.setDeleteDeviceListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (progressDialog!=null){
					
					progressDialog.dismiss();
					
				}
				
				if (status == ITHKStatus.kIS_OK){
					Toast.makeText(context, "删除摄像机成功", Toast.LENGTH_SHORT).show();
					
					finish();
				} else if (status == ITHKStatus.kIS_ErrorCode){
					Toast.makeText(context, "删除摄像机 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
				} else if (status == ITHKStatus.kIS_Error){
					Toast.makeText(context, "删除摄像机 -> 提交信息错误", Toast.LENGTH_SHORT).show();
				} else if (status == ITHKStatus.kIS_NoDev){
					Toast.makeText(context, "删除摄像机 -> 设备不存在", Toast.LENGTH_SHORT).show();
				}else if (status == ITHKStatus.kIS_Timeout){
					Toast.makeText(context, "删除摄像机 -> 网络超时", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll) {
			finish();
		}

		if (v == name_ll) {
//			ithkDeviceManagerchangeDeviceNameForSid
			ithkDeviceManager.setChangeDevRemarkListener(new ITHKStatusListener() {
				
				@Override
				public void ithkStatus(int status) {
					// TODO Auto-generated method stub
					if (status == ITHKStatus.kIS_OK){
						//修改成功
					}else if (status == ITHKStatus.kIS_ErrorCode){
						//厂商代码错误
					}else if (status == ITHKStatus.kIS_Error){
						//提交的参数信息错误
					}else if (status == ITHKStatus.kIS_NoDev){
						//摄像机没找到
					}else if (status == ITHKStatus.kIS_Timeout){
						//网络异常
					}
				}
			});
		}

		if (v == status_ll) {

		}

		if (v == led_ll) {
			if (ledStatus.equals("1")){
				ithkDeviceManager.changeDeviceLedStatusForSid(sid, "off");
				ledStatus = "0";
				led_switch.setBackgroundResource(R.drawable.ic_switch_off);
			}else{
				ithkDeviceManager.changeDeviceLedStatusForSid(sid, "on");
				ledStatus = "1";
				led_switch.setBackgroundResource(R.drawable.ic_switch_on);
			}
			
		}

		if (v == voice_ll) {
			if (soundLeadStatus.equals("1")){
				ithkDeviceManager.changeDeviceVoiceGuideForSid(sid, "off");
				soundLeadStatus = "0";
				voice_switch.setBackgroundResource(R.drawable.ic_switch_off);
			}else{
				ithkDeviceManager.changeDeviceVoiceGuideForSid(sid, "on");
				soundLeadStatus = "1";
				voice_switch.setBackgroundResource(R.drawable.ic_switch_on);
			}
		}

		if (v == alarm_ll) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("sid", sid);
			bundle.putString("alarmStatus", alarmStatus);
			bundle.putString("alarmSoundStatus", alarmSoundStatus);
			bundle.putString("alarmStartTime", alarmStartTime);
			bundle.putString("alarmEndTime", alarmEndTime);
			intent.putExtras(bundle);
			intent.setClass(context, AlarmSettingsActivity.class);
			startActivity(intent);
		}
		
		if (v == video_ll) {
			Intent intent = new Intent();
			intent.setClass(context, VideoSettingsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("sid", sid);
			bundle.putString("storeStatus", storeStatus);
			bundle.putString("quality", definition);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
		if (v == subAccount_ll) {
			
			Intent intent = new Intent();
			intent.setClass(context, SubAccountActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("sid", sid);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
		if (v == workModel_ll) {
			Intent intent = new Intent();
			intent.setClass(context, WordModeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("sid", sid);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
		if (v == deleteBtn) {
			showProgressDialog();
			
			ithkDeviceManager.deleteDeviceForSid(sid);
		}

	}
	
	private int startTime = 0;
	private int endTime = 0;
	
	/**
	 * 赋值报警时间段
	 */
	private void initAlarmTime() {
		try {
			if (alarmStartTime != null && !alarmStartTime.equals(""))
				startTime = Integer.parseInt(alarmStartTime);

			if (alarmEndTime != null && !alarmEndTime.equals(""))
				endTime = Integer.parseInt(alarmEndTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String start = (startTime < 10 ? "0" + startTime : startTime) + "";
		String end = (endTime < 10 ? "0" + endTime : endTime) + "";

		if (alarmStatus != null && !alarmStatus.equals("")) {
			if (alarmStatus.equals("0")) {
				alarm_tv.setText("关");
			} else {
				if (alarmStartTime.equals(alarmEndTime)) {
					alarm_tv.setText("全天报警");
				} else {
					alarm_tv.setText((start + ":00") + "~" + (end + ":00"));
				}

			}
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

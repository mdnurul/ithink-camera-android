package com.ithink.demo;

import com.ithink.camera.control.ITHKDeviceManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.view.MyProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class VideoSettingsActivity extends Activity implements OnClickListener{

	private final String TAG = VideoSettingsActivity.class.getSimpleName();

	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;
	
	private ITHKDeviceManager ithkDeviceManager;
	
	/**
	 * 单选按钮组
	 */
	private RadioGroup radioGroup;
	
	/**
	 * 流畅单选按钮
	 */
	private RadioButton smRB;
	/**
	 * 标清单选按钮
	 */
	private RadioButton sdRB;
	/**
	 * 高清单选按钮
	 */
	private RadioButton hdRB;
	
	/**
	 * 视频质量  单选按钮组
	 */
	private View radio_LL;
	
	/**
	 * 设备本地录像开关
	 */
	private View storeSwitch_LL;
	
	/**
	 * 录像开关状态
	 */
	private TextView videoSwitch_txt;
	
	/**
	 * 设备本地录像开关状态
	 */
	private String storeStatus = "1";
	
	
	
	/**
	 * 存储视频清晰度
	 * ；（1,流畅，2，标清，3，高清）
	 */
	private String quality;
	
	private String qualityTemp;
	/**
	 * 摄像头唯一标识码
	 */
	private String sid;
	
	/**
	 * 进度体对话框
	 */
	private MyProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;
		
		setContentView(R.layout.activity_video_settings);
		Bundle bundle = getIntent().getExtras();
		
		if (bundle!=null){
			sid = bundle.getString("sid");
			storeStatus = bundle.getString("storeStatus");
			quality = bundle.getString("quality");
		}
		
		ithkDeviceManager = new ITHKDeviceManager(context);
		
		initViews();
		
		initEvents();
	}
	
	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("录像设置");
		title_left_txt.setText(R.string.back);
		
		storeSwitch_LL = (View) findViewById(R.id.videoSwitch_LL);
		videoSwitch_txt = (TextView) findViewById(R.id.videoSwitch_txt);
		
		radioGroup = (RadioGroup) findViewById(R.id.qualityRadioGroup);
		smRB = (RadioButton) findViewById(R.id.smooth_Radio);
		sdRB = (RadioButton) findViewById(R.id.sd_Radio);
		hdRB = (RadioButton) findViewById(R.id.hd_Radio);
		radio_LL = (View) findViewById(R.id.radio_LL);
		
		if (storeStatus.equals("1"))
		{
			videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_on);
			
		}else{
			videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_off);
			radio_LL.setVisibility(View.GONE);
		}
		
		if (quality.equals("1")){
			smRB.setChecked(true);
		}else if ((quality.equals("2"))){
			sdRB.setChecked(true);
		}else if((quality.equals("3"))){
			hdRB.setChecked(true);
		}
	}
	
	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		storeSwitch_LL.setOnClickListener(this);
		ithkDeviceManager.setChangeLocalDeviceStoreStatusListener(new ITHKStatusListener() {
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					
				}else{
					if (storeStatus.equals("1")){
						storeStatus = "0";
						videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_off);
						radio_LL.setVisibility(View.GONE);
					}else{
						storeStatus = "1";
						videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_on);
						radio_LL.setVisibility(View.VISIBLE);
					}
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "录像开关  -> 厂商代码错误", Toast.LENGTH_SHORT).show();
					}else if(status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "录像开关  -> 提交参数信息错误", Toast.LENGTH_SHORT).show();
					}else if(status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "录像开关  -> 摄像机离线", Toast.LENGTH_SHORT).show();
					}else if(status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "录像开关  -> 网络超时", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == smRB.getId()){
					//流畅
					showProgressDialog();
					qualityTemp = "1";
					ithkDeviceManager.changeDeivceLocalStoreQualityForSid(sid, 1);
				}else if (checkedId == sdRB.getId()){
					//标清
					showProgressDialog();
					qualityTemp = "2";
					ithkDeviceManager.changeDeivceLocalStoreQualityForSid(sid, 2);
				}else if (checkedId == hdRB.getId()){
//					Toast.makeText(context, "高清", Toast.LENGTH_SHORT).show();
					//打开本地视频存储开关
					showProgressDialog();
					qualityTemp = "3";
					ithkDeviceManager.changeDeivceLocalStoreQualityForSid(sid, 3);
				}
			}
		});
		
		ithkDeviceManager.setChangeLocalDeviceStoreQualityListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (progressDialog!=null)
					progressDialog.dismiss();
				
				if (status == ITHKStatus.kIS_OK){
					quality = qualityTemp;
				}else{
					if (storeStatus.equals("1")){
						storeStatus = "0";
						videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_off);
					}else{
						storeStatus = "1";
						videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_on);
					}
					
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "录像清晰度  -> 厂商代码错误", Toast.LENGTH_SHORT).show();
					}else if(status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "录像清晰度  -> 提交参数信息错误", Toast.LENGTH_SHORT).show();
					}else if(status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "录像清晰度  -> 摄像机离线", Toast.LENGTH_SHORT).show();
					}else if(status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "录像清晰度  -> 网络超时", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
		
		if (v == storeSwitch_LL){
			if (storeStatus.equals("1")){
				//关闭本地视频存储开关
				storeStatus = "0";
				
				ithkDeviceManager.changeDeivceLocalStoreStatusForSid(sid, "off");
				videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_off);
				radio_LL.setVisibility(View.GONE);
			}else{
				//打开本地视频存储开关
				storeStatus = "1";
				ithkDeviceManager.changeDeivceLocalStoreStatusForSid(sid, "on");
				videoSwitch_txt.setBackgroundResource(R.drawable.ic_switch_on);
				radio_LL.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 显示进度条
	 */
	private void showProgressDialog() {
		if (progressDialog==null)
			progressDialog = new MyProgressDialog(context);
		
		progressDialog.show();

	}
}

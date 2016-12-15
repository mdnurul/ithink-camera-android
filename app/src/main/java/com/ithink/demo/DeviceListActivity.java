package com.ithink.demo;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ithink.bean.DeviceBean;
import com.ithink.bean.IthinkBean;
import com.ithink.camera.control.ITHKDeviceManager;
import com.ithink.camera.control.ITHKPushMessage;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.camera.control.ITHKVideoView;
import com.ithink.view.MyProgressDialog;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 摄像机列表
 * @author Vine
 *
 */
public class DeviceListActivity extends Activity implements OnClickListener{

	private final String TAG = DeviceListActivity.class.getSimpleName();
	
	private Context context;
	
	private MyProgressDialog progressDialog;
	
	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;
	private TextView title_right_txt;

	private View title_left_ll;
	private View title_right_ll;
	
	private ITHKDeviceManager itkDeivceManager;
	private List<Map<String, Object>> list;
	private List<DeviceBean> deviceInfoBeanList;
	
	private ListView myListView;
	private TextView tip_tv;
	
	private DeviceInfoAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_device_list);
		
		itkDeivceManager = new ITHKDeviceManager(context);
		// 友盟推送统计
				
		showProgressDialog();
		
		initViews();
		
		initEvents();
		
	}

	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_right_ll = (View) findViewById(R.id.title_right_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);
		title_right_txt = (TextView) findViewById(R.id.title_right_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText(R.string.device_list);
		title_left_txt.setText(R.string.back);
		title_right_txt.setText(R.string.refresh);
		
		myListView = (ListView) findViewById(R.id.device_listView);
		tip_tv = (TextView) findViewById(R.id.tip_tv);
	}
	
	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		title_right_ll.setOnClickListener(this);
		
		itkDeivceManager.setGetDeivceListListener(new ITHKStatusListener() {
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if( progressDialog !=null && progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				
				if (status == ITHKStatus.kIS_OK){
					
					System.out.println("摄像机列表->"+itkDeivceManager.deviceListJsonChar);
					IthinkBean ithinkBean = JSON.parseObject(itkDeivceManager.deviceListJsonChar, IthinkBean.class);
					deviceInfoBeanList = ithinkBean.getDeviceList();
					
					if (deviceInfoBeanList == null || deviceInfoBeanList.size() == 0){
						tip_tv.setVisibility(View.VISIBLE);
						myListView.setVisibility(View.GONE);
					}else{
						tip_tv.setVisibility(View.GONE);
						myListView.setVisibility(View.VISIBLE);
						getListData();
					}
				}else if (status == ITHKStatus.kIS_Timeout){
					Toast.makeText(context, "Get List -> Network Timeout", Toast.LENGTH_SHORT).show();
				}else if (status == ITHKStatus.kIS_NoUsr){
					Toast.makeText(context, "Get list -> user name does not exist", Toast.LENGTH_SHORT).show();
				}else if (status == ITHKStatus.kIS_Error){
					Toast.makeText(context, "Get List -> Submit Parameter Info Error", Toast.LENGTH_SHORT).show();
				}else if (status == ITHKStatus.kIS_ErrorCode){
					Toast.makeText(context, "Get List -> Partner Vendor Code Error", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Map<String, ?> map = (Map<String, ?>) (list.get(position));
				
				String sid = (String) map.get("seriaNumber");
				String devName = (String) map.get("deviceName");
				
				ITHKVideoView itkVideoView = new ITHKVideoView(context);
				
				itkVideoView.playRealTimeVideoOfDevice(sid, devName, "1.1");
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
		
		if (v == title_right_ll){
			itkDeivceManager.getDeviceListPage(1, 100);
			showProgressDialog();
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
	
	private void getListData(){
		list = new ArrayList<Map<String, Object>>();
		
		if (deviceInfoBeanList == null || deviceInfoBeanList.size() == 0) {
			return ;
		}
		
		for (int i = 0; i < deviceInfoBeanList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			/**
			 * 设备名称
			 */
			String devName = deviceInfoBeanList.get(i).getName();
			/**
			 * 设备唯一标识
			 */
			String devSid = deviceInfoBeanList.get(i).getSid();
			/**
			 * 设备LED灯开关状态
			 */
			String ledStatus = deviceInfoBeanList.get(i).getLedStatus();
			/**
			 * 设备语音播放开关状态
			 */
			String soundLeadStatus = deviceInfoBeanList.get(i)
					.getSoundLeadStatus();
			/**
			 * 报警开关状态
			 */
			String alarmStatus = deviceInfoBeanList.get(i).getAlarmDic().getAlarmStatus();
			
			/**
			 * 报警声音开关状态
			 */
			String alarmSoundStatus = deviceInfoBeanList.get(i).getAlarmDic().getAlarmSoundStatus();
			/**
			 * 开始报警时间
			 */
			String alarmStartTime = deviceInfoBeanList.get(i).getAlarmDic().getAlarmStartTime();
			/**
			 * 结束报警时间
			 */
			String alarmEndTime = deviceInfoBeanList.get(i).getAlarmDic().getAlarmEndTime();
			/**
			 * 设备固件
			 */
			String devVer = deviceInfoBeanList.get(i).getVersion();
			/**
			 * 设备内网IP
			 */
			String innerIP = deviceInfoBeanList.get(i).getInnerIP();
			/**
			 * 设备mac地址
			 */
			String mac = deviceInfoBeanList.get(i).getMacAddress();
			/**
			 * 当前用户是否为该设备的管理员
			 */
			int init = deviceInfoBeanList.get(i).getInit();
			/**
			 * 摄像头状态 0 离线 1在线
			 */
			int status = deviceInfoBeanList.get(i).getStatus();
			/**
			 * 设备类型
			 */
			String type = deviceInfoBeanList.get(i).getType(); 
			/**
			 * 设备是否支持报警，0否1是
			 */
			String showAlarm = deviceInfoBeanList.get(i).getShowAlarm();
			/**
			 * 设备是否支持离线存储 0否1是
			 */
			String showOffLineModel = deviceInfoBeanList.get(i)
					.getShowOffLineModel();
			/**
			 * 是否开启了离线存储模式 0否1是
			 */
			String offLineModeStatus = deviceInfoBeanList.get(i).getWorkModeDic()
					.getOffLineModeStatus();
			/**
			 * 离线视频存储清晰度（1：流畅；2：标清；3：高清）
			 */
			String definition = deviceInfoBeanList.get(i).getWorkModeDic().getDefinition();
			/**
			 * 获取设备预览图的下载地址
			 */
			String img =deviceInfoBeanList.get(i).getImg();
			/**
			 * 设备sd状态
			 */
			String sdcardStatus = deviceInfoBeanList.get(i).getMemCardDic().getSdcardStatus();
			/**
			 * 设备SD卡总共容量（单位：byte）
			 */
			String sdcardTotal = deviceInfoBeanList.get(i).getMemCardDic().getSdcardTotal();
			/**
			 * 设备SD卡可用容量（单位：byte）
			 */
			String sdcardAvailable = deviceInfoBeanList.get(i).getMemCardDic().getSdcardAvailable();
			
			
			/**
			 * 摄像机录像开关状态
			 */
			String storeStatus = deviceInfoBeanList.get(i).getStoreStatus();
			
			/**
			 * 是否显示录像功能
			 */
			String showLocalStore = deviceInfoBeanList.get(i).getShowLocalStore();
			/**
			 * 摄像机备注
			 */
			String remark = deviceInfoBeanList.get(i).getRemark();
			
			map.put("img", img);
			map.put("deviceName", devName);
			map.put("seriaNumber", devSid);
			map.put("status", status+"");
			map.put("ledStatus", ledStatus);
			map.put("soundLeadStatus", soundLeadStatus);
			map.put("alarmStatus", alarmStatus);
			map.put("alarmStartTime", alarmStartTime);
			map.put("alarmSoundStatus", alarmSoundStatus);
			map.put("alarmEndTime", alarmEndTime);
			map.put("devVer", devVer);
			map.put("innerIP", innerIP);
			map.put("mac", mac);
			map.put("init", init + "");
			map.put("type", type);
			map.put("showAlarm", showAlarm);
			map.put("showOffLineModel", showOffLineModel);
			map.put("offLineModeStatus", offLineModeStatus);
			map.put("definition", definition);
			map.put("sdcardStatus", sdcardStatus);
			map.put("sdcardTotal", sdcardTotal);
			map.put("sdcardAvailable", sdcardAvailable);
			map.put("storeStatus", storeStatus);
			map.put("showLocalStore", showLocalStore);
			map.put("devRemark", remark);

			System.out.println("alarmStatus->"+alarmStatus);
			
			list.add(map);
		}
		
		adapter= new DeviceInfoAdapter(this, list, R.layout.device_list_layout, myListView);
		
		myListView.setAdapter(adapter);
	}
	
	public class DeviceInfoAdapter extends BaseAdapter {
		protected LayoutInflater mInflater;
		private ListView mgridView;
		private Context context;
		private List<? extends Map<String, ?>> mData;
		private int mResource;
		
		int div = 0;
		public DeviceInfoAdapter(Context cn, List<? extends Map<String, ?>> data, int resource, ListView g) {
			this.mData = data;
			this.mResource = resource;
			this.mgridView = g;
			this.mInflater = LayoutInflater.from(cn);
			this.context = cn;

			if (data.size()>8){
				div = 4;
			}else{
				div = 2;
			}
		}

		@Override
		public int getCount() {

			return mData.size();
		}

		@Override
		public Object getItem(int position) {

			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		public void setLayoutResource(int resource) {
			mResource = resource;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, null);
				holder = new ViewHolder();
				holder.mImageView = (ImageView) convertView.findViewById(R.id.img);
				holder.mTextView = (TextView) convertView.findViewById(R.id.deviceName);
				holder.mButtonVideo = (View) convertView.findViewById(R.id.home_list_video_btn);
				holder.mButtonSettings = (View)convertView.findViewById(R.id.home_list_settings_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String devSid = mData.get(position).get("seriaNumber").toString();
			String name= mData.get(position).get("deviceName").toString();
			String status = mData.get(position).get("status").toString();
			String img = mData.get(position).get("img").toString();
			
//			holder.mImageView.setImageURI(Uri.parse(img));
			holder.mTextView.setText(name);
			
			holder.mButtonVideo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Map<String, ?> map = (Map<String, ?>) (mData.get(position));
					
					String sid = (String) map.get("seriaNumber");
					
					
					Intent intent = new Intent();
					intent.setClass(context, PlayBackListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("sid", sid);
					intent.putExtras(bundle);
					startActivity(intent);
					

				}
			});
			
			holder.mButtonSettings.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Map<String, ?> map = (Map<String, ?>) (mData.get(position));
					
					String sid = (String) map.get("seriaNumber");
					String devName = (String) map.get("deviceName");
					String devRemark = (String) map.get("devRemark").toString()
							.replace("备注：", "");
					String devVer = (String) map.get("devVer");
					String ledStatus = (String) map.get("ledStatus");
					String soundLeadStatus = (String) map.get("soundLeadStatus");
					String alarmStatus = (String) map.get("alarmStatus");
					String alarmSoundStatus = (String) map.get("alarmSoundStatus");
					String alarmStartTime = (String) map.get("alarmStartTime");
					String alarmEndTime = (String) map.get("alarmEndTime");
					String status = (String) map.get("deviceStatus");
					String innerIP = (String) map.get("innerIP");
					String mac = (String) map.get("mac");
					String init = (String) map.get("init");
					String type = (String) map.get("type");
					String showAlarm = (String) map.get("showAlarm");
					/**
					 * 是否显示离线存储功能 0否 1是
					 */
					String showOffLineModel = (String) map.get("showOffLineModel");
					/**
					 * 是否开启了离线存储 0否 1是
					 */
					String offLineModeStatus = (String) map.get("offLineModeStatus");
					/**
					 * 离线视频存储清晰度（1：流畅；2：标清；3：高清）
					 */
					String definition = (String) map.get("definition");
					/**
					 * 设备sd状态
					 */
					String sdcardStatus = (String) map.get("sdcardStatus");
					/**
					 * 设备sd卡总共容量 （单位：byte）
					 */
					String sdcardTotal = (String) map.get("sdcardTotal");
					/**
					 * 设备sd卡可用容量 （单位：byte）
					 */
					String sdcardAvailable = (String) map.get("sdcardAvailable");
					String storeStatus = (String) map.get("storeStatus");
					
					/**
					 * 是否显示视频录像功能
					 */
					String showLocalStore = (String) map.get("showLocalStore");
					
					Intent intent = new Intent();
					intent.setClass(context, DeviceInfoActivity.class);
					Bundle bundle = new Bundle();
					
					bundle.putString("sid", sid);
					bundle.putString("name", devName);
					bundle.putString("remark", devRemark);
					bundle.putString("ver", devVer);
					bundle.putString("status", status);
					bundle.putString("ledStatus", ledStatus);
					bundle.putString("soundLeadStatus", soundLeadStatus);
					bundle.putString("alarmStatus", alarmStatus);
					bundle.putString("alarmSoundStatus", alarmSoundStatus);
					bundle.putString("alarmStartTime", alarmStartTime);
					bundle.putString("alarmEndTime", alarmEndTime);
					bundle.putString("innerIP", innerIP);
					bundle.putString("mac", mac);
					bundle.putInt("item", position);
					bundle.putString("init", init);
					bundle.putString("type", type);
					bundle.putString("showAlarm", showAlarm);
					bundle.putString("showOffLineModel", showOffLineModel);
					bundle.putString("offLineModeStatus", offLineModeStatus);
					bundle.putString("definition", definition);
					bundle.putString("sdcardStatus", sdcardStatus);
					bundle.putString("sdcardTotal", sdcardTotal);
					bundle.putString("sdcardAvailable", sdcardAvailable);
					bundle.putString("storeStatus", storeStatus);
					bundle.putString("showLocalStore", showLocalStore);
					
					intent.putExtras(bundle);
					startActivity(intent);
					
//					SettingClick(map, position);
				}
			});
			return convertView;
		}
		
		public class ViewHolder {
			private ImageView mImageView;
			private TextView mTextView;
			private View mButtonVideo;
			private View mButtonSettings;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		itkDeivceManager.getDeviceListPage(1, 100);
	}
}

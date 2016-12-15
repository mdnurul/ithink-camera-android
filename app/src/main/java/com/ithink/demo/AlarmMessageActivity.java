package com.ithink.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ithink.bean.AlarmMsgBean;
import com.ithink.bean.IthinkBean;
import com.ithink.camera.control.ITHKDownloadStatusListener;
import com.ithink.camera.control.ITHKMessageManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.camera.control.ITHKVideoView;
import com.ithink.demo.DeviceListActivity.DeviceInfoAdapter;
import com.ithink.demo.DeviceListActivity.DeviceInfoAdapter.ViewHolder;
import com.ithink.view.MyProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmMessageActivity extends Activity implements OnClickListener {

	private final String TAG = AlarmMessageActivity.class.getSimpleName();

	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;
	private TextView title_right_txt;

	private View title_left_ll;

	private ListView alarmMsgListView;

	private ITHKMessageManager ithkMsgManager;

	private List<AlarmMsgBean> alarmBeanList;

	private MyProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;

		setContentView(R.layout.activity_alarm_message);

		ithkMsgManager = new ITHKMessageManager(context);

		initViews();

		initEvents();

		showProgressDialog();
		ithkMsgManager.getAlarmMsg(1, 100);
	}

	private void initViews() {

		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);
		title_right_txt = (TextView) findViewById(R.id.title_right_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("报警消息");
		title_left_txt.setText(R.string.back);
		title_right_txt.setText("刷新");

		alarmMsgListView = (ListView) findViewById(R.id.alarmMsgListView);

	}

	private void initEvents() {
		title_left_ll.setOnClickListener(this);
		title_right_txt.setOnClickListener(this);

		ithkMsgManager.setGetAlarmMsgListener(new ITHKStatusListener() {

			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub

				if (progressDialog != null)
					progressDialog.dismiss();

				if (status == ITHKStatus.kIS_OK) {
					IthinkBean ithinkBean = JSON.parseObject(
							ithkMsgManager.alarmMessageListJsonChar,
							IthinkBean.class);
					alarmBeanList = ithinkBean.getAlarmList();
					Log.e(TAG, "报警消息->"
							+ ithkMsgManager.alarmMessageListJsonChar);
					getData();
				} else {
					if (status == ITHKStatus.kIS_ErrorCode) {
						Toast.makeText(context, "获取报警消息-> 厂商代码错误",
								Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Error) {
						Toast.makeText(context, "获取报警消息-> 提交的参数信息错误",
								Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_NoUsr) {
						Toast.makeText(context, "获取报警消息-> 用户不存在",
								Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Timeout) {
						Toast.makeText(context, "获取报警消息-> 网络超时",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		alarmMsgListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Map<String, ?> map = list.get(position);

				String sid = map.get("sid").toString();
				String alarmTime = map.get("alarmTime").toString();

				ITHKVideoView ithkVideoView = new ITHKVideoView(context);
				ithkVideoView.playAlarmVideoForDevice(sid, sid, "1.1",
						alarmTime);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll) {
			finish();
		}

		if (v == title_right_txt) {
			showProgressDialog();
			ithkMsgManager.getAlarmMsg(1, 100);
		}
	}

	private List<Map<String, Object>> list;

	private void getData() {
		list = new ArrayList<Map<String, Object>>();

		if (alarmBeanList == null || alarmBeanList.size() == 0)
			return;

		for (int i = 0; i < alarmBeanList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			String alarmId = alarmBeanList.get(i).getAlarmID();
			String alarmTime = alarmBeanList.get(i).getAlarmTime();
			String img = alarmBeanList.get(i).getDevInfo().getImg();
			String name = alarmBeanList.get(i).getDevInfo().getName();
			int ower = alarmBeanList.get(i).getDevInfo().getInit();
			String sid = alarmBeanList.get(i).getDevInfo().getSid();
			int deviceStatus = alarmBeanList.get(i).getDevInfo().getStatus();
			String version = alarmBeanList.get(i).getDevInfo().getVersion();
			int isDownload = alarmBeanList.get(i).getIsDownload();
			int isRead = alarmBeanList.get(i).getIsRead();

			map.put("alarmId", alarmId);
			map.put("alarmTime", alarmTime);
			map.put("img", img);
			map.put("name", name);
			map.put("ower", ower);
			map.put("sid", sid);
			map.put("deviceStatus", deviceStatus);
			map.put("deviceStatus", deviceStatus);
			map.put("isDownload", isDownload);
			map.put("isRead", isRead);

			list.add(map);
		}

		AlarmMsgInfoAdapter adapter = new AlarmMsgInfoAdapter(this, list,
				R.layout.alarm_msg_list_layout);

		alarmMsgListView.setAdapter(adapter);
	}

	private void showProgressDialog() {
		progressDialog = new MyProgressDialog(context);
		progressDialog.show();
		progressDialog.setProgressBarGone(View.VISIBLE);
		progressDialog.changeDialogTitle("");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);
	}

	public class AlarmMsgInfoAdapter extends BaseAdapter {
		protected LayoutInflater mInflater;
		private Context context;
		private List<? extends Map<String, ?>> mData;
		private int mResource;

		int div = 0;

		public AlarmMsgInfoAdapter(Context cn,
				List<? extends Map<String, ?>> data, int resource) {
			this.mData = data;
			this.mResource = resource;
			this.mInflater = LayoutInflater.from(cn);
			this.context = cn;

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, null);
				holder = new ViewHolder();
				holder.alarmId_tv = (TextView) convertView
						.findViewById(R.id.alarm_id_tv);
				holder.alarm_time_tv = (TextView) convertView
						.findViewById(R.id.alarm_time_tv);
				holder.alarm_name_tv = (TextView) convertView
						.findViewById(R.id.alarm_name_tv);

				holder.down_btn = (Button) convertView
						.findViewById(R.id.down_video_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final String alarmId = mData.get(position).get("alarmId")
					.toString();
			final String sid = mData.get(position).get("sid").toString();
			String time = mData.get(position).get("alarmTime").toString();
			
			String name = mData.get(position).get("name").toString();
			
			int isDownload = Integer.parseInt(mData.get(position)
					.get("isDownload").toString());

			final String date = mData.get(position).get("alarmTime").toString()
					.replace("-", "").replace(" ", "").replace(":", "");

			holder.alarm_time_tv.setText(time);
			holder.alarm_name_tv.setText(name);
			holder.alarmId_tv.setText(alarmId);
			if (isDownload == 0) {
				holder.down_btn.setVisibility(View.INVISIBLE);
			} else if (isDownload == 1) {
				holder.down_btn.setVisibility(View.VISIBLE);
			}
			holder.down_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ithkMsgManager.downloadAlarmVideo(sid, alarmId, date);
					ithkMsgManager
							.setDownloadAlarmVideoListener(new ITHKDownloadStatusListener() {

								@Override
								public void ithkDownloadStatus(int status,
										String alarmId, int progress) {
									// TODO Auto-generated method stub
									if (status == ITHKStatus.kIS_OK) {
										// 请求成功，开始下载
										Toast.makeText(context, "请求成功，开始下载", Toast.LENGTH_SHORT).show();
									} else if (status == ITHKStatus.kIS_ErrorCode) {
										// 合作厂商代码错误
									} else if (status == ITHKStatus.kIS_Error) {
										// 提交参数错误
									} else if (status == ITHKStatus.kIS_NoDev) {
										// 没找到摄像机
									} else if (status == ITHKStatus.kIS_OffLine) {
										// 摄像机离线
									}else if (status == ITHKStatus.kIS_NoVideo) {
										// 没找到视频文件
										Toast.makeText(context, "没找到视频文件", Toast.LENGTH_SHORT).show();
									}else if (status == ITHKStatus.kIS_Dev_TimeOut) {
										// 连接摄像机超时
										Toast.makeText(context, "连接摄像机超时", Toast.LENGTH_SHORT).show();
									} if (status == ITHKStatus.kIS_NoSDCard) {
										// 摄像机没有插入SD卡（内存卡）
										Toast.makeText(context, "摄像机没有插入SD卡", Toast.LENGTH_SHORT).show();
									}else if (status == ITHKStatus.kIS_NoRel) {
										// 没有权限 （子账号没有权限）
										Toast.makeText(context, "没有权限", Toast.LENGTH_SHORT).show();
									} else if (status == ITHKStatus.REQUEST_DOWN_PROGRESS) {
										// 报警消息ID alarmId
										// progress 下载进度
									} else if (status == ITHKStatus.REQUEST_DOWN_FINISH) {
										// 下载完成
										// 报警消息ID alarmId
										// progress 下载进度
										Toast.makeText(context, alarmId+"--下载完成", Toast.LENGTH_SHORT).show();
									}
								}
							});
				}
			});

			return convertView;
		}

		public class ViewHolder {
			private TextView alarmId_tv;
			private TextView alarm_name_tv;
			private TextView alarm_time_tv;
			private Button down_btn;
		}

	}
}

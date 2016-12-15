package com.ithink.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ithink.bean.IthinkBean;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.camera.control.ITHKVideoView;
import com.ithink.view.MyProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PlayBackListActivity extends Activity implements OnClickListener{
	
	private final static String TAG = PlayBackListActivity.class
			.getSimpleName();
	
	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;
	private TextView title_right_txt;

	private View title_left_ll;
	
	private MyProgressDialog progressDialog; // 进度对话框
	
	private String sid;

	private ListView listView;
	
	private ITHKVideoView ithkVideoView;
	
	private String listStr;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题,
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态栏，充满全屏

		context = this;
		
		setContentView(R.layout.activity_play_back_list);
		
		ithkVideoView = new ITHKVideoView(context);
		
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			sid = bundle.getString("sid");
		}

		initViews();
		
		initEvents();
		
		showProgressDialog();
		ithkVideoView.getPlayBackListWithSid(sid);
	}
	
	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);
		title_right_txt = (TextView) findViewById(R.id.title_right_txt);
		
		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("视频回放列表");
		title_left_txt.setText(R.string.back);
		title_right_txt.setText("刷新");
		
		listView = (ListView) findViewById(R.id.listview);
		
		
	}
	
	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		title_right_txt.setOnClickListener(this);
		
		ithkVideoView.setGetPlayBackListListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				
				if (progressDialog!=null)
					progressDialog.dismiss();
				
				if (status == ITHKStatus.kIS_OK){
					IthinkBean ithinkBean = JSON.parseObject(ithkVideoView.playbackListJsonChar, IthinkBean.class);
					
					listStr = ithinkBean.getPlayBackList();
					
					Log.e(TAG, "视频回放列表->"+ithkVideoView.playbackListJsonChar);
					getData();
					
				}else {
					if (status == ITHKStatus.kIS_ErrorCode){
						
						Toast.makeText(context, "录像回放列表 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "录像回放列表 -> 提交参数错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_NoSDCard){
						Toast.makeText(context, "录像回放列表 -> 摄像机没有内存卡", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_NoList){
						Toast.makeText(context, "录像回放列表 -> 摄像机暂时没有回放视频", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "设备离线", Toast.LENGTH_SHORT).show();
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
		
		if (v == title_right_txt){
			showProgressDialog();
			ithkVideoView.getPlayBackListWithSid(sid);
		}
	}
	
	private void showProgressDialog() {
		progressDialog = new MyProgressDialog(context);
		progressDialog.show();
		progressDialog.setProgressBarGone(View.VISIBLE);
		progressDialog.changeDialogTitle("");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Map<String, ?> map = (Map<String, ?>) myData.get(position);
				String date = (String) map.get("date");
				ithkVideoView.playPlaybackVideoForDevice(sid, sid, "1.0", date);
			}
		});
	}

	private List<Map<String, Object>> myData;

	private List<Map<String, Object>> getData() {

		myData = new ArrayList<Map<String, Object>>();
		if (listStr == null)
			return myData;
		String prefix[] = listStr.split(",");
		int len = prefix.length;

		for (int i = 0; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", prefix[i]);
			myData.add(map);
		}

		listView.setAdapter(new SimpleAdapter(this, myData,
				android.R.layout.simple_list_item_1, new String[] { "date" },
				new int[] { android.R.id.text1 }));
		return myData;

	}

}

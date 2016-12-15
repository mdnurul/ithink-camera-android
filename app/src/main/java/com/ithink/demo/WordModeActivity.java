package com.ithink.demo;

import com.ithink.camera.control.ITHKDeviceManager;
import com.ithink.camera.control.ITHKSWBindManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工作模式
 * @author Vine
 *
 */
public class WordModeActivity extends Activity implements OnClickListener{

	private final String TAG = WordModeActivity.class.getSimpleName();

	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;
	
	private Button net_mode_btn;
	private Button offLine_mode_btn;
	
	private ITHKDeviceManager ithkDeviceManager;
	private ITHKSWBindManager ithkSWManager;
	private String sid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_word_mode);
		
		ithkDeviceManager = new ITHKDeviceManager(context);
		ithkSWManager = new ITHKSWBindManager(context);
		
		Bundle bundle = getIntent().getExtras();
		sid = bundle.getString("sid");
		
		
		initViews();
		
		initEvents();
	}
	
	
	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("工作模式");
		title_left_txt.setText(R.string.back);
		
		net_mode_btn = (Button) findViewById(R.id.net_mode_btn);
		offLine_mode_btn = (Button) findViewById(R.id.offLine_mode_btn);
	}
	
	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		net_mode_btn.setOnClickListener(this);
		offLine_mode_btn.setOnClickListener(this);
		
		ithkDeviceManager.setOpenOfflineModeListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (status == ITHKStatus.kIS_OK){
					offLine_mode_btn.setEnabled(false);
					Toast.makeText(context, "切到到断网模式成功", Toast.LENGTH_SHORT).show();
				}else{
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "切到到断网模式 ->  合作厂商代码错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "切到到断网模式 ->  提交参数信息错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_OffLine){
						Toast.makeText(context, "切到到断网模式 ->  摄像机离线", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_NoSDCard){
						Toast.makeText(context, "切到到断网模式 ->  摄像机没有插入内存卡", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "切到到断网模式 ->  网络超时", Toast.LENGTH_SHORT).show();
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
		
		if (v == offLine_mode_btn){
			ithkDeviceManager.openOfflineModeForSid(sid, 1);
		}
		
		if (v == net_mode_btn){
			ithkSWManager.playSoundWaveToNetMode(handler);
		}
		
	}
	
	private Handler handler = new Handler(){
		
	};

}

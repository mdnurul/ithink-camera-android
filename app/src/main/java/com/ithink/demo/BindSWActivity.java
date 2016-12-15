package com.ithink.demo;

import com.ithink.camera.control.ITHKSWBindManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 声波绑定
 * @author Vine
 *
 */
public class BindSWActivity extends Activity implements OnClickListener{

	private final String TAG = BindSWActivity.class.getSimpleName();
	
	private Context context;
	
	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;
	
	private Button sw_btn;
	private TextView tip_tv;
	
	private ITHKSWBindManager ithkSWBind;
	
	private String wifi_name = "";
	private String wifi_pass = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_bind_sw);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle!=null){
			wifi_name = bundle.getString("name");
			wifi_pass = bundle.getString("pass");
			
		}
		
		initViews();
		
		initEvents();
		
		ithkSWBind = new ITHKSWBindManager(context);
	}

	
	private void initViews() {

		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		
		
		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText(R.string.bind_sw);
		title_left_txt.setText(R.string.back);
		
		tip_tv = (TextView) findViewById(R.id.tip_tv);
		
		tip_tv.setText("Please turn up the volume of the phone，Close to your camcorder, and click the Send Sonic button");
		
		sw_btn = (Button) findViewById(R.id.sw_btn);
		
		
	}

	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		sw_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
		
		if (v == sw_btn){
			
			ithkSWBind.playSoundWaveWithSSID(wifi_name, wifi_pass, handler);
			
			ithkSWBind.startSWCheckBindStartStatus();
			ithkSWBind.setCheckVoiceBindStatusListener(new ITHKStatusListener() {
				
				@Override
				public void ithkStatus(int status) {
					// TODO Auto-generated method stub
					System.out.println("Binding Status"+status);
					if (status == ITHKStatus.kIS_OK){
						Toast.makeText(context, "Bind successfully", Toast.LENGTH_SHORT).show();
						tip_tv.setTextSize(24);
						tip_tv.setText("Bind successfully");
					}else if (status == ITHKStatus.kIS_Wait){
						Toast.makeText(context, "Detection of binding state", Toast.LENGTH_SHORT).show();
						
					}else if (status == ITHKStatus.kIS_Binded){
						Toast.makeText(context, "Has been bound", Toast.LENGTH_SHORT).show();
						tip_tv.setTextSize(24);
						tip_tv.setText("Has been bound");
					}else if (status == ITHKStatus.kIS_NonSupport){
						Toast.makeText(context, "Unsupported camera type", Toast.LENGTH_SHORT).show();
						tip_tv.setTextSize(24);
						tip_tv.setText("Unsupported camera type");
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "network timeout", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_ErrorCode){
						//合作厂商代码错误
					}else if (status == ITHKStatus.kIS_Error){
						//提交参数信息错误
					}
				}
			});
		}
	}
	
	private Handler  handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case ITHKStatus.ITK_PLAYVOICE_PROGRESS:
				int pro = msg.arg1;
				Log.e(TAG, "The time (in milliseconds) of events required to play the sound wave"+pro);
				break;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (ithkSWBind!=null){
			ithkSWBind.stopSoundWaveWithSSID();
			ithkSWBind.stopSWCheckBindStartStatus();
		}
	}
}

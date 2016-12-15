package com.ithink.demo;

import com.ithink.camera.control.ITHKQRBindManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 二维码绑定
 * @author Vine
 *
 */
public class BindQRActivity extends Activity implements OnClickListener{

	private final String TAG = BindQRActivity.class.getSimpleName();
	
	private Context context;
	
	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;
	
	private ITHKQRBindManager ithkQRBind;
	
	private ImageView qr_img;
	
	private TextView tip_tv;
	
	private String wifi_name = "";
	private String wifi_pass = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_bind_qr);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle!=null){
			wifi_name = bundle.getString("name");
			wifi_pass = bundle.getString("pass");
			
		}
		
		ithkQRBind = new ITHKQRBindManager(context);
		
		initViews();
		
		initEvents();
		
		ithkQRBind.startQRCheckBindStartStatus();
	}


	private void initViews() {

		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText(R.string.bind_qr);
		title_left_txt.setText(R.string.back);
		
		qr_img = (ImageView) findViewById(R.id.qr_img);
		
		tip_tv = (TextView) findViewById(R.id.tip_tv);
		
		tip_tv.setText("With the camera scan phone two-dimensional code. Heard a drop of sound, scan success");
		
		Bitmap bitmap = ithkQRBind.qrImageForSSID(wifi_name, wifi_pass);
		if (bitmap!=null)
			qr_img.setImageBitmap(bitmap);
	}

	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		
		ithkQRBind.setCheckQRBindStatusListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				System.out.println("Binding state->"+status);
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (ithkQRBind!=null){
			ithkQRBind.stopQRCheckBindStartStatus();
		}
		
	}
	
}

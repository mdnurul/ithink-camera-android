package com.ithink.demo;

import com.ithink.camera.control.ITHKInitSDK;
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
import android.widget.EditText;

public class UserLoginActivity extends Activity implements OnClickListener{

	private final String TAG = UserLoginActivity.class.getSimpleName();
	
	private Context context;
	private EditText name_edt;
	private EditText pass_edt;
	private Button login_btn;
	
	private MyProgressDialog progressDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_user_login);
		
		initViews();
		
		initEvents();
	}

	private void initViews() {
		name_edt = (EditText) findViewById(R.id.login_name_edt);
		pass_edt = (EditText) findViewById(R.id.login_pass_edt);
		
		name_edt.setText("Vine");
		pass_edt.setText("000000");
		
		login_btn = (Button) findViewById(R.id.login_login_btn);
		
	}

	private void initEvents() {
		login_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == login_btn){
			String name = name_edt.getText().toString();
			String pass = pass_edt.getText().toString();
			
			showProgressDialog();
			
			ITHKInitSDK  initSdk = new ITHKInitSDK(context);
			initSdk.initSDKWithUsername(name, "rentlykeyless");
			initSdk.setITHKInitSDKListener(new ITHKStatusListener() {
				
				@Override
				public void ithkStatus(int status) {
					// TODO Auto-generated method stub
//					getDeviceList()
					if (status == ITHKStatus.kIS_OK){
						//初始化成功
					} else if(status == ITHKStatus.kIS_ErrorCode){
						//厂商代码错误
					}else if(status == ITHKStatus.kIS_Error){
						//提交参数错误
					}
					if (progressDialog!=null && progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					
					Intent intent = new Intent();
					intent.setClass(context, MainActivity.class);
					startActivity(intent);
					finish();
				}
			});
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

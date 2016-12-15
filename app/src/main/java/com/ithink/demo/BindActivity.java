package com.ithink.demo;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BindActivity extends Activity implements OnClickListener{

	private final String TAG = BindActivity.class.getSimpleName();
	
	private Context context;
	
	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;

	private View title_left_ll;
	
	private EditText wifi_name_edt;
	
	private EditText wifi_pass_edt; 
	
	private Button main_sw_bind_btn;
	private Button main_qr_bind_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_bind);
		
		initViews();
		
		initEvents();
		
	}

	private void initViews() {

		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);

		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText(R.string.bind_sw);
		title_left_txt.setText(R.string.back);
		
		wifi_name_edt = (EditText) findViewById(R.id.wifi_name_edt);
		wifi_pass_edt = (EditText) findViewById(R.id.wifi_pass_edt);
		
		main_sw_bind_btn = (Button) findViewById(R.id.main_sw_bind_btn);
		
		main_qr_bind_btn = (Button) findViewById(R.id.main_qr_bind_btn);
		
		wifi_name_edt.setText("Bitfour");
		wifi_pass_edt.setText("badaca11");
		
		
	}
	
	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		main_sw_bind_btn.setOnClickListener(this);
		main_qr_bind_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
		
		if (v == main_sw_bind_btn){
			String wifi_name = wifi_name_edt.getText().toString();
			String wifi_pass = wifi_pass_edt.getText().toString();
			
			if (wifi_name.equals("") && wifi_pass.equals("")){
				Toast.makeText(context, "Enter the password and name", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			Intent intent = new Intent();
			intent.setClass(context, BindSWActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", wifi_name);
			bundle.putString("pass", wifi_pass);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
		if (v == main_qr_bind_btn){
			String wifi_name = wifi_name_edt.getText().toString();
			String wifi_pass = wifi_pass_edt.getText().toString();
			if (wifi_name.equals("") && wifi_pass.equals("")){
				Toast.makeText(context, "Enter the password and name", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			Intent intent = new Intent();
			intent.setClass(context, BindQRActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", wifi_name);
			bundle.putString("pass", wifi_pass);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
}

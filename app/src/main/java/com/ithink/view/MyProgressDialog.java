package com.ithink.view;


import com.ithink.demo.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Vine
 * @version 创建时间：2015-01-23 上午10:59:43 类说明
 */
public class MyProgressDialog extends ProgressDialog {

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public MyProgressDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// progressDialog.setIndeterminate(true);
		// setCancelable(false);
		setCanceledOnTouchOutside(false);

		// progressDialog.show()
		setContentView(R.layout.my_dialog);
	}

	public void showDialog() {
		show();

	}

	public void changeDialogTitle(String content) {
		TextView title = (TextView) findViewById(R.id.mydialog_title);
		title.setTextColor(Color.BLACK);
		// RelativeLayout linear= (RelativeLayout)findViewById(R.id.progress);
		// linear.setBackgroundColor(Color.argb(55, 0, 0, 0)); // /鑳屾櫙閫忔槑搴�
		// linear.setTextColor(Color.argb(80, 255, 255, 255)); // 鏂囧瓧閫忔槑搴�
		title.setText(content);
	}

	public void setProgressBarGone(int v) {
		ProgressBar proBar = (ProgressBar) findViewById(R.id.progressBar);
		proBar.setVisibility(v);
	}
	
	/**
	 * 鏄剧ず杩涘害鏉�
	 */
	public void showProgressDialog() {
		show();
		setCanceledOnTouchOutside(false);
		setCancelable(true);
	}
}
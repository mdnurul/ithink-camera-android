package com.ithink.view;


import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context, int theme, int layoutResID) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.setContentView(layoutResID);
	}

}

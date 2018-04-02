package com.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class VAdminMenuActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	//	按下事件;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
	    }
		return super.onKeyDown(keyCode, event);
	}
}

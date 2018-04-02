package com.view;

import com.hqbs.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VDiagActivity extends Activity implements OnClickListener{
	private RelativeLayout btnback;
	private TextView	   topic,content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_popupwindow_delivery_n);
		initView();
		initEvent();
	}
	private void initView(){
		btnback=(RelativeLayout) findViewById(R.id.btnBack);
		topic	= (TextView) findViewById(R.id.topic);
		content	= (TextView) findViewById(R.id.content);
	}
	private void initEvent(){
		Window 		window 	= getWindow();  
		WindowManager.LayoutParams layoutParams = window.getAttributes();  
		//设置窗口的大小及透明度  
		layoutParams.width  = 500;  
		layoutParams.height = 700;  
		layoutParams.alpha  = 1f;  
		layoutParams.dimAmount = 0.5f;
		window.setAttributes(layoutParams);  
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//	获得相应的事件内容;
		Intent   intent	=	getIntent();
		Bundle   bundle	=   intent.getExtras();
		String   id		=   bundle.getString("id").toString();
		String   string	=   bundle.getString("content").toString();
		topic.setText(id);
		content.setText(string);
		//	添加事件监听;
		btnback.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;

		default:
			break;
		}
	}	
}

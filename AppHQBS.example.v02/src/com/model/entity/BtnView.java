package com.model.entity;

import com.hqbs.app.R;

import android.graphics.Color;
import android.widget.TextView;

public class BtnView {
	private TextView[]  textviews	=new TextView[2];
	private int[]	    backgrounds	={R.drawable.shape_btn1_n,R.drawable.shape_btn0_n};
	private String[]	names		={"2","1"};
	
	public BtnView() {
		super();
	}

	public BtnView(TextView tv0, TextView tv1) {
		super();
		this.textviews[0] = tv0;
		this.textviews[1] = tv1;
	}
	
	public String clickbtn(int location){
		for(int i=0;i<2;i++){
			TextView view= (TextView) textviews[i];
			if(location==i){
				view.setBackgroundResource(backgrounds[0]);
				view.setTextColor(Color.WHITE);
			}else {
				view.setBackgroundResource(backgrounds[1]);
				view.setTextColor(Color.BLACK);
			}
		}
		return names[location];
	}
	
}

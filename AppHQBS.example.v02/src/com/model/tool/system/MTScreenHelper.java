package com.model.tool.system;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class MTScreenHelper {
	private DisplayMetrics metrics;
	private WindowManager  manager;
	private Display		   display;
	private int 		   screenWidth=0;
	private int 		   screenHeight=0;
	
	public MTScreenHelper() {
		if(metrics==null){			
			metrics = new DisplayMetrics();
			display = manager.getDefaultDisplay();
			display.getMetrics(metrics);
		}
	}
	//	屏幕宽度;
	public int getScreenWidth() {
		screenWidth=metrics.widthPixels;
		return screenWidth;
	}
	//	屏幕长度;
	public int getScreenHeight() {
		screenHeight=metrics.heightPixels;
		return screenHeight;
	}
	
}

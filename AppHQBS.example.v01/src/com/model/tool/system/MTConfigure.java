package com.model.tool.system;
import java.io.File;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class MTConfigure {
//	public static String TAG_IP_ADDRESS	="39.106.70.111";
	public static String TAG_IP_ADDRESS	="172.23.120.81";
//	public static String TAG_IP_ADDRESS	="192.168.99.147";
	public static String TAG_PORT		="8888";
	public static String TAG_PROGRAM	="PHQBS.example";
	public static final int 	 NTAG_SUCCESS	=1;
	public static final int 	 NTAG_FAIL		=2;
	/*配置文件的参数*/
	private int screenWidth;
	private int screenHeight;
	/*文件的配置路径*/
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + File.separator + "hqbsFile", saveFolder = "photo",
			fParentPath, fState;
	//	构造函数;
	public MTConfigure() {
		this.fParentPath = saveDir + File.separator + saveFolder;
		this.fState = Environment.getExternalStorageState();
	}
	/*文件配置的路径内容*/
	public String getfState() {
		return fState;
	}

	public void setfState(String fState) {
		this.fState = fState;
	}

	public String getfParentPath() {
		return fParentPath;
	}
	/*设置屏幕的相应内容*/
	@SuppressWarnings("deprecation")
	public void setScreenWidthAndHeigth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth= wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
	}
	//	1.设置屏幕的宽度;
	public int getScreenWidth() {
		return screenWidth;
	}
	//	2.设置屏幕的高度;
	public int getScreenHeight() {
		return screenHeight;
	}
	/*获得编辑框的内容*/
	public String getEditText(EditText et){
		String result=null;
		try {			
			result=et.getText().toString().trim();
			if(!result.equals("")){
				return result;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	//	颜色的变化;
	@SuppressWarnings("deprecation")
	public void setViewDrawable(boolean flag,Resources resources,View view,int drawable1,int drawable2){
		if(!flag) view.setBackgroundDrawable(resources.getDrawable(drawable1));
		else view.setBackgroundDrawable(resources.getDrawable(drawable2));
	}
}

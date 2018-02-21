package com.model.tool.system;
import android.widget.EditText;

public class MTConfigure {
	public static String TAG_IP_ADDRESS	="172.23.58.113";
	public static String TAG_PORT		="8888";
	public static String TAG_PROGRAM	="PHQBS.example";
	public static final int 	 NTAG_SUCCESS	=1;
	public static final int 	 NTAG_FAIL		=2;
	
	public MTConfigure() {
		
	}
	
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
	
	public void closeThread(Thread thread){
		if(thread!=null){
			thread.interrupt();
			thread=null;
		}
	}
}

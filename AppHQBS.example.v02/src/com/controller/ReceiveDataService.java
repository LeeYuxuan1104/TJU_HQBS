package com.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;

import android.app.Service;
import android.os.Bundle;
import android.os.IBinder;
import android.content.Intent;

public class ReceiveDataService extends Service {
	private boolean    	threadDisable;
	private int 		sleeptime=1000;
	private MTGetOrPostHelper mtGOrPostHelper;
	private String   	pTargetFromService="com.from.service.to.activity";	
	/*	进行相应的数据解析*/
	//	1.计数的相应线程;
	private CountTread	countTread;		
	//	2.进行相应数据采集;
	private long		ltemp=0;

	private Bundle		mBundle;
	
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mBundle			=	intent.getExtras();
		String node_id	=	mBundle.getString("node_id");
		String driver_id=	mBundle.getString("driver_id");
		mtGOrPostHelper	=	new MTGetOrPostHelper();
		threadDisable	=	true;
		countTread=new CountTread(mtGOrPostHelper,node_id,driver_id);
		countTread.start();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	//	01.计数的线程;
	public class CountTread extends Thread{
		private MTGetOrPostHelper mGOrPostHelper;
		private String node_id,driver_id;
		
		public CountTread(MTGetOrPostHelper mGOrPostHelper,String node_id,String driver_id) {
			this.mGOrPostHelper=mGOrPostHelper;
			this.node_id	=node_id;
			this.driver_id	=driver_id;
		}

		@Override
		public void run() {
			while (threadDisable) {
				try {
					dorequest();
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {

				}
			}
		}
		//	进行相应的线程操作;
		private void dorequest(){
			String 		url 	=	null;
			String 		param	=	null;
			JSONArray	array	=	null;
			JSONObject  obj     = 	null;
			int			nSize	=	0;
			String 		response= 	"-1";
			url	  			= 	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/weixintalk_info";
			param 			=	"opertype=2&node_id="+node_id+"&driver_id="+driver_id;
			response  		= 	mGOrPostHelper.sendGet(url, param);
			String  result	=	response.trim();
			if(!result.equals("")){
				if(response!=null){
					try {
						array 	 = new JSONArray(response);
					} catch (JSONException e) {
						array	 = null;
					}
					if(array!=null){
						nSize	 = array.length();
						if(nSize!=0){
							int 	i= 0;
							do {
								try {
									obj 		 	= array.getJSONObject(i);
									String  id 		= obj.getString("id");
									String  message = obj.getString("message");
									String  target 	= obj.getString("target");
									String  timename= obj.getString("timename");
									long	lid		= Long.valueOf(id);
									
									if(lid!=ltemp){
										ltemp=lid;
										Intent  intent=new Intent();
										Bundle	bundle=new Bundle();
										bundle.putString("id", id);
										bundle.putString("message", message);
										bundle.putString("target", target);
										bundle.putString("timename", timename);
										intent.putExtras(bundle);//
										intent.setAction(pTargetFromService);//
										sendBroadcast(intent);
									}
									i++;
								} catch (JSONException e) {
									obj=null;
									break;
								}
							} while (obj!= null);
						} 						
					}
				}
			}
		}
	}
	
	
	//	生命周期关闭线程;
	public void onDestroy() {
		super.onDestroy();
		if(countTread!=null){
			countTread.interrupt();
			countTread=null;
			this.threadDisable = false;
		}
	}
}

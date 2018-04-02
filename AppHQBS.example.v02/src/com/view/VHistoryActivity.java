package com.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.entity.CollectionAndDelivery;
import com.model.entity.Photo;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VHistoryActivity extends Activity implements OnClickListener,OnTouchListener{
	private Context	mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	private RelativeLayout	btnBack,btndetail;
	private TextView		ivBack,tvTopic;
	private TextView		etorder,etdeadline,etgoal,etstate;
	
	/*线程的具体内容*/
	private MTGetOrPostHelper	mtGetOrPostHelper;
	
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框的相应内容;
	private String			id,param="";
	
	/*句柄内容*/
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			String oper	 = bundle.getString("oper");
			//	关闭对话方框;
			if(vDialog!=null){				
				vDialog.dismiss();
			}
			//	判断标记内容; 
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigure.NTAG_SUCCESS:		
				//	提示符;
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_LONG).show();
				if(oper.equals("init")){
					Map<String, String> map=(Map<String, String>) bundle.getSerializable("item");
					jumpActivity(map);
				}
				if(vDialog!=null){
					finish();
				}
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			//	关闭线程;
			closeThread();
		}
	};
	//	线程关闭;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_history_n);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		btnBack	 =(RelativeLayout) findViewById(R.id.btnBack);
		ivBack 	 =(TextView) findViewById(R.id.ivBack);
		tvTopic	 =(TextView) findViewById(R.id.tvTopic);
		etorder	 =(TextView) findViewById(R.id.etorder);
		etdeadline=(TextView) findViewById(R.id.etdeadline);
		etgoal	 =(TextView) findViewById(R.id.etgoal);
		btndetail=(RelativeLayout) findViewById(R.id.btndetail);
		etstate	 =(TextView) findViewById(R.id.etstate);

	}
	//	初始化事件;
	private void initEvent(){
		if(mContext==null){
			mContext=VHistoryActivity.this;
		}
		//
		mIntent	=	getIntent();
		mBundle =	mIntent.getExtras();
		id		= 	mBundle.getString("id");
		String deadline = mBundle.getString("deadline");
		String goal		= mBundle.getString("goal");
		String state	= mBundle.getString("state");
		if(state.equals("2")){
			etstate.setText("正常");
		}else if(state.equals("1")){
			etstate.setText("异常");
		}
		mtGetOrPostHelper = new MTGetOrPostHelper();
		
		ivBack.setBackgroundResource(R.drawable.icon_back);
		btnBack.setOnTouchListener(this);
		btndetail.setOnClickListener(this);
		//	标题名称;
		tvTopic.setText(R.string.topic_history);
		etorder.setText(id);
		etdeadline.setText(deadline);
		etgoal.setText(goal);
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int nVid	=view.getId();
		int nAction =event.getAction();
		switch (nVid) {
		case R.id.btnBack:
			switch (nAction) {  
	            case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域  
	            	ivBack.getBackground().setAlpha(255);
	            	finish();
	                break;  
	            case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域  
	            	ivBack.getBackground().setAlpha(10);
	                break;  
	            default:  
	                break;  
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	private void jumpActivity(Map<String, String> map){
		String id		= map.get("id");
		String deadline	= map.get("deadline");
		String goal		= map.get("goal");
		String information= map.get("information");
		mIntent			= new Intent(mContext, VDiagActivity.class);
		mBundle			= new Bundle();
		mBundle.putString("id",id);
		String content  =
				"电池名称    "+information+"\r\n\r\n" +
				"地址           "+goal+"\r\n\r\n" +
				"日期           "+deadline+"\r\n\r\n";
		mBundle.putString("content", content);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btndetail:
			if(mThread==null){
				param="opertype=5&id="+id;
				mThread=new MyThread(mtGetOrPostHelper, param, "init",null,null);
				mThread.start();
			}
			break;
		default:
			break;
		}
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String 			  		param,oper;
		private MTGetOrPostHelper 		mtGetOrPostHelper;
		private CollectionAndDelivery	collectionAndDelivery;
		private SQLiteDatabase			sqlDB;
		private ArrayList<Photo> 		photolist;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper,CollectionAndDelivery collectionAndDelivery,SQLiteDatabase sqlDB) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.oper			  =oper;
			if(collectionAndDelivery!=null){				
				this.photolist		  =collectionAndDelivery.getMphoto().getPhotolist();
			}
			this.sqlDB			  =sqlDB;
		}
		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  url	 	 =	null;
			String  response = 	null;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			if(oper.equals("init")){				
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
				response	 =	mtGetOrPostHelper.sendGet(url,param);	
				////返回结果
				if(response!=null){
					try {
						array 	= new JSONArray(response);
					} catch (JSONException e) {
						array	= null;
					}
					if(array!=null){
						int 	i= 0;
							do {
								try {
									Map<String, String> map=new HashMap<String, String>();
									obj 		 	  = array.getJSONObject(i);
									String id	 	  = obj.getString("id");
									String deadline	  = obj.getString("deadline");
									String goal		  =obj.getString("goal");
									String information="";

									map.put("id", id);
									if(id.contains("HS")){
										String model=obj.getString("model");
										String count=obj.getString("count");
										String price=obj.getString("price");
										information	=model+"  x  "+count+"   "+price+"元";
									}else if(id.contains("N")){
										information	=obj.getString("information");
									}
									
									map.put("deadline", deadline);
									map.put("goal", goal);
									map.put("information", information);
									bundle.putSerializable("item", (Serializable) map);
									i++;
								} catch (JSONException e) {
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;	
			}else if(oper.equals("submit")){
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
				response	 =	mtGetOrPostHelper.sendGet(url,param);
				if(response.trim().equals("success")){
					//	SQL语句的内容;
					String sql			= 
							"delete from collectionanddelivery where id='"+id+"'";
					//	数据库的更新操作;
					sqlDB.execSQL(sql);
					sql					= 
					"insert into collectionanddelivery (id,photo,status) values ('"+id+"','"+collectionAndDelivery.getMphoto().getImageNames()+"','"+collectionAndDelivery.getStatus()+"')";
					//	数据库的更新操作;
					sqlDB.execSQL(sql);
					//	进行数据显示列表;
					Iterator iterator	=	photolist.iterator();
					while (iterator.hasNext()) {
						Photo 	item	=	 (Photo) iterator.next();
						String  name	=	item.getName();
						String  path	=	item.getPath();
						String  urlhead	=	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
						String  urlbody =	"?opertype=7&folder="+collectionAndDelivery.getId();
						url				=	urlhead+urlbody;
						Log.i("MyLog", "url="+url);
						response	=	mtGetOrPostHelper.uploadFile(url,path,name);
					}
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}
			bundle.putInt("flag", nFlag);
			bundle.putString("oper", oper);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
}

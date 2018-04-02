package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTSQLiteHelper;
import com.model.tool.view.MTDriverAddAdapter;
import com.model.tool.view.MTEditTextWithDel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VDriverAdd2Activity extends Activity implements OnClickListener,OnFocusChangeListener{
	//	内容的上下文;
	private Context		mContext;
	private Resources	mResources;
	/*控件的初始化*/
	private TextView	vBack,vTopic,vSubmit;
	private RelativeLayout laysearch;
	private ImageView	visearch;
	private MTEditTextWithDel etsearch;
	private ListView	vlistshow;
	/*自定义的对象类*/
	private MTConfigure			mtConfigure;
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框;
	//	进行数据库的操作;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	//	适配器的内容;
	private MTDriverAddAdapter	mtDriverAddAdapter;
	//	进行数据库的操作;
	//	1.数据库的帮助类;
	private MTSQLiteHelper		sqlHelper;
	//	2.数据库的控制类;
	private SQLiteDatabase		sqlDB;
	//	3.查询的游标内容;
	private Cursor 		   	  	mCursor;	  // 数据库遍历签;
	//	4.初始化表单;
	private ArrayList<String>	initDrivers;
	//	字符串的内容;
	private String 				node_id;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			String oper  = bundle.getString("oper");
			ArrayList<Map<String, String>> list1=(ArrayList<Map<String, String>>) bundle.getSerializable("list1");
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
				//	对话框显示;
				if(oper.equals("search")){
					loadData(list1);
				}else finish();
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			closeThread();
		}
	};
	
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_add2);
		//	初始化控件;
		initView();
		//	初始化事件;
		initEvent();
	}
	//	控件初始化;
	private void initView(){
		vBack	 =(TextView) findViewById(R.id.btnBack);
		vTopic	 =(TextView) findViewById(R.id.tvTopic);
		laysearch=(RelativeLayout) findViewById(R.id.laysearch);
		visearch =(ImageView) findViewById(R.id.isearch);
		etsearch =(MTEditTextWithDel) findViewById(R.id.etsearch);
		vlistshow=(ListView) findViewById(R.id.listshow);
		//	提交的按钮;
		vSubmit	 =(TextView) findViewById(R.id.btnFunction);
	}
	//	事件初始化;
	private void initEvent(){
		if(mContext==null){
			mContext=VDriverAdd2Activity.this;
		}
		//	资源的内容;
		mResources	=	getResources();
		//	自定义配置项;
		mtConfigure =	new MTConfigure();
		//	网络控件内容;
		mtGetOrPostHelper	=	new MTGetOrPostHelper();
		//	数据库的初始值;
		sqlHelper	=	new MTSQLiteHelper(mContext);
		sqlDB		=	sqlHelper.getmDB();
		//	初始化控件值;
		vBack.setText(R.string.no);
		vBack.setVisibility(View.VISIBLE);
		vTopic.setText(R.string.action_add);
		vSubmit.setText(R.string.submit);
		vSubmit.setVisibility(View.VISIBLE);
		vBack.setOnClickListener(this);
		etsearch.setOnFocusChangeListener(this);
		visearch.setOnClickListener(this);
		vSubmit.setOnClickListener(this);
		//	初始化的数据源;
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		node_id		 =bundle.getString("ownner");
		//	所选单的内容;
		initDrivers=initData();
	}
	//	点击事件监听;
	@SuppressWarnings({"rawtypes", "unchecked" })
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	返回键;
		case R.id.btnBack:
			finish();
			break;
		//	搜索按钮;
		case R.id.isearch:
			getInfo();
			break;
		//	提交按钮;
		case R.id.btnFunction:
			ArrayList<Map<String, String>> list=mtDriverAddAdapter.getListinfo();
			Iterator iterator=list.iterator();
			String param="node_id="+node_id+"&driver_id=";
			while (iterator.hasNext()) {
				Map<String, String> map=(Map<String, String>) iterator.next();
				String id	=map.get("id");
				String state=map.get("state");
				param	   +=id+"_"+state+",";
			}
			if(mThread==null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				mThread = new MyThread(mtGetOrPostHelper,param,"submit",null);
				mThread.start();
			}
			break;
		default:
			break;
		}
	}
	//	进行搜索内容;
	private void getInfo(){
		String getinfo=etsearch.getText().toString();
		if(getinfo instanceof String)
			if(mThread==null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				mThread = new MyThread(mtGetOrPostHelper, "name="+getinfo,"search",initDrivers);
				mThread.start();
			}
		else {			
			Toast.makeText(mContext, "数据类型非字符串", Toast.LENGTH_SHORT).show();
			return ;
		}
				
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		private ArrayList<String> initDrivers;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper,ArrayList<String> initDrivers) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.oper			  =oper;
			//	人员的列表;
			this.initDrivers	  =initDrivers;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  urlhead	 =	null;
			String  urlbody	 =	null;
			String  response = 	null;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			//	进行数据的插入内容;
			ArrayList<Map<String, String>>	list1=new ArrayList<Map<String,String>>();
			int 	nSize	 = 0;
			if(oper.equals("search")){				

				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/driver_info";
				urlbody			 =  "opertype=3&"+param;
				response		 =	mtGetOrPostHelper.sendGet(urlhead,urlbody);	
				////返回结果
				if(response!=null){

					try {
						array 	= new JSONArray(response);
					} catch (JSONException e) {
						array	= null;
					}
					if(array!=null){
						nSize	 = array.length();
						if(nSize!=0){
							int 	i= 0;
							do {
								try {
									obj 		 = array.getJSONObject(i);
									String id	 = obj.getString("id");
									String carnumber	 = obj.getString("carnumber");
									String name	 = obj.getString("name");
									Log.i("MyLog", id+"|"+carnumber+"|"+name);
									//	进行数据内容;
									Map<String , String> map=new HashMap<String, String>();
									map.put("id", id);
									map.put("carnumber", carnumber);
									map.put("name", name);
									if(initDrivers.contains(id))										
										map.put("state", "1");
									else map.put("state", "0");
									
									list1.add(map);
									//	下角标进行迭加;
									i++;
								} catch (JSONException e) {
									//	当出现异常的情况下,进行跳出;
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}else if(oper.equals("submit")){
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/driver_info";
				urlbody			 =  "opertype=2&"+param;
				response		 =	mtGetOrPostHelper.sendGet(urlhead,urlbody);	
				if(response!=null){
					nFlag	 = 	MTConfigure.NTAG_SUCCESS;
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}
			bundle.putSerializable("list1", list1);
			bundle.putString("oper", oper);
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
	//	聚焦的内容信息;
	@Override
	public void onFocusChange(View view, boolean flag) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.etsearch:
			mtConfigure.setViewDrawable(flag, mResources, laysearch, R.drawable.shape_edit0, R.drawable.shape_edit1);
			break;
		default:
			break;
		}
	}
	//	进行数据的加载;
	private void loadData(ArrayList<Map<String, String>> list){
		mtDriverAddAdapter=new MTDriverAddAdapter(mContext, list);
		vlistshow.setAdapter(mtDriverAddAdapter);
	}
	//	进行数据的初始化;
	private ArrayList<String> initData(){
		ArrayList<String> hasDrivers=new ArrayList<String>();
		String 	sql	=	"select * from driver ";
		mCursor		=	sqlDB.rawQuery(sql, null);
		while (mCursor.moveToNext()) {	
			String id		=	mCursor.getString(mCursor.getColumnIndex("id")).toString();
			hasDrivers.add(id);
		}
		
		if(mCursor!=null){
			mCursor.close();
		}

		return hasDrivers;
	}
}

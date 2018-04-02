package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.tool.adapter.MTListDriverAddAdapter;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTIDHelper;
import com.model.tool.system.MTSQLiteHelper;
import com.model.tool.view.MTEditTextWithDel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VDriverAddActivity extends Activity implements OnTouchListener,OnClickListener{
	//	上下文的内容;
	private Context	mContext;
	/*控件的初始化*/	
	private RelativeLayout	btnback,btnsubmit,btnsearch;
	private TextView		ivback,tvTopic,ivsubmit;
	private ListView 		listdrivers;
	//	内容输入框;
	private MTEditTextWithDel etname;
	private MTListDriverAddAdapter	mtListDriverAddAdapter;
	/*	进行数据库的操作*/
	//	1.数据库的帮助类;
	private MTSQLiteHelper		sqlHelper;
	//	2.数据库的控制类;
	private SQLiteDatabase		sqlDB;
	//	3.查询的游标内容;
	private Cursor 		   	  	mCursor;	  // 数据库遍历签;
	/*进行数据的初始化*/
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框;
	//	进行数据库的操作;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	/*自定义对象类*/
	private MTIDHelper	   		idHelper;	 //	使用者的身份
	private String				owner,param;
	private ArrayList<String>   initDrivers;
	private ArrayList<Map<String, String>> listalldrivers;
	
	
	//	进行线程的内容;
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			String oper  = bundle.getString("oper");
			ArrayList<Map<String, String>> listdrivers=(ArrayList<Map<String, String>>) bundle.getSerializable("listdrivers");
			//	关闭对话方框;
			if(vDialog!=null){				
				vDialog.dismiss();
			}
			Log.i("MyLog", "oper="+oper+"|flag="+nFlag+"|list="+listdrivers);
			//	判断标记内容; 
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigure.NTAG_SUCCESS:		
				//	提示符;
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_LONG).show();
				//	对话框显示;
				if(oper.equals("search")){
					listalldrivers=loadData(mContext,listdrivers);
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
	//	关闭线程;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_add_n);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		tvTopic = (TextView) findViewById(R.id.tvTopic);
		ivback  = (TextView) findViewById(R.id.ivBack);
		btnback = (RelativeLayout) findViewById(R.id.btnBack);
		//	提交的按钮;
		ivsubmit= (TextView) findViewById(R.id.ivFunction);
		btnsubmit  = (RelativeLayout) findViewById(R.id.btnFunction);
		//	显示列表的;
		listdrivers= (ListView) findViewById(R.id.listdrivers);
		// 	搜索按钮;
		btnsearch  = (RelativeLayout) findViewById(R.id.btnsearch);
		//	输入名字;
		etname	=	(MTEditTextWithDel) findViewById(R.id.etid);
	}
	//	初始化事件;
	private void initEvent(){
		if(mContext==null){
			mContext=VDriverAddActivity.this;
		}
		listalldrivers		=	new ArrayList<Map<String,String>>();
		//	数据库的初始化操作;
		mtGetOrPostHelper	=	new MTGetOrPostHelper();
		//	数据库的初始值;
		sqlHelper	=	new MTSQLiteHelper(mContext);
		sqlDB		=	sqlHelper.getmDB();
		//	控件数值的初始化;
		tvTopic.setText(R.string.topic_driver_add);
		ivback.setBackgroundResource(R.drawable.icon_back);
		btnback.setOnTouchListener(this);
		ivsubmit.setText(R.string.submit);
		ivsubmit.setTextColor(Color.WHITE);
		btnsubmit.setVisibility(View.VISIBLE);
		btnsubmit.setOnTouchListener(this);
		btnsearch.setOnClickListener(this);
		//	进行数据库的初始化;
		initIDinfo();
		//	所选单的内容;
		initDrivers=initData();
	}
	//	初始化身份的信息;
	private void initIDinfo(){
		idHelper	= new MTIDHelper(mContext, "userinfo", Context.MODE_APPEND);
		boolean flag= idHelper.isIdExist();
		if(flag){
			owner=idHelper.getUseid();
		}
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int nVid	=view.getId();
		int nAction =event.getAction();
		switch (nVid) {
		case R.id.btnBack:
			switch (nAction) {  
            case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域  
            	ivback.getBackground().setAlpha(255);
            	finish();
                break;  
            case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域  
            	ivback.getBackground().setAlpha(10);
                break;  
            default:  
                break;  
			}
			break;
		case R.id.btnFunction:
			switch (nAction) {  
            case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域  
            	ivsubmit.setTextColor(Color.WHITE);
            	int size=listalldrivers.size();
            	if(size!=0){            		
            		setInfo(owner, listalldrivers);
            	}else Toast.makeText(mContext, "无可以提交信息", Toast.LENGTH_SHORT).show();
                break;  
            case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域  
            	ivsubmit.setTextColor(Color.GREEN);
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
	//	进行数据进行加载;
	private ArrayList<Map<String, String>> loadData(Context context,final ArrayList<Map<String, String>> list){
		mtListDriverAddAdapter=new MTListDriverAddAdapter(context, list);
		listdrivers.setAdapter(mtListDriverAddAdapter);
		//	list控件的监听事件;
		listdrivers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				String 	 state  = list.get(position).get("state");
				TextView ivstate= (TextView) view.findViewById(R.id.ivstate);
				int	   nstate=Integer.parseInt(state);
				//	选中;
				if(nstate==1){
					ivstate.setBackgroundResource(R.drawable.icon_person_unsele);
					list.get(position).put("state","0");
				}
				//	未选;
				else{
					ivstate.setBackgroundResource(R.drawable.icon_person_sele);	
					list.get(position).put("state","1");
				}
				
			}
		});
		return list;
	}

	//	进行数据的初始化;
	private ArrayList<String> initData(){
		ArrayList<String> hasDrivers=new ArrayList<String>();
		String 	sql	=	"select id from driver ";
		mCursor		=	sqlDB.rawQuery(sql, null);
		while (mCursor.moveToNext()) {	
			String id			=	mCursor.getString(mCursor.getColumnIndex("id")).toString();
			hasDrivers.add(id);
		}
		
		if(mCursor!=null){
			mCursor.close();
		}

		return hasDrivers;
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	搜索按钮;
		case R.id.btnsearch:
			getInfo();
			break;

		default:
			break;
		}
	}
	//	进行搜索内容;
	private void getInfo(){
		String getinfo=etname.getText().toString();
		if(getinfo instanceof String)
			if(!getinfo.equals("")){				
				if(mThread==null){
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					param	= "opertype=6&name="+getinfo+"&id="+owner;
					mThread = new MyThread(mtGetOrPostHelper, param,"search",initDrivers);
					mThread.start();
				}
			}
		else {			
			Toast.makeText(mContext, "数据类型非字符串", Toast.LENGTH_SHORT).show();
			return ;
		}		
	}
	//	设置提交信息列表;
	private void setInfo(String owner,ArrayList<Map<String, String>> list){
		Iterator<Map<String, String>> iterator=list.iterator();
		String param="opertype=7&node_id="+owner+"&driver_id=";
		while (iterator.hasNext()) {
			Map<String, String> map=(Map<String, String>) iterator.next();
			String driver_id = map.get("id");
			String state	 = map.get("state");
			param	   		+= driver_id+"_"+state+",";
		}
		if(mThread==null){
			final CharSequence strDialogTitle = getString(R.string.wait);
			final CharSequence strDialogBody = getString(R.string.doing);
			vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
			mThread = new MyThread(mtGetOrPostHelper,param,"submit",initDrivers);
			mThread.start();
		}
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		private ArrayList<String> initDrivers;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper,ArrayList<String> initDrivers) {
			this.mtGetOrPostHelper= mtGetOrPostHelper;
			this.param			  = param;
			this.oper			  = oper;
			this.initDrivers	  = initDrivers;
		}
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
			//	进行数据的插入内容;
			ArrayList<Map<String, String>>	listdrivers=new ArrayList<Map<String,String>>();
			if(oper.equals("search")){
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/user_info";
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
									obj 		 	 = array.getJSONObject(i);
									String id	 	 = obj.getString("id");
									String car_number= obj.getString("car_number");
									String name	 	 = obj.getString("name");
									//	进行数据内容;
									Map<String , String> map=new HashMap<String, String>();
									map.put("id", id);
									map.put("car_number", car_number);
									map.put("name", name);
									if(initDrivers.contains(id))										
										map.put("state", "1");
									else map.put("state", "0");
									
									listdrivers.add(map);
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
			}else if(oper.equals("submit")){
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/user_info";
				response	 =	mtGetOrPostHelper.sendGet(url,param);
//				if(!response.equals("success")){
//					nFlag	 = 	MTConfigure.NTAG_FAIL;			
//				}
			}
			bundle.putSerializable("listdrivers", listdrivers);
			bundle.putString("oper", oper);
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
}

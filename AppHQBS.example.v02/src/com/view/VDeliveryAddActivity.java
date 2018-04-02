package com.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.entity.Collection;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTIDHelper;
import com.model.tool.view.MTPopupWindows;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VDeliveryAddActivity extends Activity implements OnTouchListener,OnClickListener,OnFocusChangeListener{
	//	上下文的内容;
	private Context			mContext;
	private Intent			mIntent;
	/*控件的初始化*/	
	private RelativeLayout	btnback,btnlocation;
	private TextView		ivback,tvTopic,etgoal;
	private Spinner			etdrivers,etmodels;
	private EditText		etdeadline,etprice,etcount;
	private Button			btnsubmit;
	/*网络的相应值*/
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框的相应内容;
	private MTIDHelper	   		idHelper;	 //	使用者的身份
	private SimpleAdapter		driverAdapter,batteryAdapter;
	/*自定义的参数*/
	private String 				owner,param,driver_id,model;
	private String 				lng="0",lat="0",address;
	private Collection			collection;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			ArrayList<Map<String, String>>  listdrivers	=(ArrayList<Map<String, String>>) bundle.getSerializable("listdrivers");
			ArrayList<Map<String, String>>  listgoods	=(ArrayList<Map<String, String>>) bundle.getSerializable("listgoods");
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
				if(listdrivers!=null&&listgoods!=null){
					loadData(listdrivers, listgoods);
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_delivery_add_n);
		initView();
		initEvent();
	}
	/*系列初始化的你内容*/
	//	初始化控件;
	private void initView(){
		tvTopic  = (TextView) findViewById(R.id.tvTopic);
		ivback   = (TextView) findViewById(R.id.ivBack);
		btnback  = (RelativeLayout) findViewById(R.id.btnBack);
		etdrivers= (Spinner) findViewById(R.id.etdrivers);
		etmodels = (Spinner) findViewById(R.id.etmodels);
		etprice	 = (EditText) findViewById(R.id.etprice);
		etcount	 = (EditText) findViewById(R.id.etcount);
		etgoal	 = (TextView) findViewById(R.id.etgoal);
		btnlocation=(RelativeLayout) findViewById(R.id.btnlocation);
		etdeadline =(EditText) findViewById(R.id.etdeadline);
		btnsubmit= (Button) findViewById(R.id.btnsubmit);
	}
	//	初始化事件;
	private void initEvent(){
		if(mContext==null){
			mContext=VDeliveryAddActivity.this;
		}
		//	网络的初始化声明;
		mtGetOrPostHelper = new MTGetOrPostHelper();
		//	控件值得初始化;
		tvTopic.setText(R.string.topic_deliver_add);
		ivback.setBackgroundResource(R.drawable.icon_back);
		btnback.setOnTouchListener(this);
		btnlocation.setOnClickListener(this);
		etdeadline.setOnClickListener(this);
		etgoal.setEnabled(false);
		// 身份的信息;
		initIDinfo();
		//	进行列表信息的初始化;
		param	=	"opertype=5&id="+owner;
		initLoadDataFromServer(param);
		btnsubmit.setOnClickListener(this);
	}
	
	//	初始化身份的信息;
	private void initIDinfo(){
		idHelper=new MTIDHelper(mContext, "userinfo", Context.MODE_APPEND);
		boolean flag=idHelper.isIdExist();
		if(flag){
			owner=idHelper.getUseid();
		}
	}
	//	进行初始化查询;
	private void initLoadDataFromServer(String param){
		if(mThread==null){
			mThread=new MyThread(mtGetOrPostHelper, param, "init");
			mThread.start();
		}
	}
	/*事件监听的内容*/
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
		default:
			break;
		}
		return false;
	}
	//	获得信息;
	private void getinfo(){
		if(mThread==null){		
			if(collection==null){
				collection=new Collection();
			}
			collection=collection.getInfo(owner, driver_id, null, etdeadline, null, lng, lat,"1", etprice, etcount, etgoal, model);
			if(collection!=null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				try {
					param	= 
					"opertype=3&" +
					"owner="+collection.getOwner()+"&" +
					"driver="+collection.getDriver()+"&" +
					"photo="+collection.getPhoto()+"&" +
					"deadline="+collection.getDeadline()+"&" +
					"message="+URLEncoder.encode(collection.getMessage(),"utf-8")+"&" +
					"lng="+collection.getLng()+"&" +
					"lat="+collection.getLat()+"&" +
					"status="+collection.getStatus()+"&" +
					"price="+collection.getPrice()+"&" +
					"count="+collection.getCount()+"&" +
					"goal="+URLEncoder.encode(collection.getGoal(),"utf-8")+"&" +
					"model="+URLEncoder.encode(collection.getModel(),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mThread = new MyThread(mtGetOrPostHelper,param,"submit");
				mThread.start();
			}else Toast.makeText(mContext, "请正确输入", Toast.LENGTH_SHORT).show();
		}
	}
	//	线程定义;
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String 			  param,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.oper			  =oper;
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
			if(oper.equals("init")){				
				ArrayList<Map<String, String>>	listdrivers=new ArrayList<Map<String,String>>();
				ArrayList<Map<String, String>>  listgoods  =new ArrayList<Map<String,String>>();
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
									obj 		 	  = array.getJSONObject(i);
									String id	 	  = obj.getString("id");
									String name	 	  = obj.getString("name");
									String car_number = obj.getString("car_number");
									Map<String, String> map=new HashMap<String, String>();
									map.put("id", id);
									map.put("name", name);
									map.put("car_number", car_number);
									listdrivers.add(map);
									i++;
								} catch (JSONException e) {
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				if(listdrivers!=null){					
					bundle.putSerializable("listdrivers", listdrivers);
				}
				
				url		 	=	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/battery";
				response	=	mtGetOrPostHelper.sendGet(url,"opertype=1");	
				////返回结果
				if(response!=null){
					try {
						array 	 = new JSONArray(response);
					} catch (JSONException e) {
						array	 = null;
					}
					if(array!=null){
							int 	i= 0;
							do {
								try {
									obj 		 = array.getJSONObject(i);
									String model = obj.getString("model");
									String brand = obj.getString("brand");
									String price = obj.getString("price");
									String count = obj.getString("count");
									Map<String, String> map=new HashMap<String, String>();
									map.put("model", model);
									map.put("brand", brand);
									map.put("price", price+"元/件");
									map.put("count", count);
									listgoods.add(map);
									i++;
								} catch (JSONException e) {
									obj=null;
									break;
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				if(listgoods!=null){					
					bundle.putSerializable("listgoods", listgoods);
				}
			}else if(oper.equals("submit")){
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collection";
				response	 =	mtGetOrPostHelper.sendGet(url,param);
				Log.i("MyLog", url+"?"+param);
//				if(response!=null){
//					nFlag	 = 	MTConfigure.NTAG_SUCCESS;
//				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
	//	进行数据加载;
	private void loadData(final ArrayList<Map<String, String>> listdrivers,final ArrayList<Map<String, String>> listgoods){
		driverAdapter=new SimpleAdapter(mContext, listdrivers, R.layout.act_item_spinner_n, new String[]{"id","name"}, new int[]{R.id.id,R.id.name});
		etdrivers.setAdapter(driverAdapter);
		batteryAdapter=new SimpleAdapter(mContext, listgoods, R.layout.act_item_spinner_n, new String[]{"model","price"}, new int[]{R.id.id,R.id.name});
		etmodels.setAdapter(batteryAdapter);
		//	添加回收员监听;
		etdrivers.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> map	=listdrivers.get(position);
				driver_id				=map.get("id");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Map<String, String> map	=listdrivers.get(0);
				driver_id				=map.get("id");
			}
		});
		//	添加电池的事件监听;
		etmodels.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> map	=listgoods.get(position);
				model					=map.get("model");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Map<String, String> map	=listgoods.get(0);
				model					=map.get("model");
			}
		});
	}
	
	//	线程关闭;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnlocation:
			mIntent=new Intent(mContext, VGeoFenceActivity.class);
			startActivityForResult(mIntent, 2);
			break;
		case R.id.etdeadline:
			new MTPopupWindows(mContext, etdeadline,etdeadline);
			break;
		case R.id.btnsubmit:
			//	提交按钮;
			getinfo();
			break;
		default:
			break;
		}
		
	}
	//	回复的内容信息;
	@Override
	protected void onActivityResult(int req, int res, Intent data) {
		super.onActivityResult(req, res, data);
		Bundle bundle=data.getExtras();
		//	地图注册页面的返回值;
		if(req==2&&res==1){
			address=bundle.getString("address").trim();
			if(Double.parseDouble(bundle.getString("lng"))!=-1&&Double.parseDouble(bundle.getString("lat"))!=-1){				
				//	地址信息的加载;
				lng=bundle.getString("lng");
				lat=bundle.getString("lat");	
			}
			if(!address.equals("")){				
				Toast.makeText(mContext,R.string.locate_finished, Toast.LENGTH_SHORT).show();
			}
			etgoal.setText(address);
		}
	}
	@Override
	public void onFocusChange(View view, boolean flag) {
		// TODO Auto-generated method stub
		
	}
}

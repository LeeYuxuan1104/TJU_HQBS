package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.hqbs.app.R;
import com.model.entity.MEDelivery;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.view.MTEditTextWithDel;
import com.model.tool.view.MTPopupWindows;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VDeliveryAddActivity extends Activity implements OnClickListener,OnFocusChangeListener{
	//	上下文定义;
	private Context				mContext;
	private Resources			mResources;
	
	//	按钮的内容;
	private TextView 			vBack,vTopic,vBargain;
	private Button 				btnSumit,vLocation;
	private Spinner 			spdriver,spmodel;
	private TextView 			etbrand,etprice;
	private MTEditTextWithDel 	etcount,etgoal,etdeadline;
	//	自定义的参数内容;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private MyThread			mThread=null;// 自定义的上传线程;
	private MTConfigure			mtConfigure;
	private ProgressDialog  	vDialog;	 // 对话框;
	private Intent				mIntent;
	private Bundle				mBundle;
	private String 				ownner;
	private SimpleAdapter		driverAdapter,goodsAdapter;
	private RelativeLayout		laycount,laygoal,laydeadline;
	//	参数自定义;
	private String 				driver,model;
	private double 				lng,lat;
	private MEDelivery			delivery;
	
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
		setContentView(R.layout.act_delivery_add);
		initView();
		initEvent();
	}
	//	控件初始化;
	private void initView(){
		vBack	=	(TextView) findViewById(R.id.btnBack);
		vTopic	=	(TextView) findViewById(R.id.tvTopic);	
		vBargain = 	(TextView) findViewById(R.id.btnFunction);
		
		btnSumit=	(Button) findViewById(R.id.btnSumit);
		vLocation=	(Button) findViewById(R.id.btnLocation);
		spdriver=	(Spinner) findViewById(R.id.spdriver);
		spmodel	=	(Spinner) findViewById(R.id.spmodel);
		etbrand	=	(TextView) findViewById(R.id.etbrand);
		etprice	=	(TextView) findViewById(R.id.etprice);
		etcount	=	(MTEditTextWithDel) findViewById(R.id.etcount);
		etgoal	=	(MTEditTextWithDel) findViewById(R.id.etgoal);
		etdeadline=	(MTEditTextWithDel) findViewById(R.id.etdeadline);
		laycount=	(RelativeLayout) findViewById(R.id.laycount);
		laygoal	=	(RelativeLayout) findViewById(R.id.laygoal);
		laydeadline=(RelativeLayout) findViewById(R.id.laydeadline);
	}
	//	事件初始化;
	private void initEvent(){
		if(mContext==null){			
			mContext	=	VDeliveryAddActivity.this;
		}
		mResources	=	getResources();
		mtConfigure	=	new MTConfigure();
		mtGetOrPostHelper=new MTGetOrPostHelper();
		vTopic.setText(R.string.action_add);
		vBack.setText(R.string.no);
		vBack.setVisibility(View.VISIBLE);
		vBargain.setText(R.string.bargain);
		vBargain.setVisibility(View.VISIBLE);
		
		vBack.setOnClickListener(this);
		vLocation.setOnClickListener(this);
		vBargain.setOnClickListener(this);
		etdeadline.setOnClickListener(this);
		btnSumit.setOnClickListener(this);
		mIntent		=	getIntent();
		Bundle	bundle=mIntent.getExtras();
		ownner		=	bundle.getString("ownner");
		initLoadDataFromServer(ownner);
		//	进行按钮的监听;
		etcount.setOnFocusChangeListener(this);
		etgoal.setOnFocusChangeListener(this);
		etdeadline.setOnFocusChangeListener(this);
	}
	//	按下的点击事件;
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	取消按钮;
		case R.id.btnBack:
			finish();
			break;
		//	定位按钮;
		case R.id.btnLocation:
			mIntent=new Intent(mContext, VGeoFenceActivity.class);
			startActivityForResult(mIntent, 2);
			break;
		//	时间按钮;
		case R.id.etdeadline:
			new MTPopupWindows(mContext, etdeadline,etdeadline);
			break;
		//	提交内容;
		case R.id.btnSumit:
			if(delivery==null){
				delivery=new MEDelivery();
			}
				delivery=delivery.getDeliyerInfo(ownner, driver, model, etprice, etcount, lng, lat, etdeadline, etgoal);
			if(delivery!=null){
				if(mThread==null){
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					String param=
							"ownner="+delivery.getOwnner()+"&" +
									"driver="+delivery.getDriver()+"&" +
									"model="+delivery.getModel()+"&"+
									"goal="+delivery.getGoal()+"&"+
									"price="+delivery.getPrice()+"&"+
									"lng="+delivery.getLng()+"&"+
									"lat="+delivery.getLat()+"&"+
									"count="+delivery.getCount()+"&"+
									"deadline="+delivery.getDeadline()
									;
					mThread=new MyThread(mtGetOrPostHelper, param, "submit");
					mThread.start();
				}
			}else Toast.makeText(mContext, "请正确输入", Toast.LENGTH_SHORT).show();				
			break;
		case R.id.btnFunction:
			mIntent=new Intent(mContext, VWeixinActivity.class);
			mBundle=new Bundle();
			mBundle.putString("node_id", ownner);
			mBundle.putString("driver_id", driver);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
	//	进行初始化查询;
	private void initLoadDataFromServer(String param){
		if(mThread==null){
			mThread=new MyThread(mtGetOrPostHelper, param, "init");
			mThread.start();
		}
	}
	//	进行数据加载;
	private void loadData(final ArrayList<Map<String, String>> listdrivers,final ArrayList<Map<String, String>> listgoods){
		driverAdapter=new SimpleAdapter(mContext, listdrivers, R.layout.act_item01, new String[]{"name"}, new int[]{R.id.content});
		spdriver.setAdapter(driverAdapter);
		goodsAdapter=new SimpleAdapter(mContext, listgoods, R.layout.act_item01, new String[]{"model"}, new int[]{R.id.content});
		spmodel.setAdapter(goodsAdapter);
		//	添加回收员监听;
		spdriver.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> map	=listdrivers.get(position);
				driver					=map.get("driver");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Map<String, String> map	=listdrivers.get(0);
				driver					=map.get("driver");
			}
		});
		//	添加电池的事件监听;
		spmodel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Map<String, String> map	=listgoods.get(position);
				model					=map.get("model");
				String brand			=map.get("brand");
				String price			=map.get("price");
				etbrand.setText(brand);
				etprice.setText(price+"元");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Map<String, String> map	=listgoods.get(0);
				model					=map.get("model");
				String brand			=map.get("brand");
				String price			=map.get("price");
				etbrand.setText(brand);
				etprice.setText(price+"元");
			}
		});
	}
	
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param,oper;
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
			String  urlhead	 =	null;
			String  urlbody	 =	null;
			String  response = 	null;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			int 	nSize	 = 0;
			if(oper.equals("init")){				
				ArrayList<Map<String, String>>	listdrivers=new ArrayList<Map<String,String>>();
				ArrayList<Map<String, String>>  listgoods  =new ArrayList<Map<String,String>>();
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/delivery_info";
				urlbody			 =  "opertype=1&ownner="+param;
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
									String driver= obj.getString("driver");
									String name	 = obj.getString("name");
									Map<String, String> map=new HashMap<String, String>();
									map.put("driver", driver);
									map.put("name", name);
									listdrivers.add(map);
									i++;
								} catch (JSONException e) {
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				if(listdrivers!=null){					
					bundle.putSerializable("listdrivers", listdrivers);
				}
				
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/good_info";
				urlbody			 =  "opertype=1";
				response		 =	mtGetOrPostHelper.sendGet(urlhead,urlbody);	
				////返回结果
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
									obj 		 = array.getJSONObject(i);
									String model = obj.getString("model");
									String brand = obj.getString("brand");
									String price = obj.getString("price");
									String count = obj.getString("count");
									Map<String, String> map=new HashMap<String, String>();
									map.put("model", model);
									map.put("brand", brand);
									map.put("price", price);
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
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				if(listgoods!=null){					
					bundle.putSerializable("listgoods", listgoods);
				}
			}else if(oper.equals("submit")){
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/delivery_info";
				urlbody			 =  "opertype=2&"+param;
				response		 =	mtGetOrPostHelper.sendGet(urlhead,urlbody);	
				if(response!=null){
					nFlag	 = 	MTConfigure.NTAG_SUCCESS;
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
	//	线程关闭;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//	进行系统的自定义;
	@Override
	protected void onActivityResult(int req, int res, Intent data) {
		super.onActivityResult(req, res, data);
		Bundle bundle=data.getExtras();
		//	地图注册页面的返回值;
		if(req==2&&res==1){
			if(bundle.getDouble("lng")!=-1&&bundle.getDouble("lat")!=-1){				
				//	经度;
				lng=bundle.getDouble("lng");
				//	纬度;
				lat=bundle.getDouble("lat");
				vLocation.setBackgroundColor(Color.GREEN);
				Toast.makeText(mContext,"定位完成", Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public void onFocusChange(View view, boolean flag) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.etcount:
			mtConfigure.setViewDrawable(flag, mResources, laycount, R.drawable.shape_edit0, R.drawable.shape_edit1);
			break;
		case R.id.etgoal:
			mtConfigure.setViewDrawable(flag, mResources, laygoal, R.drawable.shape_edit0, R.drawable.shape_edit1);
			break;
		case R.id.etdeadline:
			mtConfigure.setViewDrawable(flag, mResources, laydeadline, R.drawable.shape_edit0, R.drawable.shape_edit1);
			break;
		default:
			break;
		}
		
	}
}

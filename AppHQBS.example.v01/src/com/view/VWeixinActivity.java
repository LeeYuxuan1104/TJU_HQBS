package com.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.controller.ReceiveDataService;
import com.hqbs.app.R;
import com.model.entity.MEBargaininfo;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.view.MTEditTextWithDel;
import com.model.tool.view.MTWeixinAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class VWeixinActivity extends Activity implements OnClickListener,OnFocusChangeListener{
	private Context	  		  mContext;
	private Resources 		  mResources;
	/*	自定义控件的内容*/
	private RelativeLayout 	  layinfo;
	private TextView  		  vBack,vTopic;
	private Button	  		  vSubmit;
	private MTEditTextWithDel etinfo;
	private ListView  		  vListinfo;
	//	自定义适配器的内容;
	private MTWeixinAdapter   adapter;
	private List<Map<String, String>>	listinfo;
	//	进行线程的内容;
	private MyThread 		  mThread=null;
	/*进行操作的内容*/
	//	进行检测监听;
	//	01.计数项;
	private int 	 		  pCount;
	private String 			  node_id,driver_id;
	//	02.标签发送;
	//	03.标签接收;
	private String    		pTargetFromService	=	"com.from.service.to.activity";
	private GetInfoReceiver	getInfoReceiver;
	private Intent			setIntentInfo;
	private Bundle			mBundle;
	private IntentFilter 	getInfoFilter;
	/*	自定义的类*/
	private MTConfigure		mtConfigure;
	private MTGetOrPostHelper mGOrPostHelper;
	private MEBargaininfo	meBargaininfo;
	
	//	进行数据的更新操作;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			//	关闭对话方框;
			//	判断标记内容; 
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigure.NTAG_SUCCESS:		
				Toast.makeText(mContext, R.string.sended, Toast.LENGTH_SHORT).show();
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				Toast.makeText(mContext, R.string.sendfail, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			//	关闭线程的操作;
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
		setContentView(R.layout.act_weixin);
		//	初始化控件;
		initView();
		//	初始化事件;
		initEvent();
		//	初始化服务;
		initService();
	}
	//	控件进行初始化;
	private void initView(){
		//	返回键的按钮;
		vBack	=	(TextView) findViewById(R.id.btnBack);
		//	标题栏的按钮;
		vTopic	=	(TextView) findViewById(R.id.tvTopic);
		//	提交键的按钮;
		vSubmit	=	(Button) findViewById(R.id.btnSubmit);
		//	输入框;
		etinfo	=	(MTEditTextWithDel) findViewById(R.id.etinfo);
		//	信息列表;
		vListinfo=  (ListView) findViewById(R.id.listinfo);
		//	进行边框;
		layinfo	=	(RelativeLayout) findViewById(R.id.layinfo);
	}
	//	事件进行初始化;
	private void initEvent(){
		if(mContext==null){
			mContext=	VWeixinActivity.this;
		}
		//	获得相应的资源内容信息;
		mResources	   = getResources();
		mtConfigure	   = new MTConfigure();
		mGOrPostHelper = new MTGetOrPostHelper();
		/*进行相应的数据监听的内容添加*/
		vBack.setText(R.string.no);
		vBack.setVisibility(View.VISIBLE);
		vTopic.setText(R.string.bargain_info);
		vSubmit.setOnClickListener(this);
		vBack.setOnClickListener(this);
		/*获得控件的大小尺寸*/
		etinfo.setOnFocusChangeListener(this);
		/*进行显示的列表显示内容*/
		listinfo=	new ArrayList<Map<String,String>>();
		adapter	=	new MTWeixinAdapter(mContext, listinfo);	
		vListinfo.setAdapter(adapter);
		/*计数的起始内容*/
		pCount	=	0;
		/*获得相应的编号*/
		Intent intent=getIntent();
		mBundle		 =intent.getExtras();
		node_id	 =	mBundle.getString("node_id");
		driver_id=	mBundle.getString("driver_id");
	}
	/*进行服务的相应内容*/
	private void initService(){
		getInfoReceiver = new GetInfoReceiver();
		getInfoFilter 	= new IntentFilter();
		getInfoFilter.addAction(pTargetFromService);
		registerReceiver(getInfoReceiver, getInfoFilter);
		//	服务机制;
		/*	01.注册广播—发送信息*/
		setIntentInfo	=	new Intent(mContext, ReceiveDataService.class);
		mBundle			=	new Bundle();
		mBundle.putString("node_id", node_id);
		mBundle.putString("driver_id", driver_id);
		setIntentInfo.putExtras(mBundle);
		startService(setIntentInfo);
	}
	
	//	自定义的adapter的内容; 
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnSubmit:
			if(mThread==null){
				if(meBargaininfo==null){
					meBargaininfo=new MEBargaininfo();
				}
				meBargaininfo=meBargaininfo.getBargaininfo(0, node_id, driver_id, etinfo,1, null);
				mThread=new MyThread(mGOrPostHelper, meBargaininfo);
				mThread.start();
			}
			break;
		default:
			break;
		}
	}
	/*进行数据接收的内容*/
	public class GetInfoReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent){
			//	进行数据的包装;
			Bundle bundle  = intent.getExtras();
			//	id-编号,暂时无用;
			String id	   = bundle.getString("id");
			//	message消息;
			String message = bundle.getString("message");
			//	身份标签;
			String target  = bundle.getString("target");
			//	时间表戳;
			String timename= bundle.getString("timename");
			//	承装时间容器;
			Map<String, String> map=new HashMap<String, String>();
			map.put("id", id);
			map.put("cookie", target);
			map.put("content", timename+"\r\n"+message);
			listinfo.add(map);
			pCount++;
			if(pCount>30){
				listinfo.remove(0);
			}
			adapter=new MTWeixinAdapter(mContext, listinfo);
			vListinfo.setAdapter(adapter);
		}
	}
	/*进行事件解除服务*/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//	接收传递的数据包;
		if(getInfoReceiver!=null){
			unregisterReceiver(getInfoReceiver);
		}
		//	发送传递的数据包;
		if(setIntentInfo!=null){
			stopService(setIntentInfo);
		}
	}

	
	@Override
	public void onFocusChange(View view, boolean flag) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.etinfo:
			mtConfigure.setViewDrawable(flag, mResources, layinfo, R.drawable.shape_edit0,  R.drawable.shape_edit1);
			break;

		default:
			break;
		}
	}
	//	进行相应的数据的线程;
	class MyThread extends Thread{
		//	进行get以及post的方法;
		private MTGetOrPostHelper mGOrPostHelper;
		private MEBargaininfo	  meBargaininfo;

		public MyThread(MTGetOrPostHelper mGOrPostHelper,MEBargaininfo	meBargaininfo) {
			this.mGOrPostHelper	=	mGOrPostHelper;
			this.meBargaininfo	=	meBargaininfo;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			// 传值;
			String url 	  	 = "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/weixintalk_info";
			String param;
			String response	 = "fail";
			try {
				param 	  = 
					"opertype=1&" +
					"node_id="+meBargaininfo.getNode_id()+"&" +
					"driver_id="+meBargaininfo.getDriver_id()+"&" +
					"target="+meBargaininfo.getTarget()+"&" +
					"message="+URLEncoder.encode(meBargaininfo.getMessage(),"utf-8");
				response  = mGOrPostHelper.sendGet(url, param);

			} catch (UnsupportedEncodingException e) {
				
			}

			if (response.trim().equalsIgnoreCase("fail")) {
				nFlag = 2;
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}
	}
}

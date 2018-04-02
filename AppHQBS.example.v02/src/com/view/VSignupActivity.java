package com.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.hqbs.app.R;
import com.model.entity.User;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class VSignupActivity extends Activity implements OnClickListener{
	//	程序的上下文内荣;
	private Context 		mContext;
	private Intent			mIntent;	
	//	上顶端的控件;
	private TextView		ivBack,tvTopic,tvrole;
	//	中间的控件;
	private EditText 		etname,etpwd,
//	etconfirm,
	etphone,etcarnumber;
	private RelativeLayout  layrole,btnBack,laycarnumber;
	//	下底端的控件;
	private Button			btnsignup;
	//	自定义对象;
	private	User			user;
	//	自动提示框的内容;
	//	底端的自动提示框;
	private PopupWindow 	popupWindow;
	//	底端的样式对象;
	private View 			bottomView;
	//	动态的动画对象;
	private TranslateAnimation transAnimation;
	//	经纬度内容;
	private String 			lng="0",lat="0",type,address="未填";
	//
	//	线程管理函数;
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			//	关闭对话方框;
			vDialog.dismiss();
			//	判断标记内容; 
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigure.NTAG_SUCCESS:		
				//	提示符;
				Toast.makeText(mContext, R.string.signup_success,Toast.LENGTH_LONG).show();
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			//	关闭线程的操作;
			closeThread();
			finish();
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
		setContentView(R.layout.act_signup_n);
		initView();
		initEvent();
	}
	//	进行相应的控件初始化;
	private void initView(){
		btnBack=(RelativeLayout) findViewById(R.id.btnBack);
		ivBack =(TextView) findViewById(R.id.ivBack);
		tvTopic=(TextView) findViewById(R.id.tvTopic);
		etname =(EditText) findViewById(R.id.etname);
		etpwd  =(EditText) findViewById(R.id.etpwd);
//		etconfirm=(EditText) findViewById(R.id.etconfirm);
		etphone	 =(EditText) findViewById(R.id.etphone);
		etcarnumber=(EditText) findViewById(R.id.etcarnumber);
		layrole	= (RelativeLayout) findViewById(R.id.layrole);
		tvrole	= (TextView) findViewById(R.id.tvrole);
		//	底端按钮;
		btnsignup=(Button) findViewById(R.id.btnsignup);
		//	车牌号;
		laycarnumber=(RelativeLayout) findViewById(R.id.laycarnumber);
		
	}
	//	进行相应的事件初始化;
	private void initEvent(){
		//	上下文的加载;
		if(mContext==null){
			mContext=VSignupActivity.this;
		}
		//	标题栏;
		tvTopic.setText("注册信息填写");
		ivBack.setBackgroundResource(R.drawable.icon_back);
		//	返回键;
		btnBack.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {  
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
				return true;
			}
		});
		//	角色选择事件监听;
		layrole.setOnClickListener(this);
		//	注册按钮的事件监听;
		btnsignup.setOnClickListener(this);
		mtGetOrPostHelper	=	new MTGetOrPostHelper();
	}
	
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	注册按钮;
		case R.id.btnsignup:
			//	获得注册的基础信息;
			getinfo();
			break;
		//	定位按钮;
		case R.id.layrole:
			initPopupWindow(view);
			break;
		default:
			break;
		}
	}
	//	进行底端对话框显示的内容;
	@SuppressWarnings("deprecation")
	private void initPopupWindow(View parent) {  
	   	if (popupWindow == null) {
	   		//	自定义的布局;
	   		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
	   		//	底端的控件;
			bottomView  = mLayoutInflater.inflate(R.layout.act_popupwindow_signup_n, null);
			//	获取相应显示页面的内容;
			popupWindow = new PopupWindow(bottomView,ViewGroup.LayoutParams.FILL_PARENT,300);
	   	}
	   	//	获取色彩背景;
	   	ColorDrawable cd = new ColorDrawable(0x000000);
	   	popupWindow.setBackgroundDrawable(cd); 
	   	//产生背景变暗效果
	    WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
	   	  
	   	popupWindow.setOutsideTouchable(true);
	   	popupWindow.setFocusable(true);
	   	//	动画的切入;
	    transAnimation= new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
	    transAnimation.setInterpolator(new AccelerateInterpolator());
	    transAnimation.setDuration(200);
	   	
	   	
	   	popupWindow.showAtLocation((View)parent.getParent(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	   	bottomView.startAnimation(transAnimation);
    	popupWindow.update();
    	//	返回键;
    	RelativeLayout btnback	= (RelativeLayout) bottomView.findViewById(R.id.btnBack);
    	//	回收员;
    	RelativeLayout btndriver= (RelativeLayout) bottomView.findViewById(R.id.btndriver);
    	//	回收点;
    	RelativeLayout btnnode	= (RelativeLayout) bottomView.findViewById(R.id.btnnode);
    	//	用户;
    	RelativeLayout btncommon= (RelativeLayout) bottomView.findViewById(R.id.btncommon);
    	
    	//	进行相应的事件监听;
    	btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
    	btndriver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				tvrole.setText(R.string.choose_driver);
				laycarnumber.setVisibility(View.VISIBLE);
				type="driver";
				popupWindow.dismiss();
			}
		});
    	btnnode.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View view) {
    			tvrole.setText(R.string.choose_node);
    			laycarnumber.setVisibility(View.GONE);
    			type="node";
    			mIntent=new Intent(mContext, VGeoFenceActivity.class);
    			startActivityForResult(mIntent, 1);
    			popupWindow.dismiss();
    		}
    	});
    	btncommon.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View view) {
    			tvrole.setText(R.string.choose_common);
    			laycarnumber.setVisibility(View.GONE);
    			type="common";
    			popupWindow.dismiss();
    		}
    	});
    	
    	
    	popupWindow.setOnDismissListener(new OnDismissListener(){
    	
    	//在dismiss中恢复透明度
    	public void onDismiss(){
    			WindowManager.LayoutParams lp=getWindow().getAttributes();
    			lp.alpha = 1f;
    			getWindow().setAttributes(lp);	
    		}			
    	});
	} 
	//	回复的内容信息;
	@Override
	protected void onActivityResult(int req, int res, Intent data) {
		super.onActivityResult(req, res, data);
		Bundle bundle=data.getExtras();
		//	地图注册页面的返回值;
		if(req==1&&res==1){
			address=bundle.getString("address").trim();
			if(Double.parseDouble(bundle.getString("lng"))!=-1&&Double.parseDouble(bundle.getString("lat"))!=-1){				
				lng=bundle.getString("lng");
				lat=bundle.getString("lat");
			}
			if(!address.equals("")){				
				Toast.makeText(mContext,R.string.locate_finished, Toast.LENGTH_SHORT).show();
			}
		}
	}
	//	获得信息;
	private void getinfo(){
		if(mThread==null){			
			if(user==null){
				user=new User();
			}
			user=user.getInfo(etphone, etname, etpwd, "-1", lng, lat, etcarnumber, type, address);
			if(user!=null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				mThread = new MyThread(mtGetOrPostHelper, user);
				mThread.start();
			}else Toast.makeText(mContext, "请正确输入", Toast.LENGTH_SHORT).show();
		}
	}
	//	线程的主要内容;
	class MyThread extends Thread{
		private MTGetOrPostHelper mtGetOrPostHelper;
		private User 	 user;

		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,User user) {
			this.mtGetOrPostHelper	= mtGetOrPostHelper;
			this.user				= user;
		}
		
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			String  url		 =	null;
			String  param	 =	null;
			String  response = 	null;
			int     nFlag	 = 	MTConfigure.NTAG_FAIL;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			
				url= "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/user_info";
				try {
					param	 =	
					"opertype=2&" +
					"id="+ user.getId()+ "&" +
					"nickname=" + URLEncoder.encode(user.getNickname(),"utf-8")+ "&" +
					"password=" + URLEncoder.encode(user.getPassword(),"utf-8")+ "&" +
					"status=" + user.getStatus()+ "&" +
					"lng=" +user.getLng()+ "&" +
					"lat=" + user.getLat()+ "&" +
					"car_number=" + URLEncoder.encode(user.getCar_number(),"utf-8")+ "&" +
					"type=" + user.getType()+ "&" +
					"address=" + URLEncoder.encode(user.getAddress(),"utf-8")
					;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}				
				
			response		 =	mtGetOrPostHelper.sendGet(url,param);
			// 判断种类;
			if(!response.equals("null")){
				nFlag	=	MTConfigure.NTAG_SUCCESS;
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}
	}
}

package com.view;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.hqbs.app.R;
import com.model.entity.MEDriver;
import com.model.entity.MENode;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.view.MTEditTextWithDel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VSignupActivity extends Activity implements OnClickListener{
	//	上下文内容;
	private Context mContext;
	private Intent  mIntent;
	//	控件内容;
	private TextView vBack,vTopic;
	private Button vResign,vLocation;
	private MTEditTextWithDel vname,vtel,vpwd,vcarnumber,vnodeaddress;
	private Spinner vkind;
	private RelativeLayout laycar,laynode;
	//	参数内容;
//	private MTConfigure			mtConfigure;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private String[] kinds={"D","N"};
	private String 	 kind;
	private double	 lng,lat;
	private MEDriver driver;
	private MENode	 node;
	//	线程管理函数;
	private MyThread		mThread=null;// 自定义的上传线程;
	private ProgressDialog  vDialog;	// 对话框;
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
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_LONG).show();
				finish();
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
//			mtConfigure.closeThread(mThread);
			closeThread();
		}
	};
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//	构造函数;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_signup);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		vBack	=(TextView) findViewById(R.id.btnBack);
		vResign =(Button) findViewById(R.id.btnResign);
		vLocation=(Button) findViewById(R.id.btnLocation);
		vname	=(MTEditTextWithDel) findViewById(R.id.etname);
		vtel	=(MTEditTextWithDel) findViewById(R.id.ettel);
		vpwd	=(MTEditTextWithDel) findViewById(R.id.etpwd);
		vcarnumber	 =(MTEditTextWithDel) findViewById(R.id.etcarnumber);
		vnodeaddress=(MTEditTextWithDel) findViewById(R.id.etnodeaddress);
		vkind	=(Spinner) findViewById(R.id.spkind);
		laycar	=(RelativeLayout) findViewById(R.id.laycar);
		laynode	=(RelativeLayout) findViewById(R.id.laynode);
	}
	//	初始化事件;
	private void initEvent(){
		//	上下文内容;
		mContext	=	VSignupActivity.this;
//		mtConfigure	=	new MTConfigure();
		//	类型为首个;
		kind		=	kinds[0];
		//	初始化相应的控件内容;
		vBack.setText(R.string.no);
		vTopic.setText(R.string.add_signup);
		vBack.setOnClickListener(this);
		vBack.setVisibility(View.VISIBLE);
		//	声明参数;
		mtGetOrPostHelper	=	new MTGetOrPostHelper();
		//	注册键的触发;
		vResign.setOnClickListener(this);
		vLocation.setOnClickListener(this);
		//	身份选择;
		vkind.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				kind=kinds[position];
				switch (position) {
				case 0:
					laycar.setVisibility(View.VISIBLE);
					laynode.setVisibility(View.GONE);
					break;
				case 1:
					laycar.setVisibility(View.GONE);
					laynode.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
		});
	}
		
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	返回按钮;
		case R.id.btnBack:
			finish();
			break;
		//	注册按钮;
		case R.id.btnResign:
			//	进行信息获取;
			getInfo(kind);
			break;
		//	定位按钮;
		case R.id.btnLocation:
			mIntent=new Intent(mContext, VGeoFenceActivity.class);
			startActivityForResult(mIntent, 1);
			break;
		default:
			break;
		}
	}
	//	获得信息内容;
	private void getInfo(String kind){
		if(kind.equals("D")){
			if(driver==null){
				driver=new MEDriver();
			}
			driver=driver.getDriverInfo(vpwd, vcarnumber, vtel, vname);
			if(driver!=null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				mThread=new MyThread(mtGetOrPostHelper, "D", driver, null);
				mThread.start();
			}else Toast.makeText(mContext, "请正确输入", Toast.LENGTH_SHORT).show();
			
		}else if(kind.equals("N")){
			if(node==null){
				node=new MENode();
			}
			node=node.getNodeInfo(vpwd, vname, vtel, vnodeaddress, lng, lat);
			if(node!=null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				mThread=new MyThread(mtGetOrPostHelper, "N", null, node);
				mThread.start();
			}else Toast.makeText(mContext, "请正确输入", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onActivityResult(int req, int res, Intent data) {
		super.onActivityResult(req, res, data);
		Bundle bundle=data.getExtras();
		//	地图注册页面的返回值;
		if(req==1&&res==1){
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
	//	线程的完成;
	//进行登录的线程;
	class MyThread extends Thread{
		private MTGetOrPostHelper mtGetOrPostHelper;
		private MEDriver driver;
		private MENode 	 node;
		private String 	 oper;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String oper,MEDriver driver,MENode node) {
			this.mtGetOrPostHelper	=mtGetOrPostHelper;
			this.oper				=oper;
			this.driver				=driver;
			this.node				=node;
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
			if(oper.equals("N")){
				url= "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/node_info";
				try {
					param		 =	
					"opertype=7&" +
					"id="+ node.getId()+ "&" +
					"password=" + node.getPassword()+ "&" +
					"lng=" + node.getLng()+ "&" +
					"lat=" + node.getLat()+ "&" +
					"name=" + URLEncoder.encode(node.getName(),"utf-8")+ "&" +
					"tel=" + node.getTel()+ "&" +
					"address=" + URLEncoder.encode(node.getAddress(),"utf-8")
					;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}else 
			if(oper.equals("D")){
				url="http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/driver_info";
				try {
					param		 =	
					"opertype=7&" +
					"id="+ driver.getId()+ "&" +
					"password=" + driver.getPassword()+"&"+
					"carnumber=" + URLEncoder.encode(driver.getCarNumber(),"utf-8")+"&"+
					"tel=" + driver.getTel()+"&"+
					"lng=" + driver.getLng()+"&"+
					"lat=" + driver.getLat()+"&"+
					"name=" + URLEncoder.encode(driver.getName(),"utf-8")
					;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			response		 =	mtGetOrPostHelper.sendGet(url,param);

			// 判断种类;
			if(response!=null){
				//	去除多余符号;
				response.trim();
				nFlag	=	MTConfigure.NTAG_SUCCESS;
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}
	}
}

package com.view;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.hqbs.app.R;
import com.model.entity.MEAccount;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.view.MTEditTextWithDel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VWelActivity extends Activity implements OnClickListener{
	private Context  mContext;
	private Intent	 mIntent;
	//	下底部的标签;
	private TextView vsignup;
	//	用户账号+密码;
	private MTEditTextWithDel vid,vpwd;
	//	登录的按钮;
	private Button vlogin;
	//	进行网络的使用;
	// 03.与交互相关的Hanlder内容;
	private MyThread		mThread=null;// 自定义的上传线程;
	private ProgressDialog  vDialog;	// 对话框;	
	private MTGetOrPostHelper mGetOrPostHelper; // 与网络有关的帮助类;
	private MEAccount		meAccount;
	
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
				//	id编号;
				String id=bundle.getString("id");
				//	发送页面;
				sendAct(id, bundle);
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
		}
	};
	
	
	//	进行相应的页面跳转;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wel);
		initView();
		initEvent();
	}
	//	事件的初始化;
	private void initView(){
		vsignup=(TextView) findViewById(R.id.btnSignup);
		vid=(MTEditTextWithDel) findViewById(R.id.etid);
		vpwd=(MTEditTextWithDel) findViewById(R.id.etpwd);
		vlogin=(Button) findViewById(R.id.btnlogin);
	}
	
	//	控件的初始化;
	private void initEvent(){
		if(mContext==null){			
			mContext=VWelActivity.this;
		}
		vlogin.setOnClickListener(this);
		vsignup.setText(Html.fromHtml("若无账号请选择&nbsp;&nbsp;<a href=\"\">注  册</a>"));	
		vsignup.setOnClickListener(this);
		mGetOrPostHelper=new MTGetOrPostHelper();
	}
	
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	////
	//进行登录的线程;
	class MyThread extends Thread{
		private MTGetOrPostHelper mtGetOrPostHelper;
		private MEAccount meAccount;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,MEAccount meAccount) {
			this.mtGetOrPostHelper	=mtGetOrPostHelper;
			this.meAccount			=meAccount;
		}
		
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			String  url		 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/account_info";
			String  param	 =	"opertype=1&accountid="+ meAccount.getAid()+ "&password=" + meAccount.getPwd();
			String  response = 	mtGetOrPostHelper.sendGet(url,param);
			int     nFlag	 = 	MTConfigure.NTAG_FAIL;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();

			// 判断种类;
			if(response!=null){
				//	去除多余符号;
				response.trim();
				try {
					JSONArray array = 	new JSONArray(response);
					int 	  i		=	0;
					JSONObject obj  = 	null;
					do {
						try {								
							obj		=	array.getJSONObject(i);
							//	插入到首选项的配置文件当中;
							nFlag	= 	MTConfigure.NTAG_SUCCESS;
							String id	=	obj.getString("id");
							String name	=	obj.getString("name");
							String password=obj.getString("password");
							bundle.putString("id", id);
							bundle.putString("name", name);
							bundle.putString("pwd", password);
							i++;
						} catch (Exception e) {
							obj		=	null;
							break;
						}
					} while (obj!=null);
				} catch (JSONException e) {
					nFlag			=	MTConfigure.NTAG_FAIL;
				}
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}
	}
			
	
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		
		switch (nVid) {
		//	登录按钮;
		case R.id.btnlogin:
			if(mThread==null){
				if(meAccount==null){
					meAccount=new MEAccount();
				}
				meAccount=meAccount.getAccountInfo(vid, vpwd);
				// 账户类型;
				if(meAccount!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					mThread=new MyThread(mGetOrPostHelper, meAccount);
					mThread.start();
				}				
			}
			
			break;
		//	注册按钮;
		case R.id.btnSignup:
			mIntent=new Intent(mContext, VSignupActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
	//	进行跳转
	private void sendAct(String id,Bundle bundle){
		String head=id.substring(0, 1);
		if(head.equalsIgnoreCase("n")){
			mIntent=new Intent(mContext, VNodeMenuActivity.class);
		}else if(head.equalsIgnoreCase("d")){
			mIntent=new Intent(mContext, VDriverMenuActivity.class);
		}
		mIntent.putExtras(bundle);
		startActivity(mIntent);
		finish();
		return ;
	}
}


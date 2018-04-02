package com.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.entity.User;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTIDHelper;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VWelActivity extends Activity implements OnClickListener{
	private Context  mContext;
	private Intent	 mIntent;
	//	下底部的标签;
	private TextView ivsignup;
	private RelativeLayout btnsignup;
	//	用户账号+密码;
	private MTEditTextWithDel vid,vpwd;
	//	登录的按钮;
	private Button vlogin;
	//	进行网络的使用;
	// 03.与交互相关的Hanlder内容;
	private MyThread		mThread=null;// 自定义的上传线程;
	private ProgressDialog  vDialog;	// 对话框;	
	private MTGetOrPostHelper mGetOrPostHelper; // 与网络有关的帮助类;
	private User			user;
	private MTIDHelper		idHelper;
	
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
				//	发送页面;
				sendAct(bundle);
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.error_login,Toast.LENGTH_SHORT).show();
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
		if(mContext==null){			
			mContext=VWelActivity.this;
		}
		//	选择操作类型;
		chooseOper(mContext);
		//	布置布局页面;
		setContentView(R.layout.act_wel_n);
		//	初始化控件值;
		initView();
		//	初始化事件值;
		initEvent();
	}
	//	事件的初始化;
	private void initView(){
		//	注册按钮;
		ivsignup	=(TextView) findViewById(R.id.ivSignup);
		//	账户号码;
		vid		=(MTEditTextWithDel) findViewById(R.id.etid);
		//	账户密码;
		vpwd	=(MTEditTextWithDel) findViewById(R.id.etpwd);
		//	登录按钮;
		vlogin	=(Button) findViewById(R.id.btnlogin);
		//	注册按钮;
		btnsignup=(RelativeLayout) findViewById(R.id.btnsignup);
	}
	//	选择操作的类型;
	private void chooseOper(Context context){
		idHelper=new MTIDHelper(context, "userinfo", Context.MODE_APPEND);
		boolean flag=idHelper.isIdExist();
		if(flag){
			User	 user	=idHelper.getUser();
			//	id编号;
			String   id		=user.getId();
			String   pwd	=user.getPassword();
			String   name	=user.getNickname();
			String   role	=user.getType();
			if(role.equalsIgnoreCase("node")){
				mIntent=new Intent(mContext, VNodeMenuActivity.class);
			}else if(role.equalsIgnoreCase("driver")){
				mIntent=new Intent(mContext, VDriverMenuActivity.class);
			}else mIntent=new Intent(mContext, VAdminMenuActivity.class);
			
			Bundle bundle=new Bundle();
			bundle.putString("id", id);
			bundle.putString("pwd", pwd);
			bundle.putString("name", name);
			mIntent.putExtras(bundle);
			startActivity(mIntent);
			finish();
		}
	}
	
	//	控件的初始化;
	private void initEvent(){
		vlogin.setOnClickListener(this);
		ivsignup.setText(Html.fromHtml("若无账号请选择&nbsp;&nbsp;<a href=\"\">注  册</a>"));	
		btnsignup.setOnClickListener(this);
		mGetOrPostHelper=new MTGetOrPostHelper();
	}
	
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//进行登录的线程;
	class MyThread extends Thread{
		private MTGetOrPostHelper mtGetOrPostHelper;
		private User user;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,User user) {
			this.mtGetOrPostHelper	=mtGetOrPostHelper;
			this.user			=user;
		}
		
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			String  url		 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/user_info";
			String  param	 =	"opertype=1&id="+ user.getId()+ "&password=" + user.getPassword();
			String  response = 	mtGetOrPostHelper.sendGet(url,param);
			int     nFlag	 = 	MTConfigure.NTAG_FAIL;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			// 判断种类;
			if(!response.equals("null")){
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
							String name	=	obj.getString("nickname");
							String password=obj.getString("password");
							String role=obj.getString("role");
							//	进行数据的插入;
							bundle.putString("id", id);
							bundle.putString("name", name);
							bundle.putString("pwd", password);
							bundle.putString("role", role);
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
				if(user==null){
					user=new User();
				}
				user=user.getInfo(vid, vpwd);
				// 账户类型;
				if(user!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					mThread=new MyThread(mGetOrPostHelper, user);
					mThread.start();
				}else Toast.makeText(mContext, R.string.error_login, Toast.LENGTH_SHORT).show();			
			}
			
			break;
		//	注册按钮;
		case R.id.btnsignup:
			mIntent=new Intent(mContext, VSignupActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
	//	进行跳转
	private void sendAct(Bundle bundle){
		//	id编号;
		String   id		=bundle.getString("id");
		String   pwd	=bundle.getString("pwd");
		String   name	=bundle.getString("name");
		String   role	=bundle.getString("role");
		User	 user	=new User(id, name, pwd, null, null, null, null, role);
		
		//	设置公用对象;
		idHelper.setUser(user);
		//	设置默认的身份;
		idHelper.setIdentity();
		
		if(role.equalsIgnoreCase("node")){
			mIntent=new Intent(mContext, VNodeMenuActivity.class);
		}else if(role.equalsIgnoreCase("driver")){
			mIntent=new Intent(mContext, VDriverMenuActivity.class);
		}else {
			mIntent=new Intent(mContext, VAdminMenuActivity.class);
		}
		mIntent.putExtras(bundle);
		startActivity(mIntent);
		finish();
		return ;
	}
}


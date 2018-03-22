package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.entity.MEPerson;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTIDHelper;
import com.model.tool.view.MTEditTextWithDel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VDriverMenuActivity extends Activity implements OnClickListener,OnFocusChangeListener{
	private Context   mContext;
	private Intent    mIntent;
	private Bundle	  mbundle;
	private Resources mResources;
	//	标题按钮;
	private TextView  vTopic,vBack;
	/*	下方三栏按钮 */
	private ImageView 		vLeft,vMiddle,vRight;
	private RelativeLayout 	lLeft,lMiddle,lRight;
	private MTEditTextWithDel etSearch;
	private ImageView 		vSearch;
	private ProgressDialog  vDialog;	 // 对话框;
	private String [] t_top={"任务单","完成单"};
	private int [] 	  t_sel={R.drawable.tab_bar_icon_list_sel,R.drawable.tab_bar_icon_bat_sel};
	private int [] 	  t_nar={R.drawable.tab_bar_icon_list_nar,R.drawable.tab_bar_icon_bat_nar};
	private int 	  btnposition=0;
	/*	上方的搜索栏*/
	private RelativeLayout  laysearch;
	private ArrayAdapter<String> mAdapter;
	private ListView  		vListData;

	/*	自定义的类*/
	private MTConfigure		mtConfigure;
	private MTIDHelper		idHelper;
	private MyThread		mThread=null;// 自定义的上传线程;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	/*	参数的内容*/
	private String 			driverId;
	private String			driverName;
	/*	handler句柄的控制类*/
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			ArrayList<String> 				list1=(ArrayList<String>) bundle.getSerializable("list1");
			ArrayList<Map<String, String>>  list2=(ArrayList<Map<String, String>>) bundle.getSerializable("list2");
			//	关闭对话方框;
			vDialog.dismiss();
			//	判断标记内容; 
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigure.NTAG_SUCCESS:		
				//	提示符;
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_LONG).show();
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			loadData(list1,list2);
			//	关闭线程的操作;
			closeThread();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_menu);
		initView();
		initEvent();
		//	进行全数据的查询;
		queryDataFromServer("driver="+driverId+"&state=0", btnposition, "queryall",null);
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			Builder builder=new Builder(mContext);
			builder.setTitle(R.string.exit);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					MEPerson person=new MEPerson(null, null);
					idHelper.setPerson(person);
					idHelper.setIdentity();
					mIntent=new Intent(mContext, VWelActivity.class);
					startActivity(mIntent);
					finish();
				}
			});
			builder.setNegativeButton(R.string.no, null);
			builder.create();
			builder.show(); 
			break;
		case R.id.layleft:
			etSearch.setText("");
			btnposition=0;
			queryDataFromServer("driver="+driverId+"&state=0", btnposition, "queryall",null);
			break;
		case R.id.laymiddle:
			etSearch.setText("");
			btnposition=1;	
			queryDataFromServer("driver="+driverId+"&state=1", btnposition, "queryall",null);
			break;
		case R.id.isearch:
			String id=mtConfigure.getEditText(etSearch);
			if(id!=null){				
				switch (btnposition) {
				//	条件查询-任务单;
				case 0:
					queryDataFromServer("model="+id+"&driver="+driverId+"&state=0", btnposition, "queryitem",null);	
					break;
				//	条件查询-完成单;
				case 1:
					queryDataFromServer("model="+id+"&driver="+driverId+"&state=1", btnposition, "queryitem",null);
					break;
				default:
					break;
				}
			}else Toast.makeText(mContext, "不可为空", Toast.LENGTH_SHORT).show();
			
			break;
		default:
			break;
		}
		initChooseState(btnposition);
	}
	//	初始化控件;
	private void initView(){
		//	标题栏;
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vTopic.setText("任务列表");
		/*三个钮的初始化状态*/
		//	三个按键的内容;
		vSearch	=(ImageView) findViewById(R.id.isearch);
		vLeft	=(ImageView) findViewById(R.id.btnleft);
		vMiddle	=(ImageView) findViewById(R.id.btnmiddle);
		vRight	=(ImageView) findViewById(R.id.btnright);
		lLeft	=(RelativeLayout) findViewById(R.id.layleft);
		lMiddle	=(RelativeLayout) findViewById(R.id.laymiddle);
		lRight	=(RelativeLayout) findViewById(R.id.layright);
		//	初始化状态;
		btnposition=0;
		//	自身属性内容;
		laysearch=(RelativeLayout) findViewById(R.id.laysearch);
		vBack	 =(TextView) findViewById(R.id.btnBack);
		etSearch =(MTEditTextWithDel) findViewById(R.id.etsearch);
		vListData=(ListView) findViewById(R.id.listShow);
		
	}
	//	初始化事件;
	private void initEvent(){
		//	上下文内容信息;
		if(mContext==null){
			mContext=VDriverMenuActivity.this;
		}
		//	资源文件的内容;
		mResources	= getResources();
		mtGetOrPostHelper= new MTGetOrPostHelper();
		mtConfigure	= new MTConfigure();
		//	身份标签;
		idHelper    = new MTIDHelper(mContext, "userinfo", Context.MODE_APPEND);
		/*	数值参数的变量*/
		mIntent 	= getIntent();
		mbundle 	= mIntent.getExtras();
		driverId	= mbundle.getString("id");
		driverName	= mbundle.getString("name");
		
		vTopic.setText(t_top[0]);
		vLeft.setBackgroundResource(t_sel[0]);
		vMiddle.setBackgroundResource(t_nar[1]);
		vRight.setVisibility(View.GONE);
		lRight.setVisibility(View.GONE);
		vBack.setVisibility(View.VISIBLE);
		vBack.setText("退出");
		vBack.setOnClickListener(this);
		lLeft.setOnClickListener(this);
		lMiddle.setOnClickListener(this);
		//	探索栏的事件监听;
		etSearch.setOnFocusChangeListener(this);
		vSearch.setOnClickListener(this);
	}
	//	进行图像的初始化;
	private void initChooseState(int position){
		switch (position) {
		case 0:
			vLeft.setBackgroundResource(t_sel[0]);
			vMiddle.setBackgroundResource(t_nar[1]);
			break;
		case 1:
			vLeft.setBackgroundResource(t_nar[0]);
			vMiddle.setBackgroundResource(t_sel[1]);
			break;
		default:
			break;
		}
		vTopic.setText(t_top[position]);
	}
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
	//	按下事件;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
	    }
		return super.onKeyDown(keyCode, event);
	}
	//	进行相应的状态信息的内容;
	//	从服务器端进行数据的查询;
	private void queryDataFromServer(String param,int position,String oper,SQLiteDatabase mDB){
		if(mThread==null){
			final CharSequence strDialogTitle = getString(R.string.wait);
			final CharSequence strDialogBody = getString(R.string.doing);
			vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
			mThread = new MyThread(mtGetOrPostHelper, param,position,oper,mDB);
			mThread.start();
		}
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param2,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		private int position;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,int position,String oper,SQLiteDatabase mDB) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param2			  =param;
			this.oper			  =oper;
			this.position		  =position;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  url		 =	null;
			String  param	 =	null;
			String  response = 	null;
			//	全局定位的操作符号;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			ArrayList<String>				list1=new ArrayList<String>();
			ArrayList<Map<String, String>>  list2=new ArrayList<Map<String,String>>();
			int 	nSize	 = 0;
			url				 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/delivery_info";
			///	网络请求块;
			//	查询类别的判定;
			switch (position) {
			//	回收单;
			case 0:
			case 1:
				//	全信息查询;
				if(oper.equals("queryall")){
					param			 =  "opertype=3&"+param2;
				}else 
				//	单信息查询;
				if(oper.equals("queryitem")){
					param			 =  "opertype=4&"+param2;
				}
				//	跳出循环;
				break;
			default:
				//	跳出循环;
				break;
			}
			////返回结果
			response		 =	mtGetOrPostHelper.sendGet(url,param);	
			if(response!=null){
				response.trim();
				try {
					array 	 = new JSONArray(response);
				} catch (JSONException e) {
					array	 = null;
				}
				if(array!=null){
					nSize	 = array.length();
					if(nSize!=0){
						int 	i= 0;
						switch (position) {
						//	进行回收单内容的操作;
						case 0:
						case 1:
							do {
								try {
									obj 	 = array.getJSONObject(i);

									String ownner	= obj.getString("ownner");
									String model	= obj.getString("model");
									String price	= obj.getString("price");
									String count	= obj.getString("count");
									String lng		= obj.getString("lng");
									String lat		= obj.getString("lat");
									String deadline	= obj.getString("deadline");
									String goal		= obj.getString("goal");
									String id		= obj.getString("id");
									list1.add(ownner+"    "+model+"    "+deadline);
									Map<String , String> map=new HashMap<String, String>();
									map.put("ownner", ownner);
									map.put("model", model);
									map.put("price", price);
									map.put("count", count);
									map.put("lng", lng);
									map.put("lat", lat);
									map.put("deadline", deadline);
									map.put("goal", goal);
									map.put("id", id);
									list2.add(map);
									i++;
								} catch (JSONException e) {
									obj		= null;
									break;
								}
							} while (obj!= null);
							break;
						default:
							break;
						}
					}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			bundle.putSerializable("list1", list1);
			bundle.putSerializable("list2", list2);
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
	//	进行数据的列表相应显示;
	private void loadData(ArrayList<String> list1,final ArrayList<Map<String, String>>  list2){
		//	适配器的使用;
		mAdapter=new ArrayAdapter<String>(mContext, R.layout.act_item01,R.id.content, list1);
		vListData.setAdapter(mAdapter);
		vListData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				Map<String, String>  item=list2.get(position);
				String ownner	=	item.get("ownner");
//				driverId;
//				driverName;
				String model	=	item.get("model");
				String price	=	item.get("price");
				String count	=	item.get("count");
				String lng		=	item.get("lng");
				String lat		=	item.get("lat");
				String deadline	=	item.get("deadline");
				String goal		=	item.get("goal");
				String did		=	item.get("id");
				mbundle			=	new Bundle();
				//	跳转的按钮;
				switch (btnposition) {
				//	任务单的列表
				case 0:
					//	进行数据的跳转-传递参数;
					mIntent		=	new Intent(mContext, VDeliveryDetailActivity.class);
					mbundle.putString("ownner", ownner);
					mbundle.putString("driverId", driverId);
					mbundle.putString("driverName", driverName);
					mbundle.putString("model", model);
					mbundle.putString("price", price);
					mbundle.putString("count", count);
					mbundle.putString("lng", lng);
					mbundle.putString("lat", lat);
					mbundle.putString("deadline", deadline);
					mbundle.putString("goal", goal);
					mbundle.putString("id", did);
					
					break;
				//	完成单的列表;
				case 1:
					mIntent=new Intent(mContext, VDeliveryHistoryActivity.class);
					mbundle.putString("id", did);
					break;
				default:
					break;
				}
				mIntent.putExtras(mbundle);
				startActivity(mIntent);				
			}
		});
	}
	//	关闭线程内容;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//	重新加载;
	@Override
	protected void onRestart() {
		super.onRestart();
		//	重新进行数据加载;
		switch (btnposition) {
		//	查询所有的回收单信息;
		case 0:
			//	进行全数据的查询;
			queryDataFromServer("driver="+driverId+"&state=0", btnposition, "queryall",null);
			break;
		//	查询所有数据信息;
		case 1:
			queryDataFromServer("driver="+driverId+"&state=1", btnposition, "queryall",null);
			break;
		default:
			break;
		}
	}
}

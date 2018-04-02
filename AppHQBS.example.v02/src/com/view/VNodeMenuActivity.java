package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.entity.User;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTIDHelper;
import com.model.tool.system.MTSQLiteHelper;
import com.model.tool.view.MTEditTextWithDel;
import com.model.tool.view.MTMenuBar;
import com.model.tool.view.SwipeLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VNodeMenuActivity extends Activity implements OnClickListener,OnTouchListener{
	//	上下文的内容;
	private Context		   mContext;
	private Intent		   mIntent;
	/*上端的按钮*/
	private RelativeLayout btnback,btnadd;
	private TextView	   tvtopic;
	/*中间布局*/
	//	可删除的输入框;
	private MTEditTextWithDel	etsearch;
	//	查询按钮;
	private RelativeLayout btnsearch;
	/*底端的按钮*/
	private RelativeLayout btnleft,btnmiddle,btnright;
	private TextView	   ivleft,ivmiddle,ivright,ivback,ivadd;
	//	下角标;
	private int 	  	   btnposition=0;
	/*自定义的对象*/
	private MTMenuBar	   mtMenuBar;
	private MTIDHelper	   idHelper;
	private MTConfigure	   mtConfigure;
	//	站点id编号,角色类别;
	private String 		   owner,role,param;
	/*线程有关的参数*/
	private ProgressDialog vDialog;	 // 对话框;
	private MyThread	   mThread=null;// 自定义的上传线程;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	/*数据库的内容*/
	private MTSQLiteHelper 	    mSqLiteHelper;// 数据库的帮助类;
	private SQLiteDatabase		sqlDB;
	/*前端显示控件*/
	/**列表控件*/
	private ListView 	   listview;
	/**当前滑动的下标*/
	private MTListCollectionAdapter	adapter0;
	private MTListDriverAdapter		adapter1;
	private MTListDeliveryAdapter	adapter2;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			ArrayList<Map<String, String>>  listnode=(ArrayList<Map<String, String>>) bundle.getSerializable("listnode");
			//	关闭对话方框;
			vDialog.dismiss();
			//	判断标记内容; 
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigure.NTAG_SUCCESS:		
				//	提示符;
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_SHORT).show();
				break;
			//	结果错误信号;
			case MTConfigure.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			//	加载数据信息;
			loadData(listnode,btnposition);
			//	关闭线程的操作;
			closeThread();
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_node_menu_n);
		//	初始化控件;
		initView();
		//	初始化事件;
		initEvent();
		//	获取最开始数据
		//	回收单全信息查询;
		param="opertype=1&owner="+owner;
		queryDataFromServer(param, btnposition,null);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		switch (btnposition) {
		//	回收单全信息查询;
		case 1:
			param="opertype=3&id="+owner;
			queryDataFromServer(param, btnposition,sqlDB);
			break;
		case 0:	
		case 2:
			param="opertype=1&owner="+owner;
			queryDataFromServer(param, btnposition,null);
			break;
		default:
			break;
		}
	}
	private void initView(){
		//	返回键的按钮;
		btnback=(RelativeLayout) findViewById(R.id.btnBack);
		ivback =(TextView) findViewById(R.id.ivBack);
		//	功能键;
		btnadd =(RelativeLayout) findViewById(R.id.btnFunction);
		ivadd  =(TextView) findViewById(R.id.ivFunction);
		//	标题按钮;
		tvtopic=(TextView) findViewById(R.id.tvTopic);
		//	下方的三个按钮;
		//	左键按钮;
		ivleft	=(TextView) findViewById(R.id.ivleft);
		//	中间按钮;
		ivmiddle=(TextView) findViewById(R.id.ivmiddle);
		//	右键按钮;
		ivright =(TextView) findViewById(R.id.ivright);
		//	点击触发的按钮;
		btnleft	 =(RelativeLayout) findViewById(R.id.btnleft);
		btnmiddle=(RelativeLayout) findViewById(R.id.btnmiddle);
		btnright =(RelativeLayout) findViewById(R.id.btnright);
		//	列表显示;
		listview =(ListView) findViewById(R.id.listmenu);
		//	按钮内容;
		etsearch =(MTEditTextWithDel) findViewById(R.id.etid);
		btnsearch=(RelativeLayout) findViewById(R.id.btnsearch);
	}
	private void initEvent(){
		if(mContext==null){
			mContext=VNodeMenuActivity.this;
		}
		//	自定义部分的内容;
		mtGetOrPostHelper=new MTGetOrPostHelper();
		mtConfigure		 =new MTConfigure();
		//	数据库的声明;
		mSqLiteHelper	 = new MTSQLiteHelper(mContext);
		sqlDB			 = mSqLiteHelper.getmDB();
		//	初始值的内容;
		btnposition=0;
		//	身份校验
		idHelper	= new MTIDHelper(mContext, "userinfo", Context.MODE_APPEND);
		btnadd.setVisibility(View.VISIBLE);
		//	下方列单的内容;
		mtMenuBar = new MTMenuBar(ivleft, ivmiddle, ivright,ivadd,etsearch);
		mtMenuBar.setBottomlayout(btnposition, tvtopic);
		mtMenuBar.setTopButton(btnposition);
		mtMenuBar.setSearchTip(btnposition);
		//	增加事件监听;
		btnleft.setOnClickListener(this);
		btnmiddle.setOnClickListener(this);
		btnright.setOnClickListener(this);
		//	返回键的内容;
		ivback.setBackgroundResource(R.drawable.icon_add_l);
		//	返回键的事件;
		btnback.setOnTouchListener(this);
		//	增加键的事件;
		btnadd.setOnTouchListener(this);
		//	搜索按钮添加事件监听;
		btnsearch.setOnClickListener(this);
		//	进行身份信息的获取;
		initIDinfo();
	}
	//	获取相应的身份信息;
	private void initIDinfo(){
		idHelper=new MTIDHelper(mContext, "userinfo", Context.MODE_APPEND);
		boolean flag=idHelper.isIdExist();
		if(flag){
			owner=idHelper.getUseid();
			role=idHelper.getUserole();
			Log.i("MyLog", "角色:"+role);
		}
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnleft:
			btnposition=0;
			//	查询输入框清空;
			etsearch.setText("");
			mtMenuBar.setBottomlayout(btnposition, tvtopic);
			mtMenuBar.setTopButton(btnposition);
			mtMenuBar.setSearchTip(btnposition);
			//	回收单全信息查询;
			param="opertype=1&owner="+owner;
			queryDataFromServer(param, btnposition,null);
			break;
		case R.id.btnmiddle:
			btnposition=1;
			//	查询输入框清空;
			etsearch.setText("");
			mtMenuBar.setBottomlayout(btnposition, tvtopic);
			mtMenuBar.setTopButton(btnposition);
			mtMenuBar.setSearchTip(btnposition);
			//	全信息查询;
			param="opertype=3&id="+owner;
			queryDataFromServer(param, btnposition,sqlDB);
			break;
		case R.id.btnright:
			btnposition=2;
			//	查询输入框清空;
			etsearch.setText("");
			mtMenuBar.setBottomlayout(btnposition, tvtopic);
			mtMenuBar.setTopButton(btnposition-2);
			mtMenuBar.setSearchTip(btnposition);
			//	全信息查询;
			param="opertype=1&owner="+owner;
			queryDataFromServer(param, btnposition,null);
			break;
		case R.id.btnsearch:
			String id=mtConfigure.getEditText(etsearch);
			if(id!=null){				
				switch (btnposition) {
				case 1:
					param="opertype=4&id="+owner+"&tel="+id;
					break;
				case 0:
				case 2:
					param="opertype=2&owner="+owner+"&id="+id;	
					break;
				default:
					break;
				}
				queryDataFromServer(param, btnposition,null);
			}else Toast.makeText(mContext, "不可为空", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
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
	            	Builder builder=new Builder(mContext);
	    			builder.setTitle(R.string.exit);
	    			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	    				@Override
	    				public void onClick(DialogInterface arg0, int arg1) {
	    					User	 user=new User(null, null, null, null, null, null, null, null);
	    					idHelper.setUser(user);
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
            	ivadd.getBackground().setAlpha(255);
            	chooseOper(btnposition);
                break;  
            case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域  
            	ivadd.getBackground().setAlpha(10);
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
	private void chooseOper(int location){
		switch (location) {
		case 0:
			mIntent=new Intent(mContext, VDeliveryAddActivity.class);
			break;
		case 1:
			mIntent=new Intent(mContext, VDriverAddActivity.class);
			break;
		case 2:
			mIntent=new Intent(mContext, VBatteryAddActivity.class);
			break;

		default:
			break;
		}
		startActivity(mIntent);
	}
	//	从服务器端进行数据的查询;
	private void queryDataFromServer(String param,int position,SQLiteDatabase sqlDB){
		if(mThread==null){
			final CharSequence strDialogTitle = getString(R.string.wait);
			final CharSequence strDialogBody = getString(R.string.doing);
			vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
			mThread=new MyThread(mtGetOrPostHelper, param,position,sqlDB);
			mThread.start();
		}
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param;
		private MTGetOrPostHelper mtGetOrPostHelper;
		private int position;
		private SQLiteDatabase sqlDB;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,int position,SQLiteDatabase sqlDB) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.position		  =position;
			this.sqlDB			  =sqlDB;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  url	 	 =	null;
			String  response = 	null;
			String  sql		 =  "";
			//	全局定位的操作符号;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array =  null;
			JSONObject obj 	 =  null;
			ArrayList<Map<String, String>>  listnode=new ArrayList<Map<String,String>>();
			///	网络请求块;
			//	查询类别的判定;
			switch (position) {
			//	回收单;
			case 0:
				url	= "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collection";
				//	跳出循环;
				break;
			//	回收员;
			case 1:
				url	= "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/user_info";
				if(sqlDB!=null){					
					sql	= "delete from driver";
					sqlDB.execSQL(sql);
				}
				break;
			//	新电池
			case 2:
				url	= "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/delivery";
				//	跳出循环;
				break;
			//	默认位置;
			default:
				break;
			}
			////返回结果
			response		 =	mtGetOrPostHelper.sendGet(url,param);	
			if(!response.equals("null")){
				try {
					array 	 = new JSONArray(response);
				} catch (JSONException e) {
					array	 = null;
				}
				if(array!=null){
					int 	i= 0;
					do {
						try {
							obj 	= array.getJSONObject(i);
							// 进行容器的装载;
							Map<String , String> map=new HashMap<String, String>();
							switch (position) {
							//	订购单的解析;
							case 0:
								//	获取相应的数据;
								String id	 = obj.getString("id");
								String owner = obj.getString("owner");
								String driver= obj.getString("driver");
								String deadline=obj.getString("deadline");
								String lng	 = obj.getString("lng");
								String lat	 = obj.getString("lat");
								String status= obj.getString("status");
								String price = obj.getString("price");
								String count = obj.getString("count");
								String goal	 = obj.getString("goal");
								String model = obj.getString("model");
								String name	 = obj.getString("name");
								//	添加至map;
								map.put("id", id);
								map.put("owner", owner);
								map.put("driver", driver);
								map.put("deadline", deadline);
								map.put("lng", lng);
								map.put("lat", lat);
								map.put("status", status);
								map.put("price", price);
								map.put("count", count);
								map.put("goal", goal);
								map.put("model", model);
								map.put("name", name);
								break;
							//	回收员信息解析;
							case 1:
								//	获取相应的数据;
								String id2	 = obj.getString("id");
								String name2 = obj.getString("name");
								String car_number	 = obj.getString("car_number");
								//	添加至map;
								map.put("id", id2);
								map.put("name", name2);
								map.put("car_number", car_number);
								if(sqlDB!=null){									
									sql		= "insert into driver (id,carnumber,name,state) values ('"+id2+"','"+car_number+"','"+name2+"','1')";
									//	数据库的插入;
									sqlDB.execSQL(sql);
								}
								break;
							//	新电池信息解析;
							case 2:
								String id3	 	 = obj.getString("id");
								String owner3 	 = obj.getString("owner");
								String driver3	 = obj.getString("driver");
								String lng3	 	 = obj.getString("lng");
								String lat3	 	 = obj.getString("lat");
								String deadline3 = obj.getString("deadline");
								String information = obj.getString("information");
								String goal3	 = obj.getString("goal");
								String name3	 	 = obj.getString("name");
								map.put("id", id3);
								map.put("owner", owner3);
								map.put("driver", driver3);
								map.put("lng", lng3);
								map.put("lat", lat3);
								map.put("deadline", deadline3);
								map.put("information", information);
								map.put("goal", goal3);
								map.put("name", name3);
								break;
							default:
								break;
							}
							//	放入到数据的容器中;
							listnode.add(map);
							i++;
						} catch (Exception e) {
							obj		= null;
							break;
						}
					} while (obj!=null);
				}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
			}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			bundle.putSerializable("listnode", listnode);
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
	//	加载数据列表;
	private void loadData(ArrayList<Map<String, String>> list,int btnposition){
		switch (btnposition) {
		//	回收单列表显示
		case 0:			
			adapter0=new MTListCollectionAdapter(mContext, list, -1, listview);
			listview.setAdapter(adapter0);
			break;
		//	回收员列表显示
		case 1:
			adapter1=new MTListDriverAdapter(mContext, list, -1, listview);
			listview.setAdapter(adapter1);
			break;
		//	新电池列表显示
		case 2:
			adapter2=new MTListDeliveryAdapter(mContext, list, -1, listview);
			listview.setAdapter(adapter2);
			break;
		default:
			break;
		}
		//	进行数据的加载;
	}
	//	关闭线程内容;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//	回收单的内容;
	public class MTListCollectionAdapter extends BaseAdapter{
		private Context				context;
		private ArrayList<Map<String, String>> 	list;
		private int 				size;
		private int 				index;
		private ListView			listview;

		public MTListCollectionAdapter(Context context,ArrayList<Map<String, String>> list,int index,ListView listview) {
			this.context	= context;
			this.list		= list;
			this.size		= this.list.size();
			this.index		= index;
			this.listview	= listview;
		}
		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			//	map映射的内容;
			Map<String, String> map=list.get(position);
			//	控件的内容;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView  = LayoutInflater.from(context).inflate(R.layout.act_item_node_n, null);
				//	滑动块;
				viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
				//	文本内容;
				viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.tvnumber);
				viewHolder.tvid 	= (TextView) convertView.findViewById(R.id.tvid);
				viewHolder.tvname 	= (TextView) convertView.findViewById(R.id.tvname);
				viewHolder.ivstate 	= (TextView) convertView.findViewById(R.id.ivstate);
				viewHolder.tvstate 	= (TextView) convertView.findViewById(R.id.tvstate);
				viewHolder.tvtel 	= (TextView) convertView.findViewById(R.id.tvtel);
				viewHolder.tvtel.setVisibility(View.GONE);
				viewHolder.tvnumber.setVisibility(View.VISIBLE);
				
				//	删除按钮;
				viewHolder.txt_delete = (TextView) convertView.findViewById(R.id.text_delete);
				viewHolder.linear = (LinearLayout) convertView.findViewById(R.id.linear);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			//	具体信息;
			final String id		 =	map.get("id");
			final String deadline=	map.get("deadline");
			final String price	 =	map.get("price");
			final String goal 	 =	map.get("goal");
			final String model	 =	map.get("model");
			final String count	 =	map.get("count");
			/* 进行相应内容*/
			String status	=	map.get("status");
			String name 	=	map.get("name");
			
			//	进行数据的标签;
			int    order    = position+1;
		
			int    state 	= Integer.parseInt(status);
			if(state==1){
				viewHolder.ivstate.setBackgroundResource(R.drawable.icon_inv_ok);
				viewHolder.tvstate.setText("审批通过");
				viewHolder.tvstate.setTextColor(Color.GREEN);
			}else if(state==0){
				viewHolder.ivstate.setBackgroundResource(R.drawable.icon_inv_mis);
				viewHolder.tvstate.setText("审批未过");
				viewHolder.tvstate.setTextColor(Color.RED);
			}
			viewHolder.tvnumber.setText(String.valueOf(order));
			//设置内容
			viewHolder.tvid.setText(id);
			viewHolder.tvname.setText(name);	

			//	进行标签的颜色交换;
			if(position%2==0){
				viewHolder.tvnumber.setBackgroundResource(R.drawable.shape_order_n);
			}else {
				viewHolder.tvnumber.setBackgroundResource(R.drawable.shape_order1_n);
			}
			
			//	滑块布局的内容;
			viewHolder.linear.setVisibility(View.GONE);
			
			 //删除按钮
	        viewHolder.txt_delete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	btnposition=0;
	            	param	   ="opertype=4&id="+id+"&owner="+owner;
	            	queryDataFromServer(param, btnposition, null);
	            }
	        });
			//设置自定义监听
			viewHolder.swipeLayout.setOnSlide(new SwipeLayout.onSlideListener() {
				//侧滑完了之后调用 true已经侧滑，false还未侧滑
				@Override
				public void onSlided(boolean isSlide) {
					if (isSlide) {//是否滑动成功（包括侧滑之后的返回滑动）
	                    if (index != -1) {
	                    	//当第一个已经侧滑了，在侧滑第二个的时候，就把第一个还原
	                        if (listview.getChildAt(index - listview.getFirstVisiblePosition()) != null) {
	                            SwipeLayout swipeLayout = (SwipeLayout) listview.getChildAt(index - listview.getFirstVisiblePosition()).findViewById(R.id.swipelayout);
	                            swipeLayout.revert();
	                        }
	                    }
	                    index = position;
	                }
				}
				//未侧滑状态下的默认显示整体的点击事件
				@Override
				public void onClick() {
					Intent intent=new Intent(context, VDiagActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("id",id);
					//	
					String string		 =
							"电池名称    "+model+"\r\n\r\n" +
							"重量    	 "+count+"斤\r\n\r\n" +
							"地址           "+goal+"\r\n\r\n" +
							"日期           "+deadline+"\r\n\r\n" +
							"价格           "+price+"元"
							;

					bundle.putString("content",string);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
			
			return convertView;
		}
		//	对象类的内容;
		private class ViewHolder {
			/** 滑动父控件 */
			private SwipeLayout swipeLayout;
			/** 序号文本 */
			private TextView tvnumber;
			private TextView tvid;
			private TextView tvname;
			private TextView tvtel;
			/**	状态标记**/
			private TextView ivstate;
			private TextView tvstate;
			/** 删除按钮 */
			private TextView txt_delete;
			/** 右边试图*/
			private LinearLayout linear;
		}
	}
	//	电池单;
	public class MTListDeliveryAdapter extends BaseAdapter{
		private Context				context;
		private ArrayList<Map<String, String>> 	list;
		private int 				size;
		private int 				index;
		private ListView			listview;

		public MTListDeliveryAdapter(Context context,ArrayList<Map<String, String>> list,int index,ListView listview) {
			this.context	= context;
			this.list		= list;
			this.size		= this.list.size();
			this.index		= index;
			this.listview	= listview;
		}
		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			Map<String, String> map = list.get(position);
			if (convertView == null) {
				viewHolder 			= new ViewHolder();
				convertView  		= LayoutInflater.from(context).inflate(R.layout.act_item_node_n, null);
				//	滑动块;
				viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
				//	文本内容;
				viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.tvnumber);
				viewHolder.tvid 	= (TextView) convertView.findViewById(R.id.tvid);
				viewHolder.tvname 	= (TextView) convertView.findViewById(R.id.tvname);
				viewHolder.ivstate 	= (TextView) convertView.findViewById(R.id.ivstate);
				viewHolder.tvstate 	= (TextView) convertView.findViewById(R.id.tvstate);
				//	删除按钮;
				viewHolder.txt_delete = (TextView) convertView.findViewById(R.id.text_delete);
				viewHolder.linear = (LinearLayout) convertView.findViewById(R.id.linear);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			final String id		= map.get("id");
			final String deadline	= map.get("deadline");
			final String information= map.get("information");
			final String goal		= map.get("goal");
			final String name		= map.get("name");
			int    order	= position+1;
			//	显示;
			viewHolder.ivstate.setBackgroundResource(R.drawable.icon_inv_ok);
			viewHolder.tvstate.setText("审批通过");
			viewHolder.tvstate.setTextColor(Color.GREEN);
			//	
			viewHolder.tvnumber.setText(String.valueOf(order));
			//设置内容
			viewHolder.tvid.setText(id);
			viewHolder.tvname.setText(name);

			//	进行标签的颜色交换;
			if(position%2==0)
				viewHolder.tvnumber.setBackgroundResource(R.drawable.shape_order_n);
			else viewHolder.tvnumber.setBackgroundResource(R.drawable.shape_order1_n);
			
			//	滑块布局的内容;
			viewHolder.linear.setVisibility(View.GONE);
			
			
			 //删除按钮
	        viewHolder.txt_delete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	btnposition=2;
	            	param="opertype=4&id="+id+"&owner="+owner;
	    			queryDataFromServer(param, btnposition,null);
	            }
	        });
			//设置自定义监听
			viewHolder.swipeLayout.setOnSlide(new SwipeLayout.onSlideListener() {
				//侧滑完了之后调用 true已经侧滑，false还未侧滑
				@Override
				public void onSlided(boolean isSlide) {
					if (isSlide) {//是否滑动成功（包括侧滑之后的返回滑动）
	                    if (index != -1) {
	                    	//当第一个已经侧滑了，在侧滑第二个的时候，就把第一个还原
	                        if (listview.getChildAt(index - listview.getFirstVisiblePosition()) != null) {
	                            SwipeLayout swipeLayout = (SwipeLayout) listview.getChildAt(index - listview.getFirstVisiblePosition()).findViewById(R.id.swipelayout);
	                            swipeLayout.revert();
	                        }
	                    }
	                    index = position;
	                }
				}
				//未侧滑状态下的默认显示整体的点击事件
				@Override
				public void onClick() {
					Intent intent=new Intent(context, VDiagActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("id",id);
					
					String string		 =
					"回收员        "+name+"\r\n\r\n" +
					"电池信息    \r\n"+information+"\r\n\r\n" +
					"地址           "+goal+"\r\n\r\n" +
					"日期           "+deadline;
					
					bundle.putString("content",string);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
			
			return convertView;
		}
		//	对象类的内容;
		private class ViewHolder {
			/** 滑动父控件 */
			private SwipeLayout swipeLayout;
			/** 序号文本 */
			private TextView tvnumber;
			private TextView tvid;
			private TextView tvname;
			/**	状态标记**/
			private TextView ivstate;
			private TextView tvstate;
			/** 删除按钮 */
			private TextView txt_delete;
			/** 右边试图*/
			private LinearLayout linear;
		}
	}
	//	对象单;
	public class MTListDriverAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<Map<String, String>> list;
		private int size;
		private int index;
		private ListView listview;

		public MTListDriverAdapter(Context context,
				ArrayList<Map<String, String>> list, int index, ListView listview) {
			this.context = context;
			this.list = list;
			this.size = this.list.size();
			this.index = index;
			this.listview = listview;
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			Map<String, String> 	map	= list.get(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView  = LayoutInflater.from(context).inflate(R.layout.act_item_node_n, null);
				//	滑动块;
				viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
				//	文本内容;
				viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.tvnumber);
				viewHolder.tvid 	= (TextView) convertView.findViewById(R.id.tvid);
				viewHolder.tvname 	= (TextView) convertView.findViewById(R.id.tvname);
				viewHolder.tvtel 	= (TextView) convertView.findViewById(R.id.tvtel);
				//	删除按钮;
				viewHolder.txt_delete = (TextView) convertView.findViewById(R.id.text_delete);
				viewHolder.linear 	= (LinearLayout) convertView.findViewById(R.id.linear);
				viewHolder.laystate = (RelativeLayout) convertView.findViewById(R.id.laystate);
				viewHolder.tvnumber.setVisibility(View.GONE);
				viewHolder.laystate.setVisibility(View.GONE);
				viewHolder.tvtel.setVisibility(View.VISIBLE);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			final String id  		 = map.get("id");
			String name		 = map.get("name");
			String car_number= map.get("car_number");

			viewHolder.tvtel.setText(id);
			viewHolder.tvid.setText(name);
			viewHolder.tvname.setText(car_number);

			//	滑块布局的内容;
			viewHolder.linear.setVisibility(View.GONE);
			
			 //删除按钮
	        viewHolder.txt_delete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	btnposition=1;
	            	param	   ="opertype=8&driver_id="+id+"&id="+owner;
	            	queryDataFromServer(param, btnposition, sqlDB);
	            }
	        });
			//设置自定义监听
			viewHolder.swipeLayout.setOnSlide(new SwipeLayout.onSlideListener() {
				//侧滑完了之后调用 true已经侧滑，false还未侧滑
				@Override
				public void onSlided(boolean isSlide) {
					if (isSlide) {//是否滑动成功（包括侧滑之后的返回滑动）
	                    if (index != -1) {
	                    	//当第一个已经侧滑了，在侧滑第二个的时候，就把第一个还原
	                        if (listview.getChildAt(index - listview.getFirstVisiblePosition()) != null) {
	                            SwipeLayout swipeLayout = (SwipeLayout) listview.getChildAt(index - listview.getFirstVisiblePosition()).findViewById(R.id.swipelayout);
	                            swipeLayout.revert();
	                        }
	                    }
	                    index = position;
	                }
				}
				//未侧滑状态下的默认显示整体的点击事件
				@Override
				public void onClick() {
					
				}
			});
			
			return convertView;
		}

		// 对象类的内容;
		private class ViewHolder {
			/** 滑动父控件 */
			private SwipeLayout swipeLayout;
			/** 序号文本 */
			private TextView tvnumber;
			private TextView tvtel;
			private TextView tvid;
			private TextView tvname;

			/** 删除按钮 */
			private TextView txt_delete;
			/** 右边试图 */
			private LinearLayout linear;
			private RelativeLayout laystate;
		}
	}
}

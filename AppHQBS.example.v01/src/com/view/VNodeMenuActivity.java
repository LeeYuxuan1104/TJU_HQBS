package com.view;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTSQLiteHelper;
import com.model.tool.view.MTEditTextWithDel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VNodeMenuActivity extends Activity implements OnClickListener{
	//	上下文的内容;
	private Context			mContext;	
	private TextView 		vTopic,vFunction;
	private ImageView 		vSearch;
	private MTEditTextWithDel etSearch;
	private ImageView 		vLeft,vMiddle,vRight;
	private RelativeLayout 	lLeft,lMiddle,lRight;
	private ListView  		vListData;
	//	变量参数;
	private String [] t_top={"回收单","回收员","新电池"};
	private int [] 	  t_sel={R.drawable.tab_bar_icon_list_sel,R.drawable.tab_bar_icon_per_sel,R.drawable.tab_bar_icon_bat_sel};
	private int [] 	  t_nar={R.drawable.tab_bar_icon_list_nar,R.drawable.tab_bar_icon_per_nar,R.drawable.tab_bar_icon_bat_nar};
	//	下角标;
	private int btnposition=0;
	//	自定的参数;
	private MyThread			mThread=null;// 自定义的上传线程;
	private MTConfigure			mtConfigure;
	private ProgressDialog  	vDialog;	 // 对话框;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private Bundle				mbundle;
	private String				ownner;
	private Intent				mIntent;
	private ArrayAdapter<String>mAdapter;
	//	数据库参照;
	private MTSQLiteHelper 	  mSqLiteHelper;// 数据库的帮助类;
	private SQLiteDatabase 	  mDB; // 数据库件;
	
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
		setContentView(R.layout.act_node_menu);
		initView();
		initEvent();
		queryDataFromServer(ownner, btnposition, "queryall",null);
	}
	
	//	重新进行加载;
	@Override
	protected void onRestart() {
		super.onRestart();
		//	重新进行数据加载;
		switch (btnposition) {
		//	查询所有的回收单信息;
		case 0:
			
			break;
		//	查询所有数据信息;
		case 1:
			queryDataFromServer(ownner, btnposition, "queryall",mDB);
			break;
		//	查询所有电池信息;
		case 2:
			queryDataFromServer(ownner, btnposition, "queryall",null);
			break;
		default:
			break;
		}
	}
	//	点击按钮的事件内容;
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.layleft:
			etSearch.setText("");
			btnposition=0;
			queryDataFromServer(ownner, btnposition, "queryall",null);
			break;
		case R.id.laymiddle:
			etSearch.setText("");
			btnposition=1;	
			queryDataFromServer(ownner, btnposition, "queryall",mDB);
			break;
		case R.id.layright:
			etSearch.setText("");
			btnposition=2;
			queryDataFromServer(ownner, btnposition, "queryall",null);
			break;
		case R.id.isearch:
			String id=mtConfigure.getEditText(etSearch);
			if(id!=null){				
				switch (btnposition) {
				case 0:
					queryDataFromServer(id, btnposition, "queryitem",null);	
					break;
				case 1:
					queryDataFromServer("driver="+id+"&ownner="+ownner, btnposition, "queryitem",null);
					break;
				case 2:
					queryDataFromServer("model="+id, btnposition, "queryitem",null);
					break;
				default:
					break;
				}
			}else Toast.makeText(mContext, "不可为空", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.btnFunction:
			switch (btnposition) {
			case 0:
				mIntent=new Intent(mContext, VDeliveryAddActivity.class);
				mbundle=new Bundle();
				mbundle.putString("ownner", ownner);
				mIntent.putExtras(mbundle);
				break;
			//	新增信息的加载;
			case 1:
				mIntent=new Intent(mContext, VDriverAddActivity.class);
				mbundle=new Bundle();
				mbundle.putString("ownner", ownner);
				mIntent.putExtras(mbundle);
				break;
			//	新增信息的加载;
			case 2:
				mIntent=new Intent(mContext, VGoodAddActivity.class);
				break;

			default:
				break;
			}
			startActivity(mIntent);
			break;
		default:
			break;
		}
		initChooseState(btnposition);
	}
	//	初始化控件;
	private void initView(){
		//	标题栏;
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		etSearch=(MTEditTextWithDel) findViewById(R.id.etsearch);
		vSearch	=(ImageView) findViewById(R.id.isearch);
		vLeft	=(ImageView) findViewById(R.id.btnleft);
		vMiddle	=(ImageView) findViewById(R.id.btnmiddle);
		vRight	=(ImageView) findViewById(R.id.btnright);
		lLeft	=(RelativeLayout) findViewById(R.id.layleft);
		lMiddle	=(RelativeLayout) findViewById(R.id.laymiddle);
		lRight	=(RelativeLayout) findViewById(R.id.layright);
		vFunction=(TextView) findViewById(R.id.btnFunction);
		vListData=(ListView) findViewById(R.id.listShow);
		
	}
	//	初始化事件;
	private void initEvent(){
		if(mContext==null){			
			mContext	 = VNodeMenuActivity.this;
		}
		mtGetOrPostHelper= new MTGetOrPostHelper();
		mtConfigure		 = new MTConfigure();
		//	数据库信息的初始化;
		mSqLiteHelper 	 = new MTSQLiteHelper(mContext);
		//	数据库的获得构造函数;
		mDB 		  	 = mSqLiteHelper.getmDB();
		//	网络进行加载;
		vTopic.setText(t_top[0]);
		vLeft.setBackgroundResource(t_sel[0]);
		vMiddle.setBackgroundResource(t_nar[1]);
		vRight.setBackgroundResource(t_nar[2]);
		lLeft.setOnClickListener(this);
		lMiddle.setOnClickListener(this);
		lRight.setOnClickListener(this);
		vFunction.setText("新增");
		vFunction.setVisibility(View.VISIBLE);
		//	数值的初始化;
		mIntent =getIntent();
		mbundle =mIntent.getExtras();
		ownner	=mbundle.getString("id");
		btnposition=0;
		//	添加事件监听;
		vSearch.setOnClickListener(this);
		vFunction.setOnClickListener(this);
	}
	//	进行图像的初始化;
	private void initChooseState(int position){
		switch (position) {
		case 0:
			vLeft.setBackgroundResource(t_sel[0]);
			vMiddle.setBackgroundResource(t_nar[1]);
			vRight.setBackgroundResource(t_nar[2]);
			break;
		case 1:
			vLeft.setBackgroundResource(t_nar[0]);
			vMiddle.setBackgroundResource(t_sel[1]);
			vRight.setBackgroundResource(t_nar[2]);
			break;
		case 2:
			vLeft.setBackgroundResource(t_nar[0]);
			vMiddle.setBackgroundResource(t_nar[1]);
			vRight.setBackgroundResource(t_sel[2]);
			break;
		default:
			break;
		}
		vFunction.setText("新增");
		vTopic.setText(t_top[position]);
	}
	//	从服务器端进行数据的查询;
	private void queryDataFromServer(String param,int position,String oper,SQLiteDatabase mDB){
		if(mThread==null){
			final CharSequence strDialogTitle = getString(R.string.wait);
			final CharSequence strDialogBody = getString(R.string.doing);
			vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
			mThread=new MyThread(mtGetOrPostHelper, param,position,oper,mDB);
			mThread.start();
		}
	}
	//	进行数据读取的加载内容;
	private void loadData(ArrayList<String> list1,final ArrayList<Map<String, String>> list2){
		//	上下文信息的加载;
		if(mContext==null){
			mContext=VNodeMenuActivity.this;
		}
		mAdapter=new ArrayAdapter<String>(mContext, R.layout.act_item01,R.id.content, list1);
		vListData.setAdapter(mAdapter);
		
		vListData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg2) {
				//	对话框的生成;
				Builder 			builder	=	new Builder(mContext);
				//	标题内容的设置;
				builder.setTitle("详情");
				//	进行数据容器的设定;
				Map<String, String> map		=	list2.get(position);
				String				content =	null;
				switch (btnposition) {
				//	第1个按钮;
				case 0:
					String id	 =	map.get("id");
					String driver=	map.get("driver");
					String model =	map.get("model");
					String price =	map.get("price");
					String count =	map.get("count");
					String lng	 =	map.get("lng");
					String lat	 =	map.get("lat");
					String deadline=map.get("deadline");
					content=
					"ID编号："+id+"\r\n"+
					"回收员:"+driver+"\r\n"+
					"模式:"+model+"\r\n"+
					"价格:"+price+"元\r\n"+
					"件数:"+count+"件\r\n"+
					"经度:"+lng+"\r\n"+
					"纬度:"+lat+"\r\n"+
					"截止:"+deadline
					;
					break;
				//	第2个按钮;
				case 1:
					String driver2 =	map.get("driver");
					String car	   =	map.get("car");
					String tel 	   =	map.get("tel");
					String person  =	map.get("person");
					content=
					"回收员:"+driver2+"\r\n"+
					"车辆编号:"+car+"\r\n"+
					"电话:"+tel+"\r\n"+
					"人员:"+person
					;
					break;
				//	第3个按钮;
				case 2:
					String model2  =	map.get("model");
					String brand   = 	map.get("brand");
					String information= map.get("information");
					String price2  = 	map.get("price");
					String count2  = 	map.get("count");
					content=
					"模式:"+model2+"\r\n"+		
					"品牌:"+brand+"\r\n"+		
					"单价:"+price2+"\r\n"+		
					"件数:"+count2+"\r\n"+		
					"信息:\r\n"+information		
					;
					break;
				default:
					break;
				}
				//	单条数据信息的查询;
				builder.setMessage(content);
				if(btnposition==0){
					builder.setPositiveButton(R.string.map, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							//	进行地图的定位点;
							mIntent=new Intent(mContext, VGeoFenceActivity.class);
							startActivityForResult(mIntent, 3);	
						}
					});	
				}
				//	取消按钮的设置内容;
				builder.setNeutralButton(R.string.ok, null);
				//	创建对话框内容信息;
				builder.create();
				//	显示对话框内容信息;
				builder.show();
			}
		});
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param2,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		private SQLiteDatabase	  mDB;
		private int position;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,int position,String oper,SQLiteDatabase mDB) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param2			  =param;
			this.oper			  =oper;
			this.position		  =position;
			this.mDB			  =mDB;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  url		 =	null;
			String  param	 =	null;
			String  response = 	null;
			//	SQl语句进行操作的内容;
			String 	sql		 =	null;
			//	全局定位的操作符号;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			ArrayList<String>				list1=new ArrayList<String>();
			ArrayList<Map<String, String>>  list2=new ArrayList<Map<String,String>>();
			int 	nSize	 = 0;
			url				 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/node_info";
			///	网络请求块;
			//	查询类别的判定;
			switch (position) {
			//	回收单;
			case 0:
				//	全信息查询;
				if(oper.equals("queryall")){
					param			 =  "opertype=1&ownner="+param2;
				}else 
				//	单信息查询;
				if(oper.equals("queryitem")){
					param			 =  "opertype=2&driver="+param2;
				}
				//	跳出循环;
				break;
			//	回收员;
			case 1:
				//	全信息查询;
				if(oper.equals("queryall")){
					param			 =  "opertype=3&ownner="+param2;
				}else 
				//	单信息查询;
				if(oper.equals("queryitem")){
					param			 =  "opertype=4&"+param2;
				}			
				//	跳出循环;
				break;
			//	新电池
			case 2:
				//	全信息查询;
				if(oper.equals("queryall")) 	param	=  "opertype=5";
				else if(oper.equals("queryitem")) param	=  "opertype=6&"+param2;
				//	跳出循环;
				break;
			//	默认位置;
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
							do {
								try {
									obj 	= array.getJSONObject(i);
									String id=obj.getString("id");
									obj.getString("ownner");
									String driver=obj.getString("driver");
									String model=obj.getString("model");
									String price=obj.getString("price");
									String count=obj.getString("count");
									String lng=obj.getString("lng");
									String lat=obj.getString("lat");
									String deadline=obj.getString("deadline");
									list1.add(id+"    "+driver+"    "+deadline);
									Map<String , String> map=new HashMap<String, String>();
									map.put("id", id);
									map.put("driver", driver);
									map.put("model", model);
									map.put("price", price);
									map.put("count", count);
									map.put("lng", lng);
									map.put("lat", lat);
									map.put("deadline", deadline);
									list2.add(map);
									i++;
								} catch (JSONException e) {
									obj		= null;
									break;
								}
							} while (obj!= null);
							break;
						//	进行回收员信息的处理;
						case 1:
							sql	=	"delete from driver";
							mDB.execSQL(sql);
							do {
								try {
									obj 		 = array.getJSONObject(i);
									String driver= obj.getString("driver");
									String car	 = obj.getString("car");
									String tel	 = obj.getString("tel");
									String person= obj.getString("person");
									list1.add(driver+"    "+car+"    "+tel+"    "+person);
									Map<String , String> map=new HashMap<String, String>();
									
									map.put("driver", driver);
									map.put("car", car);
									map.put("tel", tel);
									map.put("person", person);
									
									list2.add(map);
									//	进行数据的插入;
									sql			= "insert into driver (id,carnumber,name,state) values ('"+driver+"','"+car+"','"+person+"','1')";
									mDB.execSQL(sql);
									i++;
								} catch (JSONException e) {
									obj=null;
									break;
								}
							} while (obj!= null);
							break;
						//	进行电池信息的处理操作;
						case 2:
							do {
								try {
									obj 		 	 = array.getJSONObject(i);
									String model 	 = obj.getString("model");
									String brand 	 = obj.getString("brand");
									String information	 = obj.getString("information");
									String price	 = obj.getString("price");
									String count	 = obj.getString("count");
									list1.add(model+"    "+brand+"    "+count+"件");
									Map<String , String> map=new HashMap<String, String>();
									
									map.put("model", model);
									map.put("brand", brand);
									map.put("information", information);
									map.put("price", price);
									map.put("count", count);
									
									list2.add(map);
									i++;
								} catch (JSONException e) {
									obj=null;
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
	//	关闭线程内容;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//	按下事件;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder builder=new Builder(mContext);
			builder.setTitle(R.string.exit);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					mIntent=new Intent(mContext, VWelActivity.class);
					startActivity(mIntent);
					finish();
				}
			});
			builder.setNegativeButton(R.string.no, null);
			builder.create();
			builder.show();  
	    }
		return super.onKeyDown(keyCode, event);
	}
}

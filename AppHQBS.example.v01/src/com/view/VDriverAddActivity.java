package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTSQLiteHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VDriverAddActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private TextView vBack,vTopic,vSubmit;
	private ListView vListShow;
	//////////////////////////////////////////
	//**需要改正*//
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框;
	//	进行数据库的操作;
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private MTSQLiteHelper 	    mSqLiteHelper;// 数据库的帮助类;
	private SQLiteDatabase 	    mDB; // 数据库件;
	private Cursor 		   	  	mCursor;	  // 数据库遍历签;
	private ArrayList<Map<String, String>> list;
	private MyAdapter			mAdapter;
	//	回收站点的参数;
	private Intent				mIntent;
	private Bundle				mBundle;
	private String 				node_id;
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
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
				loadData();
				//	对话框显示;
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
			closeThread();
		}
	};
	
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	//	对数据进行加载;
	private void loadData(){
		list		=	new ArrayList<Map<String,String>>();
		String 	sql	=	"select * from driver ";
		mCursor		=	mDB.rawQuery(sql, null);
		while (mCursor.moveToNext()) {	
			
			Map<String, String> map=new HashMap<String, String>();
			String id		=	mCursor.getString(mCursor.getColumnIndex("id")).toString();
			String carnumber=	mCursor.getString(mCursor.getColumnIndex("carnumber")).toString();
			String name		=	mCursor.getString(mCursor.getColumnIndex("name")).toString();
			String state	=	mCursor.getString(mCursor.getColumnIndex("state")).toString();

			map.put("id", id);
			map.put("content", carnumber+"    "+name);
			map.put("state", state);

			list.add(map);
		}
		
		if(mCursor!=null){
			mCursor.close();
		}
		//	适配器参数;
		mAdapter=new MyAdapter(mContext, list);
		//	列表进行显示;
		vListShow.setAdapter(mAdapter);
	}
	//	进行初始化查询;
	private void initLoadDataFromServer(String param,SQLiteDatabase mDB){
		if(mThread==null){
			mThread=new MyThread(mtGetOrPostHelper, param, "init",mDB);
			mThread.start();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_add);
		initView();
		initEvent();
	}
	
	
	private void initView(){
		vBack=(TextView) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vListShow=(ListView) findViewById(R.id.listShow);
		vSubmit=(TextView) findViewById(R.id.btnFunction);
	}
	
	private void initEvent(){
		if(mContext==null){			
			mContext		 =VDriverAddActivity.this;
		}
		//	获得相应的参数;
		mIntent	=	getIntent();
		mBundle	=	mIntent.getExtras();
		node_id	=	mBundle.getString("ownner");
		
		//	网络信息内容;
		mtGetOrPostHelper= new MTGetOrPostHelper();
		//	数据库信息的初始化;
		mSqLiteHelper	 = new MTSQLiteHelper(mContext);
		mDB				 = mSqLiteHelper.getmDB();
		
		vTopic.setText(R.string.action_add);
		vBack.setText(R.string.no);
		vBack.setVisibility(View.VISIBLE);
		vBack.setOnClickListener(this);
		vSubmit.setVisibility(View.VISIBLE);
		vSubmit.setText(R.string.submit);
		initLoadDataFromServer(null,mDB);
		vSubmit.setOnClickListener(this);
		
	}

	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		private SQLiteDatabase	  mDB;
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper,SQLiteDatabase mDB) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.oper			  =oper;
			this.mDB			  =mDB;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  urlhead	 =	null;
			String  urlbody	 =	null;
			String  response = 	null;
			String  sql		 =	null;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			int 	nSize	 = 0;
			if(oper.equals("init")){				

				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/driver_info";
				urlbody			 =  "opertype=1";
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
									String id	 = obj.getString("id");
									String carnumber	 = obj.getString("carnumber");
									String name	 = obj.getString("name");
									//	进行插入,当数据不重复的情况下;
									sql="insert into driver (id,carnumber,name,state) select '"+id+"','"+carnumber+"','"+name+"','0' where not exists (select id from driver where id = '"+id+"')";
									mDB.execSQL(sql);
									//	下角标进行迭加;
									i++;
								} catch (JSONException e) {
									//	当出现异常的情况下,进行跳出;
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}else if(oper.equals("submit")){
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/driver_info";
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
	//	进行自定义的适配器内容;
	/*自定义适配器*/
	class MyAdapter extends BaseAdapter{
		private List<Map<String, String>> list;
		private LayoutInflater inflater;
		private int size;
		
		public MyAdapter(Context context,List<Map<String, String>> list) {
			this.list=list;
			inflater =LayoutInflater.from(context);
			this.size=this.list.size();
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
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=inflater.inflate(R.layout.act_item03,null);
				//将控件保存到viewHolder中
				viewHolder.number=(TextView)convertView.findViewById(R.id.number);
				viewHolder.content=(TextView) convertView.findViewById(R.id.content);
				viewHolder.check=(CheckBox) convertView.findViewById(R.id.check);
				//通过setTag将ViewHoler与convertView绑定
				convertView.setTag(viewHolder);			
			}else{
				viewHolder=(ViewHolder) convertView.getTag();					
			}
			String number	=list.get(position).get("id").toString();
			String content	=list.get(position).get("content").toString();
			final String state	=list.get(position).get("state").toString();

			viewHolder.number.setText(number);
			viewHolder.content.setText(content);
			if(state.equals("1")) viewHolder.check.setChecked(true);	
			else viewHolder.check.setChecked(false); 

			viewHolder.check.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(state.equals("1")) list.get(position).put("state", "0");
					else list.get(position).put("state", "1");
				}
			});
			return convertView;
			
		}
		//	建立标签内容;
		class ViewHolder{
			public TextView	 number;
			public TextView	 content;
			public CheckBox	 check;
		}
	}
	
	//	点击事件的内容;
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	返回键;
		case R.id.btnBack:
			finish();
			break;
		//	提交键;
		case R.id.btnFunction:
			String param="";
			String body="";
			for(Map<String, String> map:list){
				String id=map.get("id");
				String state=map.get("state");
				if(state.equals("1")){
					body+=id+"_";
				}
			}
			param="node_id="+node_id+"&driver_id="+body;

			if(mThread==null){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				mThread = new MyThread(mtGetOrPostHelper, param,"submit",null);
				mThread.start();
			}
			break;
		//	默认键;
		default:
			break;
		}
	}
}

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
import com.model.tool.view.MTEditTextWithDel;
import com.model.tool.view.MTMenuBar2;
import com.model.tool.view.SwipeLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class VDriverMenuActivity extends Activity implements OnClickListener,OnTouchListener{
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
	private TextView	   ivleft,ivmiddle,ivback,tvleft,tvmiddle;
	//	下角标;
	private int 	  	   btnposition=0;
	/*自定义的对象*/
	private MTMenuBar2	   mtMenuBar;
	private MTIDHelper	   idHelper;
	private MTConfigure	   mtConfigure;
	//	站点id编号,角色类别;
	private String 		   owner,role,param;
	/*线程有关的参数*/
	private ProgressDialog vDialog;	 // 对话框;
	private MyThread	   mThread=null;// 自定义的上传线程;
	private MTGetOrPostHelper	mtGetOrPostHelper;

	/*前端显示控件*/
	/**列表控件*/
	private ListView 	   listview;
	/**当前滑动的下标*/
	private MTListCollectionAdapter	adapter0;
	
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
		//	回收单全信息查询;
		param="opertype=1&owner="+owner+"&status=1";
		queryDataFromServer(param);
	}

	private void initView(){
		//	返回键的按钮;
		btnback=(RelativeLayout) findViewById(R.id.btnBack);
		ivback =(TextView) findViewById(R.id.ivBack);
		//	功能键;
		btnadd =(RelativeLayout) findViewById(R.id.btnFunction);
		//	标题按钮;
		tvtopic=(TextView) findViewById(R.id.tvTopic);
		//	下方的三个按钮;
		//	左键按钮;
		ivleft	=(TextView) findViewById(R.id.ivleft);
		tvleft	=(TextView) findViewById(R.id.tvleft);
		//	中间按钮;
		ivmiddle=(TextView) findViewById(R.id.ivmiddle);
		tvmiddle	=(TextView) findViewById(R.id.tvmiddle);
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
	@Override
	protected void onRestart() {
		super.onRestart();
		switch (btnposition) {
		case 0:
			//	回收单全信息查询;
			param="opertype=1&owner="+owner+"&status=1";
			queryDataFromServer(param);
			break;
		case 1:
			//	回收单全信息查询;
			param="opertype=1&status=2&owner="+owner;
			queryDataFromServer(param);
			break;

		default:
			break;
		}
	}
	private void initEvent(){
		if(mContext==null){
			mContext=VDriverMenuActivity.this;
		}
		//	自定义部分的内容;
		mtGetOrPostHelper=new MTGetOrPostHelper();
		mtConfigure		 =new MTConfigure();
		
		//	初始值的内容;
		btnposition=0;
		//	身份校验
		idHelper	= new MTIDHelper(mContext, "userinfo", Context.MODE_APPEND);
		btnadd.setVisibility(View.GONE);
		btnright.setVisibility(View.GONE);
		tvleft.setText("任务单");
		tvmiddle.setText("完成单");
		//	返回键按钮;
		ivback.setBackgroundResource(R.drawable.icon_add_l);
		//	返回键的事件;
		btnback.setOnTouchListener(this);
		//	下方列单的内容;
		mtMenuBar = new MTMenuBar2(ivleft,ivmiddle,etsearch);
		mtMenuBar.setBottomlayout(btnposition, tvtopic);
		mtMenuBar.setSearchTip(btnposition);
		//	增加事件监听;
		btnleft.setOnClickListener(this);
		btnmiddle.setOnClickListener(this);
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
			mtMenuBar.setSearchTip(btnposition);
			param="opertype=1&status=1&owner="+owner;
			queryDataFromServer(param);
			break;
		case R.id.btnmiddle:
			btnposition=1;
			//	查询输入框清空;
			etsearch.setText("");
			mtMenuBar.setBottomlayout(btnposition, tvtopic);
			mtMenuBar.setSearchTip(btnposition);
			param="opertype=1&status=2&owner="+owner;
			queryDataFromServer(param);
			break;
		case R.id.btnsearch:
			String id=mtConfigure.getEditText(etsearch);
			if(id!=null){				
				switch (btnposition) {
				case 0:
					param="opertype=2&owner="+owner+"&id="+id+"&status=1";	
					break;
				case 1:
					param="opertype=2&owner="+owner+"&id="+id+"&status=2";
					break;
				default:
					break;
				}
				queryDataFromServer(param);
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
		default:
			break;
		}
		
		return false;
	}

	//	从服务器端进行数据的查询;
	private void queryDataFromServer(String param){
		if(mThread==null){
			final CharSequence strDialogTitle = getString(R.string.wait);
			final CharSequence strDialogBody = getString(R.string.doing);
			vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
			mThread=new MyThread(mtGetOrPostHelper, param);
			mThread.start();
		}
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param;
		private MTGetOrPostHelper mtGetOrPostHelper;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  url	 	 =	null;
			String  response = 	null;
			//	全局定位的操作符号;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array =  null;
			JSONObject obj 	 =  null;
			ArrayList<Map<String, String>>  listnode=new ArrayList<Map<String,String>>();
			url	= "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
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

							//	获取相应的数据;
							String id	 = obj.getString("id");
							String driver= obj.getString("driver");
							String deadline=obj.getString("deadline");
							String goal	 = obj.getString("goal");
							String status = obj.getString("status");
							String name	 = obj.getString("name");
							//	添加至map;
							map.put("id", id);
							map.put("driver", driver);
							map.put("deadline", deadline);
							map.put("status", status);
							map.put("goal", goal);
							map.put("name", name);
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
		adapter0=new MTListCollectionAdapter(mContext, list, -1, listview);
		listview.setAdapter(adapter0);
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
			final String id	= map.get("id");
//			String driver	= map.get("driver");
			final String deadline = map.get("deadline");
			String status	= map.get("status");
			final String goal		= map.get("goal");
			String name		= map.get("name");
			
			//	进行数据的标签;
			int    order    = position+1;
		
			final int    state 	= Integer.parseInt(status);
			if(state==2){
				viewHolder.ivstate.setBackgroundResource(R.drawable.icon_inv_ok);
				viewHolder.tvstate.setText("已完成");
				viewHolder.tvstate.setTextColor(Color.GREEN);
				viewHolder.txt_delete.setText("删除");
			}else if(state==1){
				viewHolder.ivstate.setBackgroundResource(R.drawable.icon_inv_mis);
				viewHolder.tvstate.setText("未完成");
				viewHolder.tvstate.setTextColor(Color.RED);
				viewHolder.txt_delete.setText("已读");
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
	            	//	变成已读;
	            	if(state==1){
		            	param	   ="opertype=3&id="+id+"&owner="+owner+"&status0=2&status=1";
		            	queryDataFromServer(param);	
	            	}else 
	            	if(state==2){
	            		param	   ="opertype=4&id="+id+"&owner="+owner+"&status=2";
		            	queryDataFromServer(param);
	            	}
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
					//	任务单跳转;
					if(state==1){						
						Intent	intent=new Intent(context, VSignActivity.class);
						Bundle  bundle=new Bundle();
						bundle.putString("id", id);
						bundle.putString("deadline", deadline);
						bundle.putString("goal", goal);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					//	完成单跳转;
					else if(state==2){
						Intent	intent=new Intent(context, VHistoryActivity.class);
						Bundle  bundle=new Bundle();
						bundle.putString("id", id);
						bundle.putString("deadline", deadline);
						bundle.putString("goal", goal);
						bundle.putString("state", "2");
						intent.putExtras(bundle);
						startActivity(intent);
					}
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
}

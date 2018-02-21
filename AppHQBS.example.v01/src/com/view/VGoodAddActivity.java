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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VGoodAddActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private TextView vBack,vTopic,vSubmit;
	private ListView vListShow;
	//////////////////////////////////////////
	//**需要改正*//
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框;
	private MyAdapter	myAdapter;
	private ArrayList<Map<String, String>> lists;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			lists		 =(ArrayList<Map<String, String>>) bundle.getSerializable("lists");
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
				if(lists!=null){
					loadData(lists);
				}
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
	private void loadData(final ArrayList<Map<String, String>> list){
		myAdapter=new MyAdapter(mContext, list);
		vListShow.setAdapter(myAdapter);
	}
	//	进行初始化查询;
	private void initLoadDataFromServer(String param){
		if(mThread==null){
			mThread=new MyThread(mtGetOrPostHelper, param, "init");
			mThread.start();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_good_add);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		//	返回键获取按钮;
		vBack	=(TextView) findViewById(R.id.btnBack);
		//	标题栏内容信息;
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		//	提交键按钮信息;
		vSubmit	=(TextView) findViewById(R.id.btnFunction);
		//	列表控件的信息;
		vListShow=(ListView) findViewById(R.id.listShow);
	}
	//	进行事件的初始化;
	private void initEvent(){
		if(mContext==null){			
			mContext	=	VGoodAddActivity.this;
		}
		mtGetOrPostHelper=new MTGetOrPostHelper();
		vTopic.setText(R.string.action_add);
		vBack.setVisibility(View.VISIBLE);
		vSubmit.setVisibility(View.VISIBLE);
		vBack.setText(R.string.no);
		vSubmit.setText(R.string.submit);
		vBack.setOnClickListener(this);
		vSubmit.setOnClickListener(this);
		initLoadDataFromServer(null);
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String param,oper;
		private MTGetOrPostHelper mtGetOrPostHelper;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.oper			  =oper;
		}
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  urlhead	 =	null;
			String  urlbody	 =	null;
			String  response = 	null;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			int 	nSize	 = 0;
			if(oper.equals("init")){				
				ArrayList<Map<String, String>>	lists=new ArrayList<Map<String,String>>();
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/good_info";
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
									String model	 = obj.getString("model");
									String brand	 = obj.getString("brand");
									String price	 = obj.getString("price");
									String count	 = obj.getString("count");

									Map<String, String> map=new HashMap<String, String>();
									map.put("id", model);
									map.put("content", brand+"    "+price+"元/件"+"  有"+count+"件");
									map.put("count",count);
									map.put("number","0");
									lists.add(map);
									i++;
								} catch (JSONException e) {
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
				if(lists!=null){					
					bundle.putSerializable("lists", lists);
				}
			}else if(oper.equals("submit")){
				urlhead			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/good_info";
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
	//	适配器的显示内容;
	/*自定义适配器*/
	class MyAdapter extends BaseAdapter{
		private List<Map<String, String>> list;
		private LayoutInflater inflater;
		private int 	size;
		private int 	nNumber=0;
		private String 	sNumber;
		
		public MyAdapter(Context context,List<Map<String, String>> list) {
			this.list=list;
			inflater=LayoutInflater.from(context);
			this.size=this.list.size();
		}
		@Override
		public int getCount() {
			//	长度信息;
			return size;
		}

		@Override
		public Object getItem(int position) {
			// 列表信息;
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			//	单个条目的下角标;
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=inflater.inflate(R.layout.act_item04,null);
				//将控件保存到viewHolder中
				viewHolder.vUp=(Button)convertView.findViewById(R.id.up);
				viewHolder.vDown=(Button) convertView.findViewById(R.id.down);
				viewHolder.vCount=(EditText) convertView.findViewById(R.id.tvcount);
				viewHolder.vNumber= (TextView) convertView.findViewById(R.id.number);
				viewHolder.vContent= (TextView) convertView.findViewById(R.id.content);
				//通过setTag将ViewHoler与convertView绑定
				convertView.setTag(viewHolder);			
			}else{
				viewHolder=(ViewHolder) convertView.getTag();					
			}
			String id		=list.get(position).get("id").toString();
			String content	=list.get(position).get("content").toString();
			String number	=list.get(position).get("number").toString();

			viewHolder.vNumber.setText(id);
			viewHolder.vContent.setText(content);
			viewHolder.vCount.setText(number);

			//	升按钮;
			viewHolder.vUp.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					sNumber=viewHolder.vCount.getText().toString();
					try {						
						nNumber=Integer.parseInt(sNumber);
					} catch (Exception e) {
						nNumber=-1;
					}
					if(nNumber!=-1){
						nNumber++;
						list.get(position).put("number", nNumber+"");
						notifyDataSetChanged();
					}else Toast.makeText(mContext, "数值有误", Toast.LENGTH_SHORT).show();	
				}
			});
			//	降按钮;
			viewHolder.vDown.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					sNumber=viewHolder.vCount.getText().toString();
					try {						
						nNumber=Integer.parseInt(sNumber);
					} catch (Exception e) {
						nNumber=-1;
					}
					if(nNumber!=-1){
						if(nNumber!=0){
							nNumber--;
							list.get(position).put("number", nNumber+"");
							notifyDataSetChanged();
						}else Toast.makeText(mContext, "数值已最小", Toast.LENGTH_SHORT).show(); 
					}else Toast.makeText(mContext, "数值有误", Toast.LENGTH_SHORT).show();
				}
			});
			return convertView;
			
		}
		//	建立标签内容;
		class ViewHolder{
			public Button	 vUp,vDown;
			public EditText	 vCount;
			public TextView	 vNumber,vContent;
		}
	}
	
	//	点击事件的按钮触发;
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
			if(mThread==null){
				if(lists!=null){
					String param="param=";
					for(Map<String, String> map:lists){
						//	id编号;
						String id	  =	map.get("id");
						//	件数;
						String scount =	map.get("count");
						//	购买个数;
						String snumber=	map.get("number");
						int    ncount = Integer.parseInt(scount);
						int    nnumber= Integer.parseInt(snumber);
						int    ntmp	  = ncount+nnumber;
						param+=id+"_"+ntmp+";";
					}
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					mThread	= new MyThread(mtGetOrPostHelper, param,"submit");
					mThread.start();
				}
			}
			break;
		default:
			break;
		}
	}
}

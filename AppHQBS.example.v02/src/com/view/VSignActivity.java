package com.view;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hqbs.app.R;
import com.model.entity.BtnView;
import com.model.entity.CollectionAndDelivery;
import com.model.entity.Photo;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTDialogListener;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTPictureHelper;
import com.model.tool.system.MTSQLiteHelper;
import com.model.tool.system.MTWritepadHelper;
import com.model.tool.view.MTWritePadDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class VSignActivity extends Activity implements OnClickListener,OnTouchListener{
	private Context	mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	private RelativeLayout	btnBack,btndetail;
	private TextView		ivBack,tvTopic;
	private TextView		etorder,etdeadline,etgoal;
	private TextView		btngood,btnbad;
	private Button			btnsubmit;
	private Builder			vBuilder;
	/*线程的具体内容*/
	private MTGetOrPostHelper	mtGetOrPostHelper;
	private MTConfigure			mtConfigure;
	private MyThread			mThread=null;// 自定义的上传线程;
	private ProgressDialog  	vDialog;	 // 对话框的相应内容;
	private String			id,param="",status="2";
	private BtnView			btnView;
	/*下方的控件*/
	//	底端的自动提示框;
	private PopupWindow 	popupWindow;
	//	底端的样式对象;
	private View 			bottomView;
	//	动态的动画对象;
	private TranslateAnimation transAnimation;
	//	按钮;
	private RelativeLayout	btnphoto,btnsign;
	private String 			folderpath,filepath,filename,temppath;
	private Photo   	 	photo;
	private CollectionAndDelivery collectionAndDelivery;
	/*数据库的内容*/
	private MTSQLiteHelper 	    mSqLiteHelper;// 数据库的帮助类;
	private SQLiteDatabase		sqlDB;
	
	/*句柄内容*/
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			String oper	 = bundle.getString("oper");
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
				if(oper.equals("init")){
					Map<String, String> map=(Map<String, String>) bundle.getSerializable("item");
					jumpActivity(map);
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
			//	关闭线程;
			closeThread();
		}
	};
	//	线程关闭;
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sign_n);
		initView();
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		btnBack	 =(RelativeLayout) findViewById(R.id.btnBack);
		ivBack 	 =(TextView) findViewById(R.id.ivBack);
		btnsubmit=(Button) findViewById(R.id.btnsubmit);
		tvTopic	 =(TextView) findViewById(R.id.tvTopic);
		etorder	 =(TextView) findViewById(R.id.etorder);
		etdeadline=(TextView) findViewById(R.id.etdeadline);
		etgoal	 =(TextView) findViewById(R.id.etgoal);
		btndetail=(RelativeLayout) findViewById(R.id.btndetail);
		//	正常按钮;
		btngood	 =(TextView) findViewById(R.id.btngood);
		btnbad	 =(TextView) findViewById(R.id.btnbad);
		//	照相按钮;
		btnphoto =(RelativeLayout) findViewById(R.id.btnphoto);
		//	签字按钮;
		btnsign	 =(RelativeLayout) findViewById(R.id.btnsign);
	}
	//	初始化事件;
	private void initEvent(){
		if(mContext==null){
			mContext=VSignActivity.this;
		}
		//
		btnView =	new BtnView(btngood, btnbad);
		mIntent	=	getIntent();
		mBundle =	mIntent.getExtras();
		id		= 	mBundle.getString("id");
		String deadline = mBundle.getString("deadline");
		String goal		= mBundle.getString("goal");
		mtGetOrPostHelper = new MTGetOrPostHelper();
		//	配置文件的声明;
		mtConfigure		= new MTConfigure();
		//	数据库的声明;
		mSqLiteHelper	 = new MTSQLiteHelper(mContext);
		sqlDB			 = mSqLiteHelper.getmDB();
		//	照片路径的配置文件
		folderpath	= mtConfigure.getfParentPath()+File.separator+id+File.separator+"sign";
		photo		= new Photo();//照片对象声明;
		
		ivBack.setBackgroundResource(R.drawable.icon_back);
		btnBack.setOnTouchListener(this);
		btndetail.setOnClickListener(this);
		//	标题名称;
		tvTopic.setText(R.string.topic_sign);
		etorder.setText(id);
		etdeadline.setText(deadline);
		etgoal.setText(goal);
		//	正常按钮点击;
		btngood.setOnClickListener(this);
		btnbad.setOnClickListener(this);
		btnphoto.setOnClickListener(this);
		btnsign.setOnClickListener(this);
		btnsubmit.setOnClickListener(this);
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int nVid	=view.getId();
		int nAction =event.getAction();
		switch (nVid) {
		case R.id.btnBack:
			switch (nAction) {  
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
			break;
		default:
			break;
		}
		return false;
	}
	
	private void jumpActivity(Map<String, String> map){
		String id		= map.get("id");
		String deadline	= map.get("deadline");
		String goal		= map.get("goal");
		String information= map.get("information");
		mIntent			= new Intent(mContext, VDiagActivity.class);
		mBundle			= new Bundle();
		mBundle.putString("id",id);
		String content  =
				"电池名称    "+information+"\r\n\r\n" +
				"地址           "+goal+"\r\n\r\n" +
				"日期           "+deadline+"\r\n\r\n";
		mBundle.putString("content", content);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnsubmit:
//			Log.i("MyLog", "size="+photo.getSize());
			//	进行数据的解析和加载;
			parseToData(id);
			break;
		case R.id.btndetail:
			if(mThread==null){
				
				param="opertype=5&id="+id;
				mThread=new MyThread(mtGetOrPostHelper, param, "init",null,sqlDB);
				mThread.start();
			}
			break;
		case R.id.btngood:
			status=btnView.clickbtn(0);
			break;
		case R.id.btnbad:
			status=btnView.clickbtn(1);
			break;
		//	照相按钮;
		case R.id.btnphoto:
			initPopupWindow(view);
			break;
		//	签字按钮;
		case R.id.btnsign:
			getWritePad(folderpath);
			break;
		default:
			break;
		}
	}
	//	进行数据进行加载;
	class MyThread extends Thread{
		private String 			  		param,oper;
		private MTGetOrPostHelper 		mtGetOrPostHelper;
		private SQLiteDatabase			sqlDB;
		private ArrayList<Photo> 		photolist;
		
		public MyThread(MTGetOrPostHelper mtGetOrPostHelper,String param,String oper,CollectionAndDelivery collectionAndDelivery,SQLiteDatabase sqlDB) {
			this.mtGetOrPostHelper=mtGetOrPostHelper;
			this.param			  =param;
			this.oper			  =oper;
			if(collectionAndDelivery!=null){				
				this.photolist		  =collectionAndDelivery.getMphoto().getPhotolist();
			}
			this.sqlDB			  =sqlDB;
		}
		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String  url	 	 =	null;
			String  response = 	null;
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			JSONArray  array = null;
			JSONObject obj 	 = null;
			if(oper.equals("init")){				
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
				response	 =	mtGetOrPostHelper.sendGet(url,param);	
				Log.i("MyLog", url+"?"+param);
				////返回结果
				if(response!=null){
					try {
						array 	= new JSONArray(response);
					} catch (JSONException e) {
						array	= null;
					}
					if(array!=null){
						int 	i= 0;
							do {
								try {
									Map<String, String> map=new HashMap<String, String>();
									obj 		 	  = array.getJSONObject(i);
									String id	 	  = obj.getString("id");
									String deadline	  = obj.getString("deadline");
									String goal		  =obj.getString("goal");
									String information="";

									map.put("id", id);
									if(id.contains("HS")){
										String model=obj.getString("model");
										String count=obj.getString("count");
										String price=obj.getString("price");
										information	=model+"  x  "+count+"   "+price+"元";
									}else if(id.contains("N")){
										information	=obj.getString("information");
									}
									
									map.put("deadline", deadline);
									map.put("goal", goal);
									map.put("information", information);
									bundle.putSerializable("item", (Serializable) map);
									i++;
								} catch (JSONException e) {
									obj			 =	null;
									break;								
								}
							} while (obj!= null);
						}else nFlag	 = 	MTConfigure.NTAG_FAIL; 						
					}else nFlag	 = 	MTConfigure.NTAG_FAIL;	
			}else if(oper.equals("submit")){
				url			 =	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
				response	 =	mtGetOrPostHelper.sendGet(url,param);
				if(response.trim().equals("success")){
					//	SQL语句的内容;
					String sql			= 
							"delete from collectionanddelivery where id='"+id+"'";
					//	数据库的更新操作;
					sqlDB.execSQL(sql);
					sql					= 
					"insert into collectionanddelivery (id,photo,status) values ('"+id+"','"+collectionAndDelivery.getMphoto().getImageNames()+"','"+collectionAndDelivery.getStatus()+"')";
					//	数据库的更新操作;
					sqlDB.execSQL(sql);
					//	进行数据显示列表;
					Iterator iterator	=	photolist.iterator();
					while (iterator.hasNext()) {
						Photo 	item	=	 (Photo) iterator.next();
						String  name	=	item.getName();
						String  path	=	item.getPath();
						String  urlhead	=	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collectionanddelivery_info";
						String  urlbody =	"?opertype=7&folder="+collectionAndDelivery.getId();
						url				=	urlhead+urlbody;
						Log.i("MyLog", "url="+url);
						response	=	mtGetOrPostHelper.uploadFile(url,path,name);
					}
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}
			bundle.putInt("flag", nFlag);
			bundle.putString("oper", oper);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}		
	}
	//	进行底端对话框显示的内容;
	@SuppressWarnings("deprecation")
	private void initPopupWindow(View parent) {  
	   	if (popupWindow == null) {
	   		//	自定义的布局;
	   		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
	   		//	底端的控件;
			bottomView  = mLayoutInflater.inflate(R.layout.act_popupwindow_sign_n, null);
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
    	RelativeLayout btnback		= (RelativeLayout) bottomView.findViewById(R.id.btnback);
    	//	回收员;
    	RelativeLayout btnphoto		= (RelativeLayout) bottomView.findViewById(R.id.btnphoto);
    	//	回收点;
    	RelativeLayout btnphotoset	= (RelativeLayout) bottomView.findViewById(R.id.btnphotoset);
    	
    	//	进行相应的事件监听;
    	btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
    	btnphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				getPhotoInfo(mtConfigure, folderpath);
				popupWindow.dismiss();
			}
		});
    	btnphotoset.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View view) {
    			showImageList(mContext,photo);
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
	//	照相的相应函数;
	//	进行照相的设置;
	public void getPhotoInfo(MTConfigure configer, String folderpath) {
		File   	   file		 = null;
		String environmentstate=configer.getfState();
		//	判定硬件的状态;
		if (environmentstate.equals(Environment.MEDIA_MOUNTED)) {
			//	文件夹的生成;
			//	获取文件名称;
			long   time      = java.lang.System.currentTimeMillis();
			filename	 	 = String.valueOf(time); 
			file 	   		 = new File(folderpath);
			//	文件存在状态;
			boolean flag=file.exists();
			// 	生成文件夹的方式;
			if (!flag) {
				//	文件生成;
				file.mkdirs();
			}
			// 生成2中文件路径:01.临时的 02.永久的
			temppath   = folderpath + File.separator + filename + "_tmp.jpg";
			filepath   = folderpath + File.separator + filename + ".jpg";
			// 文件夹的内容;
			file 	   = new File(temppath);
			// 文件维护;
			if (file.exists()) {
				file.delete();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(mContext, R.string.error_createimage, Toast.LENGTH_LONG).show();
				}
			}
			//	利用系统的文件内容进行跳转;
			Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			//	额外的文件内哦让那个
			mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(mIntent,4);
			
		} else Toast.makeText(mContext, R.string.error_sdcard, Toast.LENGTH_SHORT).show();
	}
	//	签收按钮;
	//	进行画板的设置;
	private void getWritePad(final String folderpath){
		mtConfigure.setScreenWidthAndHeigth(mContext);
		int width	=(int) (mtConfigure.getScreenWidth()*0.8);
		int heigth	=(int) (mtConfigure.getScreenHeight()*0.8);

		MTWritePadDialog writeTabletDialog = new MTWritePadDialog(
			mContext, new MTDialogListener() {
				@Override
				public void refreshActivity(Object object) {			
					//	进行数据的长宽设置;
					long 	time	   = java.lang.System.currentTimeMillis();
					String  filename   = String.valueOf(time);
					MTWritepadHelper writepadHelper=new MTWritepadHelper(object, folderpath, filename);
					Bitmap 	zoombm	   = writepadHelper.getZoombm();
					String  path	   = writepadHelper.getPath();
					if(zoombm!=null){
						photo.getPhotolist(filename, path);
						Toast.makeText(mContext, R.string.success_writepad, Toast.LENGTH_SHORT).show();
					}else Toast.makeText(mContext, R.string.error_writepad, Toast.LENGTH_SHORT).show();
				}
			},width,heigth);
		writeTabletDialog.show();
	}
	private ListView 		listview;
	//	相册集合;
	@SuppressWarnings("deprecation")
	private void showImageList(Context context,Photo photo){
		Builder 	builder= new Builder(context);
		builder.setTitle(R.string.collection_image);
		View  		view   = getLayoutInflater().inflate(R.layout.act_img, null);
		float screenWidth  = getWindowManager().getDefaultDisplay().getWidth();     // 屏幕宽（像素，如：480px）  
		float screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
		listview 		   = (ListView) view.findViewById(R.id.listview);
		ListAdapter listAdapter= new ListAdapter(context,photo,screenWidth,screenHeight);
		listview.setAdapter(listAdapter);
		builder.setNegativeButton(R.string.ok, null);
		builder.setView(view);
		//	对话框进行显示;
		builder.create();
		builder.show();
	}
	//	listview适配器-内部类;
	public class ListAdapter extends BaseAdapter {
		private Context 	context;
		private Photo 	photo;
		private float 		screenWidth,screenHeight;
		public ListAdapter(Context context,Photo photo,float screenWidth,float screenHeight) {
			this.context	 = context;
			this.photo		 = photo;
			this.screenWidth = screenWidth;
			this.screenHeight= screenHeight;
		}
		@Override
		public int getCount() {
			return photo.getSize();
		}

		@Override
		public Object getItem(int position) {
			return photo.getPhotolist().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder 	 = new ViewHolder();
				convertView  = LayoutInflater.from(context).inflate(R.layout.act_item_image, null);
				
				viewHolder.txt_content = (TextView) convertView.findViewById(R.id.text);
				viewHolder.txt_delete  = (TextView) convertView.findViewById(R.id.text_delete);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			//设置内容
			viewHolder.txt_content.setText(photo.getPhotolist().get(position).getName()+".jpg");

			viewHolder.txt_content.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String path=photo.getPhotolist().get(position).getPath();
					showImage(context, path,screenWidth,screenHeight);
				}
			});
			 //删除按钮
	        viewHolder.txt_delete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	//	单条路径;
	            	String  path = photo.getPhotolist().get(position).getPath();
	            	File 	file = new File(path);
	            	//	判断文件是否存在;
	            	boolean flag = file.exists();
	            	if(flag){
	            		file.delete();
	            		photo.removePhotolist(position);
	            	}
	            	notifyDataSetChanged();
	            }
	        }); 
			return convertView;
		}
		private class ViewHolder {
			private TextView txt_content;
			private TextView txt_delete;
		}
	}
	//
	@SuppressWarnings("deprecation")
	private void showImage(Context context,String imagePath,float screenWidth,float screenHeight){
		Builder 	builder= new Builder(context);
		builder.setTitle(R.string.show_image);
		
		View  		view   = LayoutInflater.from(context).inflate(R.layout.act_img_show, null);
		ImageView 	iv	   = (ImageView) view.findViewById(R.id.imgShow);
        Bitmap 		bm 	   = BitmapFactory.decodeFile(imagePath);
        int 		width  = bm.getWidth();  
        int 		height = bm.getHeight();  
  
        //放大為屏幕的1/2大小  
        float scaleWidth   = screenWidth/2/width;  
        float scaleHeight  = screenHeight/2/height;  
  
        // 取得想要缩放的matrix參數  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);

        iv.setBackgroundDrawable(new BitmapDrawable(context.getResources(),newbm));
        builder.setNegativeButton(R.string.ok, null);
        builder.setView(view);
        builder.create();
        builder.show();
	}
	//	进行数据的提交;
	private void parseToData(String id){

		if(collectionAndDelivery==null){
			collectionAndDelivery=new CollectionAndDelivery();
		}
		collectionAndDelivery=collectionAndDelivery.getInfo(id, null, photo.getImageNames(), status, photo);
		
		if(collectionAndDelivery!=null){			
			confirmSubmit(mContext, mtGetOrPostHelper, collectionAndDelivery);
		}else Toast.makeText(mContext, R.string.error_input, Toast.LENGTH_SHORT).show();
	}
	//	确认上传;
	private void confirmSubmit(Context context,final MTGetOrPostHelper mtGetOrPostHelper,final CollectionAndDelivery collectionAndDelivery){
		vBuilder=new Builder(context);
		vBuilder.setTitle(R.string.confirm_info);
		String state=collectionAndDelivery.getStatus();
		String status="正常";
		if(state.equals("1"))status="异常";
		String message=
				"[订单编号]   "+collectionAndDelivery.getId()+"\r\n" +
				"[图片张数]   "+collectionAndDelivery.getMphoto().getListsize()+"张\r\n" +
				"[商品状态]   "+status
				;
		vBuilder.setMessage(message);
		vBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(mThread==null){								
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					//	数据SQL语句;
//					String sql="insert into collectionanddelivery (id,photo,status) values ('"+id+"','"+collectionAndDelivery.getMphoto().getImageNames()+"','"+collectionAndDelivery.getStatus()+"')";
//					sqlDB.execSQL(sql);
					param	= "opertype=6&id="+id+"&photo="+collectionAndDelivery.getMphoto().getImageNames()+"&status="+collectionAndDelivery.getStatus();
					mThread = new MyThread(mtGetOrPostHelper, param, "submit",collectionAndDelivery,sqlDB);
					mThread.start();	
				}
			}
		});
		vBuilder.setNegativeButton(R.string.no, null);
		vBuilder.create();
		vBuilder.show();
	}

	// 返回键
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//	照相的返回功能;
		if (requestCode == 4&& resultCode == -1) {
			Toast.makeText(mContext, R.string.complete_photo, Toast.LENGTH_SHORT).show();
			MTPictureHelper pictureHelper=new MTPictureHelper();
			pictureHelper.compressPicture(temppath, filepath);
			pictureHelper.clearPicture(temppath, null);
			//	获得照片的路径;
			String 	path	=	pictureHelper.getPath();
			photo.getPhotolist(filename, path);
		} 
	}
}

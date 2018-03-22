package com.view;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import com.hqbs.app.R;
import com.model.entity.MECollection;
import com.model.entity.MEPhoto;
import com.model.tool.system.MTDialogListener;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTGetOrPostHelper;
import com.model.tool.system.MTPictureHelper;
import com.model.tool.system.MTSQLiteHelper;
import com.model.tool.system.MTWritepadHelper;
import com.model.tool.view.MTEditTextWithDel;
import com.model.tool.view.MTWritePadDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VDeliveryDetailActivity extends Activity implements OnClickListener,OnFocusChangeListener{
	/*上下文的内容*/
	private Context mContext;
	private Intent  mIntent;
	private Bundle	mBundle;
	private Resources mResources;
	/*布局控件内容*/
	private TextView vTopic,vBack;
	private TextView tvownner,tvdriver,tvmodel,tvprice,tvcount,tvgoal,tvdeadline;
	private Button   btnLocation,btnCount,btnPhoto,btnSign,btnSumit;
	private Spinner  spState;
	private MTEditTextWithDel etmessage;
	private LinearLayout laystate;
	private Builder	 vBuilder;
	/*自定义对象类*/
	private MTConfigure	 mtConfigure;
	/*自定义的参数*/
	private String driverId,driverName,price,count,lng,lat,goal,folderpath,filepath,filename,model,temppath,state,did;
	//	照片的管理器;
	private MEPhoto   	 	photo;
	private ListView 		listview;
	private MECollection	collection;
	//	使用的线程;
	private	ProgressDialog	vDialog;	// 对话框;
	private MyThread		myThread;	// 线程;
	private MTGetOrPostHelper mtGetOrPostHelper;
	private MTSQLiteHelper 	    mSqLiteHelper;// 数据库的帮助类;
	private SQLiteDatabase 	    mDB; // 数据库件;
	
	//	handler控件;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//	控制符的标签;
			Bundle bundle= msg.getData();
			int    nFlag = bundle.getInt("flag");
			vDialog.dismiss();
			switch (nFlag) {
			case 1:
				//	成功的操作;
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 2:
				//	失败的操作;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			closeThread();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_delivery_detail);
		initView();
		initEvent();
	}
	//	控件的初始化;
	private void initView(){
		//	标题内容;
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		//	返回键值;
		vBack 	=(TextView) findViewById(R.id.btnBack);
		//	所属站点;
		tvownner=(TextView) findViewById(R.id.tvownner);
		//	司机名称;
		tvdriver=(TextView) findViewById(R.id.tvdriver);
		//	电池名称;
		tvmodel	=(TextView) findViewById(R.id.tvmodel);
		//	电池单价;
		tvprice	=(TextView) findViewById(R.id.tvprice);
		//	商品个数;
		tvcount	=(TextView) findViewById(R.id.tvcount);
		//	目标地点;
		tvgoal	=(TextView) findViewById(R.id.tvgoal);
		//	截止时间;
		tvdeadline=(TextView) findViewById(R.id.tvdeadline);
		//	定位按钮;
		btnLocation	=(Button) findViewById(R.id.btnLocation);
		//	照片张数;
		btnCount	=(Button) findViewById(R.id.btnCount);
		//	拍照按钮;
		btnPhoto	=(Button) findViewById(R.id.btnPhoto);
		//	签收按钮;
		btnSign		=(Button) findViewById(R.id.btnSign);
		//	状态选择;
		spState		=(Spinner) findViewById(R.id.spState);
		//	输入框值;
		etmessage	=(MTEditTextWithDel) findViewById(R.id.etmessage);
		//	状态布局;
		laystate	=(LinearLayout) findViewById(R.id.laystate);
		//	提交按钮;
		btnSumit	=(Button) findViewById(R.id.btnSumit);
		
	}
	//	事件的初始化;
	private void initEvent(){
		if(mContext==null){
			mContext=VDeliveryDetailActivity.this;
		}
		mtConfigure		 = new MTConfigure();
		//	资源的内容;
		mResources		 = getResources();
		/*数据库的相应内容*/
		mSqLiteHelper	 = new MTSQLiteHelper(mContext);
		mDB				 = mSqLiteHelper.getmDB();
		/*获得相应的数据信息*/
		initData();
		/*数据的初始化内容*/
		//	标题内容;
		vTopic.setText("商品签收");
		//	返回值钮;
		vBack.setText("返回");
		vBack.setVisibility(View.VISIBLE);
		/*	添加监听内容*/
		vBack.setOnClickListener(this);
		btnLocation.setOnClickListener(this);
		//	照片张数;
		btnCount.setOnClickListener(this);
		//	拍照按钮;
		btnPhoto.setOnClickListener(this);
		//	签收按钮;
		btnSign.setOnClickListener(this);
		//	输入框值;
		etmessage.setOnFocusChangeListener(this);
		//	状态列表;
		spState.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				state=adapter.getItemAtPosition(position).toString();  
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				state="正常";
			}
		});
		//	提交按钮;
		btnSumit.setOnClickListener(this);
		//	设置初始化参数;
		//	01.品牌;
		model		= tvmodel.getText().toString();
		//	02.文件夹路径;
		folderpath	= mtConfigure.getfParentPath()+File.separator+driverId+File.separator+model;
		//	03.照片内容;
		photo		= new MEPhoto();
		//	网络的控件;
		mtGetOrPostHelper=new MTGetOrPostHelper();
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	返回值按钮;
		case R.id.btnBack:
			finish();
			break;
		//	定位值按钮;
		case R.id.btnLocation:
			//	进行地图的定位点;
			mIntent=new Intent(mContext, VMapShowActivity.class);
			mBundle=new Bundle();
			mBundle.putString("lng", lng);
			mBundle.putString("lat", lat);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			break;
		//	张数值按钮;
		case R.id.btnCount:
			showImageList(mContext,photo);
			break;
		//	拍照按钮;
		case R.id.btnPhoto:
			getPhotoInfo(mtConfigure, folderpath);
			break;
		//	签收按钮;
		case R.id.btnSign:
			getWritePad(folderpath);
			break;
		//	提交按钮;
		case R.id.btnSumit:
			if(collection==null){
				collection=new MECollection();
			}

			collection=collection.getCollectionInfo(tvownner, driverId,driverName,model,price,count,photo.getImageNames(), tvdeadline, etmessage, lng, lat,goal,state,photo,did);
			confirmSubmit(mContext, mtGetOrPostHelper, collection, tvdriver.getText().toString());
			break;
		default:
			break;
		}	
	}
	//	数据信息的初始化;
	private void initData(){
		mIntent	  =getIntent();
		mBundle	  =mIntent.getExtras();
		String ownner	=	mBundle.getString("ownner");
		driverId		=	mBundle.getString("driverId");
		driverName		=	mBundle.getString("driverName");
		model			=	mBundle.getString("model");
		price			=	mBundle.getString("price");
		count			=	mBundle.getString("count");
		lng				=	mBundle.getString("lng");
		lat				=	mBundle.getString("lat");
		String deadline	=	mBundle.getString("deadline");
		goal			=	mBundle.getString("goal");
		did				=	mBundle.getString("id");
		tvownner.setText(ownner);
		tvdriver.setText(driverName);
		tvmodel.setText(model);
		tvprice.setText(price+"元/件");
		tvcount.setText(count+"件");
		tvdeadline.setText(deadline);
		tvgoal.setText(goal);
	}
	
	//	输入框的颜色变化;
	@Override
	public void onFocusChange(View view, boolean flag) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.etmessage:
			//	输入框的显示内容;
			mtConfigure.setViewDrawable(flag, mResources, laystate, R.drawable.shape_edit0, R.drawable.shape_edit1);
			break;
		default:
			break;
		}
	}
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
							btnCount.setText(photo.getListsize());
						}else Toast.makeText(mContext, R.string.error_writepad, Toast.LENGTH_SHORT).show();
					}
				},width,heigth);
		writeTabletDialog.show();
	}
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
			btnCount.setText(photo.getListsize());
		} 
	}
	//	显示照片集合
	@SuppressWarnings("deprecation")
	private void showImageList(Context context,MEPhoto photo){
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
		private MEPhoto 	photo;
		private float 		screenWidth,screenHeight;
		public ListAdapter(Context context,MEPhoto photo,float screenWidth,float screenHeight) {
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
	            	btnCount.setText(photo.getListsize());
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
	//	线程的自定义形式;
	class MyThread extends Thread{
		
		private MTGetOrPostHelper  mGetOrPostHelper;
		private MECollection	   collection;
		private MEPhoto 	  	   mPhoto;
		private ArrayList<MEPhoto> photolist;
		private SQLiteDatabase 	   mDB;
		
		public MyThread(MTGetOrPostHelper mGetOrPostHelper,MECollection collection,SQLiteDatabase mDB) {
			this.mGetOrPostHelper=  mGetOrPostHelper;
			this.collection		 =	collection;
			this.mDB			 =  mDB;
			this.mPhoto		 	 =	this.collection.getMePhoto();
			this.photolist		 =	this.mPhoto.getPhotolist();
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			int     nFlag	 = 	MTConfigure.NTAG_SUCCESS;
			Message	msg		 =  new Message();
			Bundle	bundle	 =	new Bundle();
			//  传值;
			String  urlhead  = null;
			String  urlbody  = null;
			String  response = null;
			String  sql		 = null;
			urlhead			 = "http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collection_info";
			try {
				urlbody		 = 
						"opertype=1&" +
						"ownner="+URLEncoder.encode(collection.getOwnner(),"utf-8")+"&" +
						"driver="+URLEncoder.encode(collection.getDriver(),"utf-8")+"&" +
						"photo="+collection.getPhoto()+"&" +
						"deadline="+URLEncoder.encode(collection.getDeadline(),"utf-8")+"&" +
						"message="+URLEncoder.encode(collection.getMessage(),"utf-8")+"&" +
						"lng="+collection.getLng()+"&" +
						"lat="+collection.getLat()+"&" +
						"state="+URLEncoder.encode(collection.getState(),"utf-8")+"&" +
						"id="+collection.getDid();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response		 =	mGetOrPostHelper.sendGet(urlhead,urlbody);
			if(response!=null){
				if(response.trim().equals("success")){
					//	SQL语句的内容;
					sql				 = 
					"insert into driver_history " +
					"(did,ownner,driver,name,model,price,count,photo,deadline,message,lng,lat,goal,state) values ("+collection.getDid()+",'"+collection.getOwnner()+"','"+collection.getDriver()+"','"+collection.getName()+"','"+collection.getModel()+"','"+collection.getPrice()+"','"+collection.getCount()+"','"+collection.getPhoto()+"','"+collection.getDeadline()+"','"+collection.getMessage()+"','"+collection.getLng()+"','"+collection.getLat()+"','"+collection.getGoal()+"','"+collection.getState()+"')";
					//	数据库的更新操作;
					mDB.execSQL(sql);
					//	进行数据显示列表;
					Iterator iterator	=	photolist.iterator();
					while (iterator.hasNext()) {
						MEPhoto item	=	(MEPhoto) iterator.next();
						String  name	=	item.getName();
						String  path	=	item.getPath();
						urlhead			=	"http://"+MTConfigure.TAG_IP_ADDRESS+":"+MTConfigure.TAG_PORT+"/"+MTConfigure.TAG_PROGRAM+"/collection_info";
						urlbody 		=	"?opertype=2&folder="+collection.getDriver()+"_"+collection.getDeadline();
						String	url		=	urlhead+urlbody;
						response	=	mGetOrPostHelper.uploadFile(url,path,name);
					}
				}else nFlag	 = 	MTConfigure.NTAG_FAIL;
			}
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}
	}
	//	关闭线程;
	private void closeThread(){
		if(myThread!=null){
			myThread.interrupt();
			myThread=null;
		}
	}
	//	确认上传;
	private void confirmSubmit(Context context,final MTGetOrPostHelper mtGetOrPostHelper,final MECollection collection,String name){
		vBuilder=new Builder(context);
		vBuilder.setTitle(R.string.confirm_info);
		String message=
				"[站点所属]   "+collection.getOwnner()+"\r\n" +
				"[司机名称]   "+name+"\r\n" +
				"[图片张数]   "+collection.getMePhoto().getListsize()+"张\r\n" +
				"[截止时间]   "+collection.getDeadline()+"\r\n" +
				"[商品状态]   "+collection.getState()+"\r\n" +
				"[发送信息]   "+collection.getMessage()
				;
		vBuilder.setMessage(message);
		vBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(myThread==null){								
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread= new MyThread(mtGetOrPostHelper, collection,mDB);
					myThread.start();	
				}
			}
		});
		vBuilder.setNegativeButton(R.string.no, null);
		vBuilder.create();
		vBuilder.show();
	}
}

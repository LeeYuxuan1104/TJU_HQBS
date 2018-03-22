package com.view;
import java.io.File;

import com.hqbs.app.R;
import com.model.entity.MEPhoto;
import com.model.tool.system.MTConfigure;
import com.model.tool.system.MTSQLiteHelper;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VDeliveryHistoryActivity extends Activity implements OnClickListener{
	/*文件的上下文内容*/	
	private Context mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	/*控件的相应内容*/
	private TextView vTopic,vBack,vDel;
	private TextView tvownner,tvdriver,tvmodel,tvprice,tvcount,tvgoal,tvdeadline,tvState,tvMessage;
	private Button btnLocation,btnCount;
	/*数据库的相应内容*/
	private MTSQLiteHelper 	    mSqLiteHelper;// 数据库的帮助类;
	private SQLiteDatabase 	    mDB; // 数据库件;
	private Cursor 		   	  	mCursor;	  // 数据库遍历签;
	/*参数定义内容*/
	private String				did,sql,townner,driver,name,model,tprice,tcount,tgoal,tdeadline,tState,tMessage,lng,lat,photo,folderpath;
	private MEPhoto				mphoto;
	private MTConfigure			mtConfigure;
	private ListView 			listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_delivery_history);
		//	控件初始化;
		initView();
		//	事件初始化;
		initEvent();
		//	数据初始化;
		initData();
	}
	//	进行控件初始化;
	private void initView(){
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vBack =(TextView) findViewById(R.id.btnBack);
		vDel  =(TextView) findViewById(R.id.btnFunction);
		tvownner=(TextView) findViewById(R.id.tvownner);
		tvdriver=(TextView) findViewById(R.id.tvdriver);
		tvmodel=(TextView) findViewById(R.id.tvmodel);
		tvprice=(TextView) findViewById(R.id.tvprice);
		tvcount=(TextView) findViewById(R.id.tvcount);
		tvgoal=(TextView) findViewById(R.id.tvgoal);
		tvdeadline=(TextView) findViewById(R.id.tvdeadline);
		tvState=(TextView) findViewById(R.id.tvState);
		tvMessage=(TextView) findViewById(R.id.tvMessage);
		btnLocation=(Button) findViewById(R.id.btnLocation);
		btnCount=(Button) findViewById(R.id.btnCount);
	}
	//	进行事件初始化;
	private void initEvent(){
		if(mContext==null){
			mContext=VDeliveryHistoryActivity.this;
		}
		mtConfigure		 = new MTConfigure();
		/*数据库的相应内容*/
		mSqLiteHelper	 = new MTSQLiteHelper(mContext);
		mDB				 = mSqLiteHelper.getmDB();
		/*对象事例声明*/
		mphoto			 = new MEPhoto();
		/*事件进行监听添加*/
		vTopic.setText("历史详情");
		vBack.setText("返回");
		vBack.setVisibility(View.VISIBLE);
		vDel.setText("删除");
//		vDel.setVisibility(View.VISIBLE);
		vBack.setOnClickListener(this);
		vDel.setOnClickListener(this);
		btnLocation.setOnClickListener(this);
		btnCount.setOnClickListener(this);
	}
	//	进行数据的初始化;
	private void initData(){
		mIntent	 =	getIntent();
		mBundle	 =	mIntent.getExtras();
		did		 =	mBundle.getString("id");
		sql		 = "select * from driver_history where did="+did;
		
		mCursor	 =	mDB.rawQuery(sql, null);
		while (mCursor.moveToNext()) {	
			townner		=	mCursor.getString(mCursor.getColumnIndex("ownner")).toString();
			driver		=	mCursor.getString(mCursor.getColumnIndex("driver")).toString();
			name		=	mCursor.getString(mCursor.getColumnIndex("name")).toString();
			model		=	mCursor.getString(mCursor.getColumnIndex("model")).toString();
			tprice		=	mCursor.getString(mCursor.getColumnIndex("price")).toString();
			tcount		=	mCursor.getString(mCursor.getColumnIndex("count")).toString();
			tgoal		=	mCursor.getString(mCursor.getColumnIndex("goal")).toString();
			tdeadline	=	mCursor.getString(mCursor.getColumnIndex("deadline")).toString();
			photo		=	mCursor.getString(mCursor.getColumnIndex("photo")).toString();
			tState		=	mCursor.getString(mCursor.getColumnIndex("state")).toString();
			tMessage	=	mCursor.getString(mCursor.getColumnIndex("message")).toString();
			lng			=	mCursor.getString(mCursor.getColumnIndex("lng")).toString();
			lat			=	mCursor.getString(mCursor.getColumnIndex("lat")).toString();
			
		}
		tvownner.setText(townner);
		tvdriver.setText(name);
		tvmodel.setText(model);
		tvprice.setText(tprice+"元/件");
		tvcount.setText(tcount+"件");
		tvdeadline.setText(tdeadline);
		tvState.setText(tState);
		tvgoal.setText(tgoal);
		tvMessage.setText(tMessage);
		if(mCursor!=null){
			mCursor.close();
		}
		folderpath		=	mtConfigure.getfParentPath()+File.separator+driver+File.separator+model;
		String[] names	=	photo.split("_");
		for(String name:names){	
			String path = 	folderpath+File.separator+name+".jpg";
			mphoto.getPhotolist(name, path);
		}
		btnCount.setText(mphoto.getListsize());
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	返回键;
		case R.id.btnBack:
			finish();
			break;
		//	删除键;
		case R.id.btnFunction:
			sql="delete from driver_history where did="+did;
			mDB.execSQL(sql);
			finish();
			break;
		//	定位按钮;
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
			showImageList(mContext,mphoto);
		default:
			break;
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
				viewHolder.txt_delete.setVisibility(View.GONE);
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
}

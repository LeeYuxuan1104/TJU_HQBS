package com.view;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.hqbs.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VMapShowActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	//	地图控件;
	private MapView vMapView;
	//	地图类;
	private AMap	aMap;
	private Marker	marker;
	//	地图样式;
	private TextView vBack,vTopic;
	private double	 lng,lat;
	//	进行初始化的构造函数;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_map);
		initView();
		initEvent(savedInstanceState);
	}
	//	初始化控件;
	private void initView(){
		//	地图控件;
		vMapView=(MapView) findViewById(R.id.map);
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		vBack=(TextView) findViewById(R.id.btnBack);
	    
	}
	//	初始化事件;
	private void initEvent(Bundle savedInstanceState){
		if(mContext==null){
			mContext=VMapShowActivity.this;
		}
		vBack.setVisibility(View.VISIBLE);
		vBack.setText("取消");
		vTopic.setText("目的地");
		mIntent=getIntent();
		mBundle=mIntent.getExtras();
		String slng=mBundle.getString("lng");
		String slat=mBundle.getString("lat");
		lng=Double.parseDouble(slng);
		lat=Double.parseDouble(slat);
		vBack.setOnClickListener(this);
		//	地图控件的初始化内容信息;
		vMapView.onCreate(savedInstanceState);// 此方法必须重写
		aMap 	= vMapView.getMap();
		
		//绘制marker
	    marker = aMap.addMarker(new MarkerOptions()
	        .position(new LatLng(lat,lng))
	        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
	            .decodeResource(getResources(),R.drawable.gps_point)))
	        .draggable(true));
	    //	显示坐标的地图中心;
	    aMap. moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));

	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;

		default:
			break;
		}
		
	}
}

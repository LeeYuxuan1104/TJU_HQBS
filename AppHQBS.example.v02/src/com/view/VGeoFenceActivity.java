package com.view;

import java.util.List;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.hqbs.app.R;
import com.model.tool.view.MTCheckPermissionsActivity;
import com.model.tool.view.MTEditTextWithDel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("HandlerLeak")
public class VGeoFenceActivity extends MTCheckPermissionsActivity implements
GeoFenceListener,
OnMapClickListener,
LocationSource,
AMapLocationListener,
OnCheckedChangeListener,
OnClickListener{
	
	//	上下文的内容;
	private Context 	   mContext;
	private Intent	 	   mIntent; 
	private Bundle 	 	   mBundle;
	//	上方的控件;
	private TextView 	   tvTopic,ivBack;
	private RelativeLayout btnBack;
	private MTEditTextWithDel etAddress;
	
	//	下方的按钮;
	private Button 		   btnsubmit;
	/*相关的地图内容*/
	private AMapLocationClient mlocationClient;
	//	定位的事件监听;
	private OnLocationChangedListener mListener;
	//	地图定位选项;
	private AMapLocationClientOption mLocationOption;
	//	视图层的内容;
	private MapView mMapView;
	//	地图控件内容;
	private AMap 	mAMap;
	//  中心点坐标
	private LatLng  centerLatLng = null;
	//  中心点marker
	private Marker  centerMarker;
	private BitmapDescriptor ICON_YELLOW = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
	private MarkerOptions markerOption = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_geofence_n);
		
		mMapView 	 = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		markerOption = new MarkerOptions().draggable(true);
		//	初始化控件;
		initView();
		//	初始化事件;
		initEvent();
	}
	//	初始化控件;
	private void initView(){
		//	返回按钮;
		btnBack=(RelativeLayout) findViewById(R.id.btnBack);
		tvTopic=(TextView) findViewById(R.id.tvTopic);
		ivBack =(TextView) findViewById(R.id.ivBack);
		//	提交按钮;
		btnsubmit=(Button) findViewById(R.id.btnsubmit);
		//	地址输入框;
		etAddress=(MTEditTextWithDel) findViewById(R.id.etaddress);
		
	}
	//	初始化事件;
	private void initEvent(){
		if(mContext==null){
			mContext=VGeoFenceActivity.this;
		}
		btnBack.setOnClickListener(this);
		btnsubmit.setOnClickListener(this);
		tvTopic.setText(R.string.map);
		ivBack.setBackgroundResource(R.drawable.icon_back);
		//	返回键;
		btnBack.setOnTouchListener(new OnTouchListener() {
				
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {  
                case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域  
                	ivBack.getBackground().setAlpha(255);
                	//  经度;
        			mBundle.putString("lng", "-1");
        			//	纬度;
        			mBundle.putString("lat", "-1");
        			//  地址;
        			mBundle.putString("address", "");
        			mIntent.putExtras(mBundle);
        			setResult(1, mIntent);
        			finish();
                    break;  
                case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域  
                	ivBack.getBackground().setAlpha(10);
                    break;  
                default:  
                    break;  
			}
				return true;
			}
		});
		if (mAMap == null) {
			mAMap = mMapView.getMap();
			mAMap.getUiSettings().setRotateGesturesEnabled(false);
			mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
			setUpMap();
		}
		mIntent	=	getIntent();
		mBundle =	new Bundle();
	}
	/**
	 * 设置一些amap的属性
	 */
	@SuppressWarnings("deprecation")
	private void setUpMap() {
		mAMap.setOnMapClickListener(this);
		mAMap.setLocationSource(this);// 设置定位监听
		mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle.myLocationIcon(
				BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(0);
		// 设置圆形的填充颜色
		myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
		// 将自定义的 myLocationStyle 对象添加到地图上
		mAMap.setMyLocationStyle(myLocationStyle);
		mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		deactivate();
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		if (null != mlocationClient) {
			mlocationClient.onDestroy();
		}
	}

	@Override
	public void onGeoFenceCreateFinished(final List<GeoFence> geoFenceList,
			int errorCode, String customId) {
		// TODO
	}
	//	点击地图以后,出现在地图上方的坐标;
	@Override
	public void onMapClick(LatLng latLng) {
		//	自身定位的黄色图标;
		markerOption.icon(ICON_YELLOW);
		//	中心点的位置;
		centerLatLng = latLng;
		//	添加中心坐标;
		addCenterMarker(centerLatLng);
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 只是为了获取当前位置，所以设置为单次定位
			mLocationOption.setOnceLocation(true);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}
	//	增加中心的坐标;
	private void addCenterMarker(LatLng latlng) {
		if (null == centerMarker) {
			centerMarker = mAMap.addMarker(markerOption);
		}
		centerMarker.setPosition(latlng);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	提交的按钮;
		case R.id.btnsubmit:
			if(centerLatLng==null){
				//	经度;
				mBundle.putString("lng","-1");
				//	纬度;
				mBundle.putString("lat","-1");
			}else{				
				//	经度;
				mBundle.putString("lng", centerLatLng.longitude+"");
				//	纬度;
				mBundle.putString("lat", centerLatLng.latitude+"");
			}
			
			break;
		default:
			break;
		}
		//	地址;
		mBundle.putString("address", etAddress.getText().toString());
		mIntent.putExtras(mBundle);
		setResult(1, mIntent);
		finish();
	}
}

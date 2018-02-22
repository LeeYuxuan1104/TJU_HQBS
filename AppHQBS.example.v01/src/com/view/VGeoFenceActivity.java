package com.view;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.hqbs.app.R;
import com.model.tool.view.CheckPermissionsActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class VGeoFenceActivity extends CheckPermissionsActivity
		implements
			OnClickListener,
			GeoFenceListener,
			OnMapClickListener,
			LocationSource,
			AMapLocationListener,
			OnCheckedChangeListener {

	private TextView tvGuide;
	private TextView tvResult;

	/**
	 * 用于显示当前的位置
	 * <p>
	 * 示例中是为了显示当前的位置，在实际使用中，单独的地理围栏可以不使用定位接口
	 * </p>
	 */
	private AMapLocationClient mlocationClient;
	//	定位的事件监听;
	private OnLocationChangedListener mListener;
	//	地图定位选项;
	private AMapLocationClientOption mLocationOption;
	//	视图层的内容;
	private MapView mMapView;
	//	地图控件内容;
	private AMap mAMap;
	//  中心点坐标
	private LatLng centerLatLng = null;
	//  中心点marker
	private Marker centerMarker;
	private BitmapDescriptor ICON_YELLOW = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
	private MarkerOptions markerOption = null;

	//	控件内容;
	private TextView vBack,vSubmit,vTopic;
	private Intent	 mIntent; 
	private Bundle 	 mBundle;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_geofence);

		tvGuide  = (TextView) findViewById(R.id.tv_guide);
		tvResult = (TextView) findViewById(R.id.tv_result);
		tvResult.setVisibility(View.GONE);

		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		markerOption = new MarkerOptions().draggable(true);
		initView();
		initEvent();
	}
	private void initView(){
		vBack	=(TextView) findViewById(R.id.btnBack);
		vSubmit	=(TextView) findViewById(R.id.btnFunction);
		vTopic	=(TextView) findViewById(R.id.tvTopic);
	}
	
	void initEvent() {
		if (mAMap == null) {
			mAMap = mMapView.getMap();
			mAMap.getUiSettings().setRotateGesturesEnabled(false);
			mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
			setUpMap();
		}

		resetView_round();

		vBack.setText(R.string.no);
		vSubmit.setText(R.string.ok);
		vBack.setVisibility(View.VISIBLE);
		vSubmit.setVisibility(View.VISIBLE);
		vTopic.setText(R.string.locate);
		vBack.setOnClickListener(this);
		vSubmit.setOnClickListener(this);
		mIntent	=	getIntent();
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
	public void onClick(View v) {
		int nVid=v.getId();
		switch (nVid) {
		case R.id.btnBack:
			mBundle=new Bundle();
			//	经度;
			mBundle.putDouble("lng", -1);
			//	纬度;
			mBundle.putDouble("lat", -1);
			mIntent.putExtras(mBundle);
			setResult(1, mIntent);
			finish();
			break;
		case R.id.btnFunction:
			mBundle=new Bundle();
			//	经度;
			mBundle.putDouble("lng", centerLatLng.longitude);
			//	纬度;
			mBundle.putDouble("lat", centerLatLng.latitude);
			mIntent.putExtras(mBundle);
			setResult(1, mIntent);
			finish();
			break;
		default:
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 2 :
					String statusStr = (String) msg.obj;
					tvResult.setVisibility(View.VISIBLE);
					tvResult.append(statusStr + "\n");
					break;
				default :
					break;
			}
		}
	};

	List<GeoFence> fenceList = new ArrayList<GeoFence>();
	@Override
	public void onGeoFenceCreateFinished(final List<GeoFence> geoFenceList,
			int errorCode, String customId) {
		Message msg = Message.obtain();
		if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
			fenceList = geoFenceList;
			msg.obj = customId;
			msg.what = 0;
		} else {
			msg.arg1 = errorCode;
			msg.what = 1;
		}
		handler.sendMessage(msg);
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
		//	提示栏的背景颜色;
		tvGuide.setBackgroundColor(getResources().getColor(R.color.gary));
		//	提示栏编写坐标系;
		tvGuide.setText(centerLatLng.longitude + " | "+ centerLatLng.latitude);
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				tvResult.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				tvResult.setVisibility(View.VISIBLE);
				tvResult.setText(errText);
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
	//	视图的背景颜色;
	private void resetView_round() {
		tvGuide.setBackgroundColor(getResources().getColor(R.color.red));
		tvGuide.setText("请选择站点坐标");
		tvGuide.setVisibility(View.VISIBLE);
	}
}

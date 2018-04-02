package com.model.entity;

import android.widget.EditText;
public class User extends Information{
	private String id;
	private String nickname;
	private String password;
	private String status;
	private String lng;
	private String lat;
	private String car_number;
	private String type;
	private String address;
	
	public User() {
		super();
	}

	public User(String id,String nickname, String password, String status,
			String lng, String lat, String car_number, String type) {
		super();
		this.id=id;
		this.nickname = nickname;
		this.password = password;
		this.status = status;
		this.lng = lng;
		this.lat = lat;
		this.car_number = car_number;
		this.type = type;
	}
	public User(String id,String nickname, String password, String status,
			String lng, String lat, String car_number, String type,String address) {
		super();
		this.id=id;
		this.nickname = nickname;
		this.password = password;
		this.status = status;
		this.lng = lng;
		this.lat = lat;
		this.car_number = car_number;
		this.type = type;
		this.address=address;
	}
	//	进行数据的获得和设置;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	//	获得信息-基本;
	public User getInfo(EditText etid,EditText etname,EditText etpwd,String status,String lng,String lat,EditText etcar_number,String type,String address ){
		User user=null;
		String id		=	mtConfigure.getEditText(etid);
		String nickname =	mtConfigure.getEditText(etname);
		String password =	mtConfigure.getEditText(etpwd);
		String car_number=	mtConfigure.getEditText(etcar_number);
		boolean flag	=	true;
		if(car_number==null&&address.equals("null")){
			flag=false;
		}
		if(nickname!=null&&password!=null&&flag){
			
			user	=	new User(id, nickname, password,status, lng, lat, car_number, type,address);
		}
		return user;
	}
	//	获得信息-重载;
	public User getInfo(EditText etid,EditText etpwd){
		User user=null;
		String id=mtConfigure.getEditText(etid);
		String password=mtConfigure.getEditText(etpwd);
		if(id!=null&&password!=null){
			user=new User(id, null, password, null, null, null,null, null);
		}
		return user;
	}
	
}

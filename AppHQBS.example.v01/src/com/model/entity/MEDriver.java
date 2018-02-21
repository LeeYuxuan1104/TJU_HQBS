package com.model.entity;

import com.model.tool.system.MTConfigure;

import android.widget.EditText;

public class MEDriver {
	private String id;
	private String password;
	private String carNumber;
	private String tel;
	private double lng;
	private double lat;
	private String name;
	private MTConfigure mtConfigure;
	public MEDriver(String id, String password, String carNumber, String tel,
			double lng, double lat, String name) {
		this.id = id;
		this.password = password;
		this.carNumber = carNumber;
		this.tel = tel;
		this.lng = lng;
		this.lat = lat;
		this.name = name;
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}
	
	public MEDriver(String id, String password, String carNumber, String tel,
			String name) {
		this.id = id;
		this.password = password;
		this.carNumber = carNumber;
		this.tel = tel;
		this.name = name;
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}

	public MEDriver() {
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public MEDriver getDriverInfo(EditText etpwd,EditText etcarnumber,EditText ettel,EditText etname){
		MEDriver driver=null;
		String password=mtConfigure.getEditText(etpwd);
		String carNumber=mtConfigure.getEditText(etcarnumber); 
		String tel=mtConfigure.getEditText(ettel);
		String name=mtConfigure.getEditText(etname);
		if(password!=null&&carNumber!=null&&tel!=null&&name!=null){
			String id="D"+tel;
			driver=new MEDriver(id, password, carNumber, tel, 0, 0, name);			
		}
		return driver;
	}
}

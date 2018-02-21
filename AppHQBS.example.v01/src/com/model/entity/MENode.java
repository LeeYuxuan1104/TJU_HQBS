package com.model.entity;

import com.model.tool.system.MTConfigure;

import android.widget.EditText;

public class MENode {
	private String id;
	private String password;
	private double lng;
	private double lat;
	private String name;
	private String tel;
	private String address;
	private MTConfigure mtConfigure;
	public MENode(String id, String password, double lng, double lat,
			String name, String tel, String address) {
	
		this.id = id;
		this.password = password;
		this.lng = lng;
		this.lat = lat;
		this.name = name;
		this.tel = tel;
		this.address = address;
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}
	public MENode() {
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
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public MENode getNodeInfo(EditText etpwd,EditText etname,EditText ettel,EditText etaddress,double lng,double lat){
		//	站点对象;
		MENode node		=null;
		String password	=mtConfigure.getEditText(etpwd);
		String name		=mtConfigure.getEditText(etname);
		String tel		=mtConfigure.getEditText(ettel);
		String address	=mtConfigure.getEditText(etaddress);
		
		if(password!=null&&name!=null&&tel!=null&&address!=null){
			String 	id	="N"+tel;
			node		=new MENode(id, password, lng, lat, name, tel, address);
		}
		return node;
	}
}

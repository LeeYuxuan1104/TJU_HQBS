package com.model.entity;

import android.widget.EditText;
import android.widget.TextView;

public class Delivery extends Information{
	private String id;
	private String owner;
	private String driver;
	private String model;
	private String price;
	private String count;
	private String lng;
	private String lat;
	private String deadline;
	private String information;
	private String goal;
	//	构造函数-非参数;
	public Delivery() {
		super();
	}
	//	构造函数-含参数;
	public Delivery(String owner, String driver, String model, String price,
			String count, String lng, String lat, String deadline,String goal) {
		super();
		this.owner = owner;
		this.driver = driver;
		this.model = model;
		this.price = price;
		this.count = count;
		this.lng = lng;
		this.lat = lat;
		this.deadline = deadline;
		this.goal=goal;
	}
	//	构造函数-含参数;
	public Delivery(String owner, String driver, String model, String price,
			String count, String lng, String lat, String deadline,String information,String goal) {
		super();
		this.owner = owner;
		this.driver = driver;
		this.model = model;
		this.price = price;
		this.count = count;
		this.lng = lng;
		this.lat = lat;
		this.deadline = deadline;
		this.information=information;
		this.goal=goal;
	}
	//	get与set方法;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
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
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public Delivery getinfo(String owner,String driver,String model,EditText etprice,EditText etcount,String lng,String lat,EditText etdeadline,String information,TextView etgoal){
		Delivery delivery=null;
		String price=mtConfigure.getEditText(etprice);
		String count=mtConfigure.getEditText(etcount);
		String deadline=mtConfigure.getEditText(etdeadline);
		String goal=mtConfigure.getTextView(etgoal);
		if(owner!=null&&driver!=null&&deadline!=null&&information!=null&&goal!=null){
			delivery=new Delivery(owner, driver, model, price, count, lng, lat, deadline, information, goal);
		}
		return delivery;
	}
}

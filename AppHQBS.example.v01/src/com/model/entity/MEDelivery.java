package com.model.entity;

import com.model.tool.system.MTConfigure;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MEDelivery {
	private int    id;
	private String ownner;
	private String driver;
	private String model;
	private double price;
	private int    count;
	private double lng;
	private double lat;
	private String deadline;
	private String goal;
	private MTConfigure mtConfigure;
	//	订单;
	public MEDelivery() {
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}
	public MEDelivery(int id, String ownner, String driver, String model,
			double price, int count, double lng, double lat, String deadline,
			String goal) {
		super();
		this.id = id;
		this.ownner = ownner;
		this.driver = driver;
		this.model = model;
		this.price = price;
		this.count = count;
		this.lng = lng;
		this.lat = lat;
		this.deadline = deadline;
		this.goal = goal;
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}
	
	public MEDelivery(String ownner, String driver, String model, double price,
			int count, double lng, double lat, String deadline, String goal) {
		super();
		this.ownner = ownner;
		this.driver = driver;
		this.model = model;
		this.price = price;
		this.count = count;
		this.lng = lng;
		this.lat = lat;
		this.deadline = deadline;
		this.goal = goal;
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOwnner() {
		return ownner;
	}
	public void setOwner(String ownner) {
		this.ownner = ownner;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	//	获得相关对象内容;
	public MEDelivery getDeliyerInfo(String ownner, String driver, String model,
			TextView etprice, EditText etcount, double lng, double lat,
			EditText etdeadline, EditText etgoal){
		//	站点对象;
		MEDelivery	delivery=null;		
		String count=mtConfigure.getEditText(etcount);
		String deadline=mtConfigure.getEditText(etdeadline);
		String goal=mtConfigure.getEditText(etgoal);
		String price=etprice.getText().toString();
		if(count!=null&&deadline!=null&&goal!=null){
			price=price.substring(0, price.indexOf("元"));
			Log.i("MyLog", "price="+price);
			delivery=new MEDelivery(ownner, driver, model, Double.parseDouble(price), Integer.parseInt(count), lng, lat, deadline, goal);
		}
		return delivery;
	}
}

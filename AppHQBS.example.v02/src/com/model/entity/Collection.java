package com.model.entity;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Collection extends Information{
	private String id;
	private String owner;
	private String driver;
	private String photo;
	private String deadline;
	private String message;
	private String lng;
	private String lat;
	private String status;
	private String price;
	private String count;
	private String goal;
	private String model;
	//	构造函数-不含参数;
	public Collection() {
		super();
	}
	//	构造函数-含有参数;
	public Collection(String owner, String driver, String photo,
			String deadline, String message, String lng, String lat,
			String status, String price, String count, String goal, String model) {
		super();
		this.owner = owner;
		this.driver = driver;
		this.photo = photo;
		this.deadline = deadline;
		this.message = message;
		this.lng = lng;
		this.lat = lat;
		this.status = status;
		this.price = price;
		this.count = count;
		this.goal = goal;
		this.model = model;
	}
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	/*进行获取对象的操作*/
	public Collection getInfo(String owner,String driver,String photo,EditText etdeadline,EditText etmessage,String lng,String lat,String status,EditText etprice,EditText etcount,TextView etgoal,String model){
		Collection collection=null;
		String deadline =mtConfigure.getEditText(etdeadline);
		String message	=mtConfigure.getEditText(etmessage);
		String price	=mtConfigure.getEditText(etprice);
		String count	=mtConfigure.getEditText(etcount);
		String goal		=mtConfigure.getTextView(etgoal);
		if(owner!=null&&driver!=null&&deadline!=null&&price!=null&&count!=null&&goal!=null&&model!=null){
			Log.i("MyLog", owner+"|"+ driver+"|"+ photo+"|"+ deadline+"|"+ message+"|"+ lng+"|"+ lat+"|"+ status+"|"+  price+"|"+ count+"|"+goal+"|"+  model);
			collection=new Collection(owner, driver, photo, deadline, message, lng, lat, status, price, count, goal, model);
		}
		return collection;
	}
}

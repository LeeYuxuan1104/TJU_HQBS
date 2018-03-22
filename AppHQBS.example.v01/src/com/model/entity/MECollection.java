package com.model.entity;

import android.widget.EditText;
import android.widget.TextView;

import com.model.tool.system.MTConfigure;

public class MECollection {
	private int id;
	private String ownner;
	private String driver;
	private String name;
	private String model;
	private String price;
	private String count;
	private String photo;
	private String deadline;
	private String message;
	private String lng;
	private String lat;
	private String goal;
	private String state;
	private MTConfigure mtConfigure;
	private MEPhoto mePhoto;
	private String did;
	
	public MECollection() {
		super();
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}
	public MECollection(int id, String ownner, String driver, String photo,
			String deadline, String message, String lng, String lat,
			String state) {
		super();
		this.id = id;
		this.ownner = ownner;
		this.driver = driver;
		this.photo = photo;
		this.deadline = deadline;
		this.message = message;
		this.lng = lng;
		this.lat = lat;
		this.state = state;
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}
	public MECollection(String ownner, String driver, String name,String model,String price,String count,String photo,
			String deadline, String message, String lng, String lat,String goal,
			String state,MEPhoto mePhoto,String did) {
		super();
		this.ownner = ownner;
		this.driver = driver;
		this.name	= name;
		this.model	= model;
		this.price	= price;
		this.count	= count;		
		this.photo 	= photo;
		this.deadline = deadline;
		this.message  = message;
		this.lng 	= lng;
		this.lat 	= lat;
		this.goal	= goal;
		this.state 	= state;
		this.mePhoto= mePhoto;
		this.did	= did;
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
	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public MEPhoto getMePhoto() {
		return mePhoto;
	}
	public void setMePhoto(MEPhoto mePhoto) {
		this.mePhoto = mePhoto;
	}
	
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public MECollection getCollectionInfo(TextView tvownner,String driver,String name,String model,String price,String count,String photo,TextView tvdeadline,EditText etmessage,String lng,String lat,String goal,String state,MEPhoto mePhoto,String did){
		String ownner	=	tvownner.getText().toString();
		String deadline	=	tvdeadline.getText().toString();
		String message	=	etmessage.getText().toString();
		return new MECollection(ownner, driver,name,model,price,count, photo, deadline, message, lng, lat,goal, state,mePhoto,did);
	}
}

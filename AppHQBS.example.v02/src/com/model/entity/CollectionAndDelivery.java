package com.model.entity;

import android.util.Log;

import com.model.tool.system.MTConfigure;

public class CollectionAndDelivery extends Information{
	private String id;
	private String message;
	private String photo;
	private String status;
	private MTConfigure mtConfigure;
	private Photo  mphoto;
	
	public CollectionAndDelivery() {
		super();
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}
	public CollectionAndDelivery(String id,String message, String photo, 
			String status) {
		super();
		this.id = id;
		this.message = message;
		this.photo = photo;
		this.status = status;
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}
	
	public CollectionAndDelivery(String id, String message, String photo,
			String status,Photo mphoto) {
		super();
		this.id = id;
		this.message = message;
		this.photo = photo;
		this.status = status;
		this.mphoto = mphoto;
		if(mtConfigure==null){
			mtConfigure= new MTConfigure();
		}
	}	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Photo getMphoto() {
		return mphoto;
	}
	public void setMphoto(Photo mphoto) {
		this.mphoto = mphoto;
	}
	//	进行数据的获取;
	public CollectionAndDelivery getInfo(String id,String message,String photo,String status,Photo mphoto){
		CollectionAndDelivery collectionAndDelivery=null;
		Log.i("MyLog", id+"|"+ message+"|"+photo+"|"+status);
		if(id!=null&&photo!=null&&status!=null){
			collectionAndDelivery=new CollectionAndDelivery(id, message, photo, status,mphoto);
		}
		return collectionAndDelivery;
	}
}

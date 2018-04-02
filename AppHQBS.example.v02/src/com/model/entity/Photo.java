package com.model.entity;

import java.util.ArrayList;
import java.util.Iterator;

import com.model.tool.system.MTConfigure;

public class Photo extends MTConfigure{
	//	名称;
	private String name;
	//	路径;
	private String path;
	//	标记;
	private boolean flag;
	
	
	//	照片列表容器;
	private ArrayList<Photo> photolist;
	//	获得图片件数;
	private int    size;
	
	//	构造函数-无参数;
	public Photo() {
		photolist=new ArrayList<Photo>();
	}
	//	构造函数-含参数;
	public Photo(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}
	public Photo(String name, String path,boolean flag) {
		super();
		this.name = name;
		this.path = path;
		this.flag = flag;
	}
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	//	获得所有照片列表;
	public void getPhotoList(String name){
		String path=null;
		Photo photo=new Photo(name, path);
		photolist.add(photo);
	}
	//	获得照片的列表内容;
	public void getPhotolist(String name,String path){
		if(!name.equals("null")){			
			Photo photo=new Photo(name, path);
			photolist.add(photo);
		}
	}
	//	获得所有照片列表;
	public void getPhotolist(String name,String path,boolean flag){
//		if(name!=null&&!name.equals("null")){			
			Photo photo=new Photo(name, path,flag);
			photolist.add(photo);
//		}
	}
	
	//	移除列表中照片内容;
	public void removePhotolist(int position){
		photolist.remove(position);
	}
	//	获得照片列表信息;
	public ArrayList<Photo> getPhotolist() {
		return photolist;
	}
	//	获得容器照片件数;
	public String getListsize(){
		size=photolist.size();
		return "  "+String.valueOf(size)+"  ";
	}
	//	获得照片的张数;
	public int getSize() {
		size=photolist.size();
		return size;
	}
	//	获得照片的名称;
	@SuppressWarnings("rawtypes")
	public String getImageNames(){
		String   names	 = null;
		//	当前照片的列表;
		Iterator iterator= photolist.iterator();
		int 	 count	 = 0;
		if(size>0){
			names	  ="";
			while (iterator.hasNext()) {
				Photo photo= (Photo) iterator.next();
				String	name = photo.getName();
				
				if(count<size-1){
					names+=name+"_";
				}else if(count==size-1){
					names+=name;
					break;		
				}
				count++;
			}
		}
		return names;
	}
	
}

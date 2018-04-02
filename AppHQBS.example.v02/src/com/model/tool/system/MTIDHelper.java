package com.model.tool.system;

import com.model.entity.User;

import android.content.Context;

//	身份验证保存模块;
public class MTIDHelper extends MTSharedpreferenceHelper{
	//	用户的id编号;
	private String useid;
	//	用户的pwd密码;
	private String usepwd;
	//	用户的name名称;
	private String usename;
	//	用户的kind类型;
	private String userole;
	//	用户的身份判断标签;
	private boolean idExist;
	private User	user;	
	
	public MTIDHelper(Context context, String fName, int nMode) {
		super(context, fName, nMode);
		idExist=checkIdentity();
		//	进行数据的重置;
		if(idExist){
			user=new User(useid, usename, usepwd, null, null, null, null, userole);
		}
	}
	//	获得用户账户;
	public String getUseid() {
		return useid;
	}
	//	设置用户账户;
	public void setUseid(String useid) {
		this.useid = useid;
	}
	//	获得用户密码;
	public String getUsepwd() {
		return usepwd;
	}
	//	设置用户密码;
	public void setUsepwd(String usepwd) {
		this.usepwd = usepwd;
	}
	
	public String getUserole() {
		return userole;
	}
	public void setUserole(String userole) {
		this.userole = userole;
	}
	//	获得用户的名称;
	public String getUsename() {
		return usename;
	}
	//	设置用户的名称;
	public void setUsename(String usename) {
		this.usename = usename;
	}
	//	进行相应的存在信号的符号;
	public boolean isIdExist() {
		return idExist;
	}
	//	进行用户信息的的设置;
	
	//	检测身份的方式方法;
	private boolean checkIdentity() {
		//	进行相应标志的判断;
		boolean flag=false;
		//	使用的用户名称;
		useid =getValue("id");
		//	用户密码;
		usepwd=getValue("password");
		if((useid instanceof String)&&(usepwd instanceof String)){
			if(useid!=null&&usepwd!=null){
				usename=getValue("name");
				userole=getValue("role");
				flag=true;
			}
		}
		return flag;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	//	人工设置身份方式;
	public void setIdentity(){
		putValue("id", this.user.getId());
		putValue("password", this.user.getPassword());
		putValue("name", this.user.getNickname());
		putValue("role", this.user.getType());
	}
}

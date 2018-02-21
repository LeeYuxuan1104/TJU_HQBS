package com.model.entity;

import com.model.tool.system.MTConfigure;

import android.widget.EditText;

public class MEAccount {
	private String aid;
	private String name;
	private String pwd;
	private String kind;
	private MTConfigure mtConfigure;
	//	构造函数-没有参数;
	public MEAccount() {
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}	
	}
	
	//	构造函数-具有类别的;
	public MEAccount(String aid, String name, String pwd, String kind) {
		this.aid = aid;
		this.name = name;
		this.pwd = pwd;
		this.kind = kind;
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}
	//	获得id编号;
	public String getAid() {
		return aid;
	}
	//	设置id编号;
	public void setAid(String aid) {
		this.aid = aid;
	}
	//	获得name姓名;
	public String getName() {
		return name;
	}
	//	设置name姓名
	public void setName(String name) {
		this.name = name;
	}
	//	获得密码的内容;
	public String getPwd() {
		return pwd;
	}
	//	设置密码;
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	//	获得账户类型;
	public String getKind() {
		return kind;
	}
	//	设置账户类型;
	public void setKind(String kind) {
		this.kind = kind;
	}
	//	进行密码解析;
	public MEAccount getAccountInfo(EditText et1,EditText et2){
		MEAccount meAccount=null;
		String id=mtConfigure.getEditText(et1);
		String pwd=mtConfigure.getEditText(et2);

		if(id!=null&&pwd!=null){
			meAccount=new MEAccount(id, null, pwd, null);
		}
		
		return meAccount;
	}
	
	
}

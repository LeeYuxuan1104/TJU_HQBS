package com.model.entity;

public class MEPerson {
	//	账户编号;
	private String pid;
	//	账户密码;
	private String ppwd;
	//	账户名称;
	private String pname;
	//	账户类别;
	private String pkind;
	//	构造函数-含有参数;
	public MEPerson() {
		super();
	}
	//	构造函数-不含参数
	public MEPerson(String pid, String ppwd) {
		super();
		this.pid = pid;
		this.ppwd = ppwd;
	}
	//	构造函数-3个参数;
	public MEPerson(String pid, String ppwd,String pname) {
		super();
		this.pid = pid;
		this.ppwd = ppwd;
		this.pname= pname;
	}
	//	构造函数-4个参数;	
	public MEPerson(String pid, String ppwd,String pname,String pkind) {
		super();
		this.pid  = pid;
		this.ppwd = ppwd;
		this.pname= pname;
		this.pkind= pkind;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPpwd() {
		return ppwd;
	}
	public void setPpwd(String ppwd) {
		this.ppwd = ppwd;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPkind() {
		return pkind;
	}
	public void setPkind(String pkind) {
		this.pkind = pkind;
	}
	
}

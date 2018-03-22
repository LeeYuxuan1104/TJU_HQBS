package com.model.entity;
import android.widget.EditText;

public class MEBargaininfo {
	private long id;
	private String node_id;
	private String driver_id;
	private String message;
	private int target;
	private String timename;
	public MEBargaininfo(long id, String node_id, String driver_id,
			String message, int target, String timename) {
		super();
		this.id = id;
		this.node_id = node_id;
		this.driver_id = driver_id;
		this.message = message;
		this.target = target;
		this.timename = timename;
	}
	public MEBargaininfo() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNode_id() {
		return node_id;
	}
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}
	public String getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public String getTimename() {
		return timename;
	}
	public void setTimename(String timename) {
		this.timename = timename;
	}
	/*进行相应的数据操作*/
	public MEBargaininfo getBargaininfo(long id,String node_id,String driver_id,EditText etmessage,int target,String timename){
		String message	=	etmessage.getText().toString();
		return new MEBargaininfo(0, node_id, driver_id, message, target, null);
	}
}

package cn.model.entity;

import java.sql.Date;
import java.util.ArrayList;

import cn.model.tool.MTDataBaseTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Delivery {
	private int    id;
	private String ownner;
	private String driver;
	private String model;
	private double price;
	private int    count;
	private double lng;
	private double lat;
	private Date deadline;
	private String goal;
	private MTDataBaseTool mtDBTool;
	public Delivery(int id, String ownner, String driver, String model,
			double price, int count, double lng, double lat, Date deadline,
			String goal) {
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
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
	}
	public Delivery(String ownner, String driver, String model, double price,
			int count, double lng, double lat, Date deadline, String goal) {
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
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
	}
	public Delivery() {
		super();
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
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
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	//	查询司机名称;
	//	查询电池名称;
	public String getDriverList(String ownner){
		String 				sql	 = "select a.id as driver,a.name as name from driver a,node b,node_driver c where a.id=c.driver_id and b.id=c.node_id and b.id='"+ownner+"'";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
//		System.out.println(list.toString());
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("driver", items[0]);
						obj.put("name", items[1]);
					} catch (Exception e) {
						break;
					}
					array.add(obj);
				}
				return array.toString();
			}
		}
		return null;
	}
	//	数据更新内容;
	public String updateDelivery(String sql){
		String  result=null;
		int 	nflag =0;
		nflag		  =mtDBTool.doDBUpdate(sql);
		if(nflag!=0){
			result	  ="success";
		}
		return result;
	} 
}

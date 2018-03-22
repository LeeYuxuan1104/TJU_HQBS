package cn.model.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@SuppressWarnings("rawtypes")
public class Delivery extends Basedata{
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
	
	public Delivery(int id, String ownner, String driver, String model,
			double price, int count, double lng, double lat, Date deadline,
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
	}
	public Delivery() {
		super();
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
		Iterator 		 iterator= list.iterator();
		while (iterator.hasNext()) {
			String[] 	 items	 = (String[]) iterator.next();
			JSONObject 	 obj 	 = new JSONObject();
			obj.put("driver", items[0]);
			obj.put("name", items[1]);
			array.add(obj);			
		}
		return array.toString();
	}
	//	查询所有数据的SQL语句;
	@Override
	public String queryAllSQL() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//	根据条件进行查询的语句;
	@Override
	public String queryBySQL(String sql) {
		String result=null;
		ArrayList<String[]> list	= mtDBTool.query(sql);
		Iterator 			iterator= list.iterator();
		JSONArray   		array	= new JSONArray();
		if(list!=null){			
			while (iterator.hasNext()) {
				String[] items=(String[]) iterator.next();
				//	进行相应对象的赋值;
				JSONObject obj = new JSONObject();
				obj.put("ownner", items[0]);
				obj.put("model", items[1]);
				obj.put("price", items[2]);
				obj.put("count", items[3]);
				obj.put("lng", items[4]);
				obj.put("lat", items[5]);
				obj.put("deadline", items[6]);
				obj.put("goal", items[7]);
				obj.put("id", items[8]);
				array.add(obj);
			}
			result=array.toString();
		}
		return result;
	}
	@Override
	public String queryItemBySQL(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}

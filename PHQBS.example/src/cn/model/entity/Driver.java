package cn.model.entity;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("rawtypes")
public class Driver extends Basedata{
	private String id;
	private String password;
	private String carNumber;
	private String tel;
	private double lng;
	private double lat;
	private String name;
	//	构造函数-含参数;
	public Driver(String id, String password, String carNumber, String tel,
			double lng, double lat, String name) {
		super();
		this.id = id;
		this.password = password;
		this.carNumber = carNumber;
		this.tel = tel;
		this.lng = lng;
		this.lat = lat;
		this.name = name;

	}
	//	构造函数-否参数;
	public Driver() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//	查询所有的司机;
	//	查询回收员;
	public String queryAllSQL(){
		return null;
	}
	
	@Override
	public String queryBySQL(String sql) {
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		Iterator 		 iterator= list.iterator();
		while (iterator.hasNext()) {
			String[] 	items = (String[]) iterator.next();
			JSONObject  obj   = new JSONObject();
			try {
				obj.put("id", items[0]);
				obj.put("carnumber", items[1]);
				obj.put("name", items[2]);
			} catch (Exception e) {
				break;
			}
			array.add(obj);
		}
		return array.toString();
	}
	
	@Override
	public String queryItemBySQL(String sql) {
		ArrayList<String[]> list = mtDBTool.query(sql); 
		Iterator 		 iterator= list.iterator();
		JSONArray   	 array	 = new JSONArray();
		while (iterator.hasNext()) {
			String[] 	 items	 = (String[]) iterator.next();
			JSONObject 	 obj     = new JSONObject();
			try {
				obj.put("id", items[0]);
				obj.put("password", items[1]);
				obj.put("carNumber", items[2]);
				obj.put("tel", items[3]);
				obj.put("lng", items[4]);
				obj.put("lat", items[5]);
				obj.put("name", items[6]);
			} catch (Exception e) {
				return null;
			}
			array.add(obj);
		}
		return array.toString();
	}
}

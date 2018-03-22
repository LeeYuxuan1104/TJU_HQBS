package cn.model.entity;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("rawtypes")
public class Good extends Basedata{
	private String model;
	private String brand;
	private String information;
	private double price;
	private int count;
	
	public Good(String model, String brand, String information, double price,
			int count) {
		super();
		this.model = model;
		this.brand = brand;
		this.information = information;
		this.price = price;
		this.count = count;
	}
	public Good() {
		super();
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
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
	//	查询电池名称;
	public String queryAllSQL(){
		String 				sql	 = "select model,brand,price,count from good";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		Iterator		 iterator= list.iterator();
		while (iterator.hasNext()) {
			String[]	 items	 = (String[]) iterator.next();			
			JSONObject obj = new JSONObject();
			try {
				obj.put("model", items[0]);
				obj.put("brand", items[1]);
				obj.put("price", items[2]);
				obj.put("count", items[3]);
			} catch (Exception e) {
				break;
			}
			array.add(obj);
		}
		return array.toString();
	}
	@Override
	public String queryBySQL(String param) {
		return null;
	}
	@Override
	public String queryItemBySQL(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}

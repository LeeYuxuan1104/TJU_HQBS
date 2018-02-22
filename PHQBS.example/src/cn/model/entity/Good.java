package cn.model.entity;

import java.util.ArrayList;

import cn.model.tool.MTDataBaseTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Good {
	private String model;
	private String brand;
	private String information;
	private double price;
	private int count;
	private MTDataBaseTool mtDBTool;
	public Good(String model, String brand, String information, double price,
			int count) {
		this.model = model;
		this.brand = brand;
		this.information = information;
		this.price = price;
		this.count = count;
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();			
		}
	}
	public Good() {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();			
		}
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
	public String getGoodList(){
		String 				sql	 = "select model,brand,price,count from good";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
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
		}
		return null;
	}
	//	进行数据加载;
	public String updateGood(String sql){
		String  result=null;
		int 	nflag =0;
		nflag		  =mtDBTool.doDBUpdate(sql);
		if(nflag!=0){
			result	  ="success";
		}
		return result;
	}
}

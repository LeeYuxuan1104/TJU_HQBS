package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.model.tool.MTDataBaseTool;

public class Driver {
	private String id;
	private String password;
	private String carNumber;
	private String tel;
	private double lng;
	private double lat;
	private String name;
	//	进行数据的参数;
	private MTDataBaseTool mtDBTool;
	//	构造函数-含参数;
	public Driver(String id, String password, String carNumber, String tel,
			double lng, double lat, String name) {
		this.id = id;
		this.password = password;
		this.carNumber = carNumber;
		this.tel = tel;
		this.lng = lng;
		this.lat = lat;
		this.name = name;
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	构造函数-否参数;
	public Driver() {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
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
	//	进行数据的加载;
	public String checkDriver(String id,String pwd){
		String 				sql	 = "select * from driver where id='"+id+"' and password='"+pwd+"'";
		ArrayList<String[]> list = mtDBTool.query(sql); 
		if(list!=null){			
			int 			nsize= list.size();
			if(nsize>0){
				String[] 	item = list.get(0);
				JSONArray   array= new JSONArray();
				JSONObject 	obj  = new JSONObject();
				try {
					obj.put("id", item[0]);
					obj.put("password", item[1]);
					obj.put("carNumber", item[2]);
					obj.put("tel", item[3]);
					obj.put("lng", item[4]);
					obj.put("lat", item[5]);
					obj.put("name", item[6]);
				} catch (Exception e) {
					return null;
				}
				array.add(obj);
				return array.toString();
			}
		}
		return null;
	}
	//	查询所有的司机;
//	查询回收员;
	public String queryDriverAll(){
		String 				sql	 = "select id,carNumber,name  from driver ";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
//		System.out.println(list.toString());
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
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
		}
		return null;
	}
	
	//	进行数据加载;
	public String updateDriver(String sql){
		String  result=null;
		int 	nflag =0;
		nflag		  =mtDBTool.doDBUpdate(sql);
		if(nflag!=0){
			result	  ="success";
		}
		return result;
	} 
}

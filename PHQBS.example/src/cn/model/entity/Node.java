package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.model.tool.MTDataBaseTool;

public class Node {
	private String id;
	private String password;
	private double lng;
	private double lat;
	private String name;
	private String tel;
	private String address;
	//	数据库内容;
	private MTDataBaseTool mtDBTool;
	
	//	构造函数;
	public Node(String id, String password, double lng, double lat,
			String name, String tel,String address) {
		this.id = id;
		this.password = password;
		this.lng = lng;
		this.lat = lat;
		this.name = name;
		this.tel = tel;
		this.address=address;
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
	}
	public Node() {
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
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	//	进行数据的加载;
	public String checkNode(String id,String pwd){
		String 				sql	 = "select * from node where id='"+id+"' and password='"+pwd+"'";
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
					obj.put("lng", item[2]);
					obj.put("lat", item[3]);
					obj.put("name", item[4]);
					obj.put("tel", item[5]);
				} catch (Exception e) {
					return "fail";
				}
				array.add(obj);
				return array.toString();
			}
		}
		return "fail";
	}
	//	查询回收单;
	public String queryDeliveryAll(String owner){
		String 				sql	 = "select * from delivery where ownner='"+owner+"'";
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
						obj.put("ownner", items[1]);
						obj.put("driver", items[2]);
						obj.put("model", items[3]);
						obj.put("price", items[4]);
						obj.put("count", items[5]);
						obj.put("lng", items[6]);
						obj.put("lat", items[7]);
						obj.put("deadline", items[8]);
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
	//	条件查询
	public String queryDeliveryByCondition(String param){
		String 				sql	 = "select * from delivery "+param;
//		System.out.println("sql="+sql);
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
						obj.put("ownner", items[1]);
						obj.put("driver", items[2]);
						obj.put("model", items[3]);
						obj.put("price", items[4]);
						obj.put("count", items[5]);
						obj.put("lng", items[6]);
						obj.put("lat", items[7]);
						obj.put("deadline", items[8]);
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
	//	查询回收员;
	public String queryDriverAll(String ownner){
		String 				sql	 = "select a.id as driver,a.carNumber as car,a.tel as tel,a.name as person,b.id as node from driver a,node b,node_driver c where a.id=c.driver_id and b.id=c.node_id and b.id='"+ownner+"'";
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
						obj.put("car", items[1]);
						obj.put("tel", items[2]);
						obj.put("person", items[3]);
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
	//	查询回收员;
	public String queryDriverByCondition(String param){
		String 				sql	 = "select a.id as driver,a.carNumber as car,a.tel as tel,b.id as node from driver a,node b,node_driver c "+param;
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
						obj.put("car", items[1]);
						obj.put("tel", items[2]);
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
	//	查询所有的新电池;
	public String queryGoodsAll(){
		String 				sql	 = "select * from good";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
//		System.out.println(list.toString());
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("model", items[0]);
						obj.put("brand", items[1]);
						obj.put("information", items[2]);
						obj.put("price", items[3]);
						obj.put("count", items[4]);
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
	//	查询所有的新电池;
	public String queryGoodsByCondition(String param){
		String 				sql	 = "select * from good "+param;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		//	System.out.println(list.toString());
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("model", items[0]);
						obj.put("brand", items[1]);
						obj.put("information", items[2]);
						obj.put("price", items[3]);
						obj.put("count", items[4]);
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
	public String updateNode(String sql){
		String  result=null;
		int 	nflag =0;
		nflag		  =mtDBTool.doDBUpdate(sql);
		if(nflag!=0){
			result	  ="success";
		}
		return result;
	} 
}

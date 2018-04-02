package cn.model.entity;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

//	新电池订单的类;
public class Delivery extends Basedata{
	public Delivery() {
		super();
	}
	//	根据SQL语句进行查询;
	@Override
	public String queryBySQL(String[] params, String sql) {
		return mtDBTool.query(params, sql);
	}
	//	查询所有的回收单内容;
	public String getAlldeliverys(HttpServletRequest req){
		//	所述的id编号;
		String 	 owner	= req.getParameter("owner");
		//	字段名称;
		String[] params = {"id","owner","driver","lng","lat","deadline","information","goal","name"};
		//	SQL语句;
		String   sql	= "select a.id,a.owner,a.driver,a.lng,a.lat,a.deadline,a.information,a.goal,b.nickname from delivery a ,user b where a.driver=b.id and a.owner='"+owner+"'";
		System.out.println("s="+sql);
		return queryBySQL(params, sql);
	}
	//	查询条件的回收单内容;
	public String getDeliveryByCondition(HttpServletRequest req){
		//	所述的id编号;
		String 	 id		= req.getParameter("id");
		String 	 owner	= req.getParameter("owner");
		//	字段名称;
		String[] params = {"id","owner","driver","lng","lat","deadline","information","goal","name"};
		//	SQL语句;
		String   sql	= "select a.id,a.owner,a.driver,a.lng,a.lat,a.deadline,a.information,a.goal,b.nickname from delivery a ,user b where a.driver=b.id and a.owner='"+owner+"' and a.id='"+id+"'";
		return queryBySQL(params, sql);
	}
	@Override
	public String addInfo(HttpServletRequest req) {
		String id		= mtConfig.getID("N", "yyyyMMddhhmmss");
		String owner	= req.getParameter("owner");
		String driver	= req.getParameter("driver");
		String model	= req.getParameter("model");
//		String price	= req.getParameter("price");
//		String count	= req.getParameter("count");
		String lng		= req.getParameter("lng");
		String lat		= req.getParameter("lat");
		String deadline = req.getParameter("deadline");
		String information=null;
		String goal		= null;		
		try {
			information	 	= new String(req.getParameter("information").getBytes("ISO8859_1"),"utf-8");
			goal			= new String(req.getParameter("goal").getBytes("ISO8859_1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String sql		= 
		"insert into delivery (id,owner,driver,model,lng,lat,deadline,information,goal,status) values ('"+id+"'," +
		"'"+owner+"','"+driver+"','"+model+"',"+lng+","+lat+",'"+deadline+"','"+information+"','"+goal+"',1)";
		return updateSQL(sql);
	}
	@Override
	public String deleteInfo(HttpServletRequest req) {
		String id=req.getParameter("id");
		String sql="delete from delivery where id='"+id+"'";
		updateSQL(sql);
		return getAlldeliverys(req);
	}
}

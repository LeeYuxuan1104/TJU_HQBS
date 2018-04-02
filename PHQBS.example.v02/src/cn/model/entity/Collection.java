package cn.model.entity;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

//	回收单的实体类;
public class Collection extends Basedata{

	public Collection() {
		super();
	}
	//	根据sql语句进行查询;
	@Override
	public String queryBySQL(String[] params, String sql) {
		return mtDBTool.query(params, sql);
	}
	//	查询所有的回收单内容;
	public String getAllcollections(HttpServletRequest req){
		//	所述的id编号;
		String 	 owner	= req.getParameter("owner");
		//	字段名称;
		String[] params = {"id","owner","driver","deadline","lng","lat","status","price","count","goal","model","name"};
		//	SQL语句;
		String   sql	= "select a.id,a.owner,a.driver,a.deadline,a.lng,a.lat,a.status,a.price,a.count,a.goal,a.model,b.nickname from collection a ,user b where a.driver=b.id and a.owner='"+owner+"'";
		return queryBySQL(params, sql);
	}
	//	查询条件的回收单内容;
	public String getCollectionsByCondition(HttpServletRequest req){
		//		所述的id编号;
		String 	 id		= req.getParameter("id");
		String 	 owner	= req.getParameter("owner");
		//	字段名称;
		String[] params = {"id","owner","driver","deadline","lng","lat","status","price","count","goal","model","name"};
		//	SQL语句;
		String   sql	= "select a.id,a.owner,a.driver,a.deadline,a.lng,a.lat,a.status,a.price,a.count,a.goal,a.model,b.nickname from collection a ,user b where a.driver=b.id and a.owner='"+owner+"' and a.id='"+id+"'";
		return queryBySQL(params, sql);
	}
	//	回收单的新增;
	public String addInfo(HttpServletRequest req){
		String owner=req.getParameter("owner");
		String driver=req.getParameter("driver");
		String photo=req.getParameter("photo");
		String deadline=null;
		String message=null;
		String lng=req.getParameter("lng");
		String lat=req.getParameter("lat");
		String status=req.getParameter("status");
		String price=req.getParameter("price");
		String count=req.getParameter("count");
		String goal=null;
		String model=null;
		String id=mtConfig.getID("HS", "yyyyMMddhhmmss");
		try {
			deadline	=new String(req.getParameter("deadline").getBytes("ISO8859_1"),"utf-8");
			message		=new String(req.getParameter("message").getBytes("ISO8859_1"),"utf-8");
			goal		=new String(req.getParameter("goal").getBytes("ISO8859_1"),"utf-8");
			model		=new String(req.getParameter("model").getBytes("ISO8859_1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("回收单新增出现异常..");
			return null;
		}
		String sql		=
		"insert into collection (id,owner,driver,photo,deadline,message,lng,lat,status,price,count,goal,model) values (" +
		"'"+id+"','"+owner+"','"+driver+"','"+photo+"','"+deadline+"','"+message+"',"+lng+","+lat+","+status+","+price+","+count+",'"+goal+"','"+model+"')";
		return updateSQL(sql);
	}
	//	删除回收单信息;
	public String deleteInfo(HttpServletRequest req){
		String id=req.getParameter("id");
		String sql="delete from collection where id='"+id+"'";
		updateSQL(sql);
		return getAllcollections(req);
	}

}

package cn.model.entity;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

public class CollectionAndDelivery extends Basedata{
	private Collection collection;
	private Delivery   delivery;
	
	public CollectionAndDelivery() {
		super();
		if(collection==null){
			collection=new Collection();
		}
		if(delivery==null){
			delivery=new Delivery();
		}
	}
	//	根据状态进行查询;
	public String getAllOrdersByStatus(HttpServletRequest req){
		String 	 owner 	=   req.getParameter("owner");
		String 	 status	=   req.getParameter("status");
		String[] params	=	{"id","driver","deadline","goal","status","name"};
		String   sql	=	"select a.id,a.driver,a.deadline,a.goal,a.status,b.nickname from collection a ,user b where a.driver=b.id and a.driver='"+owner+"' and a.status="+status;		
		JSONArray jsonArray1= queryBySQL2(params, sql);
		sql				=   "select a.id,a.driver,a.deadline,a.goal,a.status,b.nickname from delivery a ,user b where a.driver=b.id and a.driver='"+owner+"' and a.status="+status;
		JSONArray jsonArray2= queryBySQL2(params, sql);
		jsonArray1.addAll(jsonArray2);
		return jsonArray1.toString();
	}
	//	根据单号进行查询;
	public String getAllOrdersById(HttpServletRequest req){
		String 	 owner 	=   req.getParameter("owner");
		String 	 id		=   req.getParameter("id");
		String 	 status	=   req.getParameter("status");
		String[] params	=	{"id","driver","deadline","goal","status","name"};
		String   sql	=	"select a.id,a.driver,a.deadline,a.goal,a.status,b.nickname from collection a ,user b where a.driver=b.id and a.driver='"+owner+"' and a.status="+status+" and a.id like '%"+id+"%'";		
		JSONArray jsonArray1= queryBySQL2(params, sql);
		sql				=   "select a.id,a.driver,a.deadline,a.goal,a.status,b.nickname from delivery a ,user b where a.driver=b.id and a.driver='"+owner+"' and a.status="+status+" and a.id like '%"+id+"%'";
		JSONArray jsonArray2= queryBySQL2(params, sql);
		jsonArray1.addAll(jsonArray2);
		return jsonArray1.toString();
	}
	//	进行标签的改变;
	public String changeOrderStatusById(HttpServletRequest req){
		String 	id		=	req.getParameter("id");
		String 	status	=	req.getParameter("status0");
		String  sql		=	"update collection set status="+status+" where id='"+id+"'";
		updateSQL(sql);
		sql				=	"update delivery set status="+status+" where id='"+id+"'";
		updateSQL(sql);
		
		return getAllOrdersByStatus(req);
	}
	//	进行删除的操作;
	public String deleteOrderStatusById(HttpServletRequest req){
		String id	=req.getParameter("id");
		String sql	="delete from collection where id='"+id+"'";
		updateSQL(sql);
		sql			="delete from delivery where id='"+id+"'";
		updateSQL(sql);
		return getAllOrdersByStatus(req);
	}
	//	查询单条的信息;
	public String queryItemByKind(HttpServletRequest req){
		String 	 sResult="";
		String 	 sql	= "";
		String   id		= req.getParameter("id");
		if(id.contains("HS")){
			String[] params={"id","model","count","price","deadline","goal"};
			sql="select id,model,count,price,deadline,goal from collection where id='"+id+"'";
			sResult=queryBySQL(params, sql);
		}else if(id.contains("N")){
			String[] params={"id","deadline","information","goal"};
			sql="select id,deadline,information,goal from delivery where id='"+id+"'";
			sResult=queryBySQL(params, sql);
		}
//		System.out.println(sResult);
		return sResult;
	}
	//	进行相应的数据信息的修改;
	public String updateItemById(HttpServletRequest req){
		String id	 = req.getParameter("id");
		String photo = req.getParameter("photo");
		String status= req.getParameter("status");
		String sql	 = "";
		if(id.contains("HS")){
			sql="update collection set photo='"+photo+"',status="+status+" where id='"+id+"'";
			updateSQL(sql);
		}else if(id.contains("N")){
			sql="update delivery set status="+status+" where id='"+id+"'";
			updateSQL(sql);
		}
//		System.out.println("sql="+sql);
		return "success";
	}
	//	利用JSonArray承装数据列表的信息内容;
	public JSONArray queryBySQL2(String[] params, String sql){
		return mtDBTool.query2(params, sql);
	}
	
	//	查询信息内容;
	@Override
	public String queryBySQL(String[] params, String sql) {
		return mtDBTool.query(params, sql);
	}
	//	新增信息内容;
	@Override
	public String addInfo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteInfo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
}

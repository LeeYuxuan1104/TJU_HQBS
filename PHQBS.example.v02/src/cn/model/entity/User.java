package cn.model.entity;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

//	用户对象类;
public class User extends Basedata{
	public User() {
		super();
	}
	//	根据SQL语句进行查询
	@Override
	public String queryBySQL(String[] params, String sql) {
		return mtDBTool.query(params, sql);
	}
	//	登录的内容;
	public String login(HttpServletRequest req){
		String id		=req.getParameter("id");
		String password	=req.getParameter("password");
		String[] params	={"id","nickname","password","status","lng","lat","car_number","role"};
		String sql		="select a.id,a.nickname,a.password,a.status,a.lng,a.lat,a.car_number,b.name from user a,role b,user_role c where a.id='"+id+"' and a.password='"+password+"' and a.id=c.user_id and b.id=c.role_id";
		return queryBySQL(params, sql);
	}
	//	注册的内容;
	public String signup(HttpServletRequest req){
		String id		=req.getParameter("id");
		String nickname	=null;
		String password	=null;
		String status	=req.getParameter("status");
		String lng		=req.getParameter("lng");
		String lat		=req.getParameter("lat");
		String car_number	=null;
		String type		=req.getParameter("type");
		String address	=null;
		try {
			nickname	=new String(req.getParameter("nickname").getBytes("ISO8859_1"),"utf-8");
			password	=new String(req.getParameter("password").getBytes("ISO8859_1"),"utf-8");
			car_number	=new String(req.getParameter("car_number").getBytes("ISO8859_1"),"utf-8");
			address		=new String(req.getParameter("address").getBytes("ISO8859_1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("用户注册出现异常..");
			return null;
		}
		String sql		=
		"insert into user (id,nickname,password,status,lng,lat,car_number,type,address) values (" +
		"'"+id+"','"+nickname+"','"+password+"','"+status+"',"+lng+","+lat+",'"+car_number+"','"+type+"','"+address+"')";
		return updateSQL(sql);
	}
	//	获取回收员信息;
	public String getAlldrivers(HttpServletRequest req){
		String id		=req.getParameter("id");
		String[] params	={"id","name","car_number"};
		String sql		="select a.id,a.nickname,a.car_number from user a,driver_node b where a.id=b.driver_id and b.node_id='"+id+"'";
		return queryBySQL(params, sql);
	}
	//	获取回收员信息-条件查询
	public String getDriversByCondition(HttpServletRequest req){
		String id		=req.getParameter("id");
		String tel		=req.getParameter("tel");
		String[] params	={"id","name","car_number"};
		String sql		="select a.id,a.nickname,a.car_number from user a,driver_node b where a.id=b.driver_id and b.node_id='"+id+"' and b.driver_id='"+tel+"'";
		return queryBySQL(params, sql);
	}
	//	获取所有所属回收员
	public String getAllDriversByNode(HttpServletRequest req){
		String id		=req.getParameter("id");
		String[] params	={"id","name","car_number"};
		String sql		="select a.id,a.nickname,a.car_number from user a,driver_node b where a.id=b.driver_id and b.node_id='"+id+"'";
		return queryBySQL(params, sql);
	}
	//	根据名称查询回收员;
	public String getAllDriversByName(HttpServletRequest req){
		String name		=null;
		String id		=req.getParameter("id");
		try {
			name		=new String(req.getParameter("name").getBytes("ISO8859_1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] params	={"id","name","car_number"};
		String sql		=
		"select a.id,a.nickname,a.car_number from user a ,user_role c where a.id=c.user_id and c.role_id=2 and a.id not in ( select b.driver_id FROM driver_node b where b.node_id!='"+id+"') and a.nickname like '%"+name+"%' ";
		return queryBySQL(params, sql);
	}
	//	进行相应的权限修改;
	public String setDriversRelationship(HttpServletRequest req){
		String   node_id	= req.getParameter("node_id");
		String   driver_id	= req.getParameter("driver_id");
		String[] driver_ids = driver_id.split(",");
		int 	 index		= 0;
		String	 temp	 	= null;
		do {
			try {				
				temp 		 = driver_ids[index];
				String	id	 = temp.substring(0, temp.indexOf("_"));
				String	state= temp.substring(temp.indexOf("_")+1,temp.length());
				String	sql	 = "delete from driver_node where driver_id='"+id+"' and node_id='"+node_id+"'";
				updateSQL(sql);
				if(state.equals("1")){
					sql="insert into driver_node (driver_id,node_id) values ('"+id+"','"+node_id+"')";
					updateSQL(sql);
				}
				index++;		
			} catch (Exception e) {
				temp		 = null;
				break;
			}
		} while (temp!=null);
		return "success";
	}
	//	进行相应的数据修改;
	public String deleteInfo(HttpServletRequest req){
		String driver_id=req.getParameter("driver_id");
		String node_id=req.getParameter("id");
		String sql		="delete from driver_node where driver_id='"+driver_id+"' and node_id='"+node_id+"'";
		updateSQL(sql);
		return getAlldrivers(req);
	}
	@Override
	public String addInfo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
}

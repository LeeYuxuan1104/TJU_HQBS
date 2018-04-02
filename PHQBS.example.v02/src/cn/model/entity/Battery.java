package cn.model.entity;

import javax.servlet.http.HttpServletRequest;

//	电池的实体信息;
public class Battery extends Basedata{
	public Battery() {
		super();
	}
	//	根据sql语句进行;
	@Override
	public String queryBySQL(String[] params,String sql) {
		return mtDBTool.query(params, sql);
	}
	//	获得所有的电池信息;
	public String getAllBatterys(){
		//	字段名称;
		String[] params = {"model","brand","price","count"};
		//	SQL语句;
		String   sql	= "select model,brand,price,count from battery";
		return queryBySQL(params, sql);
	}
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

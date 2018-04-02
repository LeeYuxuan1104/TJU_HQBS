package cn.model.entity;

import javax.servlet.http.HttpServletRequest;

//	角色对象类;
public class Role extends Basedata{
	public Role() {
		super();
	}
	//	根据SQL语句进行查询;
	@Override
	public String queryBySQL(String[] params, String sql) {
		return mtDBTool.query(params, sql);
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

package cn.model.entity;

import javax.servlet.http.HttpServletRequest;

//	允许的操作;
public class Permission extends Basedata{
	public Permission() {
		super();
	}
	//	根据SQl语句进行查询;
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

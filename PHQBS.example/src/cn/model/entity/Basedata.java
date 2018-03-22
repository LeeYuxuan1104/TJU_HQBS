package cn.model.entity;

import cn.model.tool.MTDataBaseTool;

public abstract class Basedata {
	public MTDataBaseTool mtDBTool;
	public Basedata() {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	更新相应数据;
	public String updateSQL(String sql){
		String  result=null;
		int 	nflag =0;
		nflag		  =mtDBTool.doDBUpdate(sql);
		if(nflag!=0){
			result	  ="success";
		}
		return result;
	}
	//	查询所有数据;
	public abstract String queryAllSQL();
	//	按SQL语句查询;
	public abstract String queryBySQL(String sql);
	//	查询单条内容;
	public abstract String queryItemBySQL(String sql);
}

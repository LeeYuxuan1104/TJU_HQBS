package cn.model.entity;

import javax.servlet.http.HttpServletRequest;

import cn.model.tool.MTConfig;
import cn.model.tool.MTDataBaseTool;

public abstract class Basedata {
	public MTDataBaseTool mtDBTool;
	public MTConfig		  mtConfig;
	public Basedata() {
		mtDBTool	=	MTDataBaseTool.getInstance();
		if(mtConfig==null){
			mtConfig=	new MTConfig();
		}
	}
	//	更新相应数据;
	public String updateSQL(String sql){
		String  result=	null;
		int 	nflag =	0;
		nflag		  =	mtDBTool.doDBUpdate(sql);
		if(nflag!=0){
			result	  =	"success";
		}
		return result;
	}
	//	按SQL语句查询;
	public abstract String queryBySQL(String[] params,String sql);
	//	新增操作;
	public abstract String addInfo(HttpServletRequest req);
	//	删除操作;
	public abstract String deleteInfo(HttpServletRequest req);
}

package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.Good;

public class CCheckGoodinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//	
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null,param=null,sql="";
		String[]		params	=	null;
		Good			good	=	null;

		switch (operType) {
		//	1.查询所有客户;
		case 1:
			good	=	new Good();
			sResult	=	good.getGoodList();
			break;
		//	2.修改件数信息;
		case 2:
			param=req.getParameter("param");
			param=param.substring(0, param.lastIndexOf(";"));
			good	=	new Good();
			params=param.split(";");
			for(String item:params){
				String[] items=item.split("_");
				String id=items[0];
				String count=items[1];
				sql="update good set count='"+count+"' where model='"+id+"'";
				good.updateGood(sql);				
			}
			
			break;
		default:
			break;
		}
	
		pWriter.print(sResult)	;
		pWriter.flush();
		pWriter.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}

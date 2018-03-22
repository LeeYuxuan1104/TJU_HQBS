package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.Weixintalk;
import cn.model.tool.MTConfig;

public class CCheckWeixintalkinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//	
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null,node_id,driver_id,message,target,timename,sql="";
		long			id;
		Weixintalk		weixintalk=null;
		MTConfig		mtConfig=	null;
		
		switch (operType) {
		//	1.更新数据库表;
		case 1:
			node_id		=	req.getParameter("node_id");
			driver_id	=	req.getParameter("driver_id");
			message		=	req.getParameter("message");
			target		=	req.getParameter("target");
			weixintalk	=	new Weixintalk();
			mtConfig	=	new MTConfig();
			id			=	mtConfig.getCurrenttime();
			timename	=	mtConfig.getCurrentdate();
			//	进行数据的相应操作;
			sql			=	
			"insert into bargaininfo " +
			"(id,node_id,driver_id,message,target,timename) values " +
			"("+id+",'"+node_id+"','"+driver_id+"','"+message+"',"+target+",'"+timename+"')";
			//	进行数据更新操作的相应内容;
			weixintalk.updateSQL(sql);
			break;
		//	2.查询数据库表;
		case 2:
			node_id		=	req.getParameter("node_id");
			driver_id	=	req.getParameter("driver_id");
			sql			=	"select id,message,target,timename from bargaininfo where node_id='"+node_id+"' and driver_id='"+driver_id+"' order by id desc limit 0,1";
			System.out.println("sql="+sql);
			weixintalk	=	new Weixintalk();
			sResult		=	weixintalk.queryBySQL(sql);
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

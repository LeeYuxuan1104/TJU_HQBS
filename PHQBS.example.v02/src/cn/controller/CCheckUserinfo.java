package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.User;

public class CCheckUserinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//
		PrintWriter  pWriter = 	resp.getWriter();
		int 		 operType=	Integer.parseInt(req.getParameter("opertype"));
		String  	 sResult =   null;
		User		 user	 =	new User();
		switch (operType) {
		
		//	1.登录;
		case 1:
			sResult	=	user.login(req);
			break;
		//	2.注册;
		case 2:
			sResult =	user.signup(req);
			break;
		//	3.全信息查询;
		case 3:
			sResult =	user.getAlldrivers(req);
			break;
		//	4.条件信息查询;
		case 4:
			sResult =	user.getDriversByCondition(req);
			break;
		//	5.根据站点找回收员;
		case 5:
			sResult =	user.getAllDriversByNode(req);
			break;
		//	6.根据姓名查找回收员
		case 6:
			sResult =	user.getAllDriversByName(req);
			break;
		//	7.进行数据关系的变化;
		case 7:
			sResult =	user.setDriversRelationship(req);
			break;
		//	8.进行数据的删除;
		case 8:
			sResult =	user.deleteInfo(req);
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

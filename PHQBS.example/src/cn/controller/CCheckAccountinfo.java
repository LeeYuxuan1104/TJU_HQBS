package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.Driver;
import cn.model.entity.Node;

public class CCheckAccountinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null;
		String 			id,pwd;
		
		switch (operType) {
		//	1.登录;
		case 1:
			id=req.getParameter("accountid");
			pwd=req.getParameter("password");
			//	进行数据的校验;
			sResult=checkKindAccount(id,pwd);
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
	//	进行账户的转化;
	private String checkKindAccount(String id,String pwd){
		String result=null;
		String head=id.substring(0, 1);
		if(head.equalsIgnoreCase("d")){
			Driver driver= new Driver();
			String sql	 = "select * from driver where id='"+id+"' and password='"+pwd+"'";
			result		 = driver.queryItemBySQL(sql);
		}else if(head.equalsIgnoreCase("n")){
			Node node=new Node();
			result=node.checkNode(id, pwd);
		}
		return result;
	}
}

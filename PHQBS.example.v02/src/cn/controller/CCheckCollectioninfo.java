package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.Collection;

public class CCheckCollectioninfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null;
		//	
		Collection	  collection=	new Collection();
		switch (operType) {
		//	1.获得相应的回收单的信息;
		case 1:
			sResult=collection.getAllcollections(req);
			break;
		//	2.获得条件的回收单的内容;
		case 2:
			sResult=collection.getCollectionsByCondition(req);
			break;
		//	3.回收单的新增;
		case 3:
			sResult=collection.addInfo(req);
			break;
		//	4.回收单的删除;
		case 4:
			sResult=collection.deleteInfo(req);
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

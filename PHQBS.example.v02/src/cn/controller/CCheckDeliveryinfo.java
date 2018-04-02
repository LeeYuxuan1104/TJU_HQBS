package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.Delivery;

public class CCheckDeliveryinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null;
		Delivery		delivery=	new Delivery();
		switch (operType) {
		//	1.查询全信息;
		case 1:
			sResult	=	delivery.getAlldeliverys(req);
			break;
		//	2.按照条件进行查询;
		case 2:
			sResult	=	delivery.getDeliveryByCondition(req);
			break;
		//	3.新增订单;
		case 3:
			sResult	=	delivery.addInfo(req);
			break;
		//	4.删除订单;
		case 4:
			sResult	=	delivery.deleteInfo(req);
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

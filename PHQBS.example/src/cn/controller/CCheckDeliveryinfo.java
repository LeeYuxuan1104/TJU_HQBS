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
		Delivery		delivery=	null;
		String 			ownner,driver,model,goal,price,lng,lat,count,deadline,sql;
		System.out.println("OPER="+operType);
		switch (operType) {
		//	1.查询司机姓名+电池名称;
		case 1:
			ownner	=	req.getParameter("ownner");
			delivery=	new Delivery();
			sResult	=	delivery.getDriverList(ownner);
			break;
		//	2.进行提交相应的数据信息;
		case 2:
			ownner	=	req.getParameter("ownner");
			driver	=	req.getParameter("driver");
			model	=	req.getParameter("model");
			goal	=	req.getParameter("goal");
			price	=	req.getParameter("price");
			lng		=	req.getParameter("lng");
			lat		=	req.getParameter("lat");
			count	=	req.getParameter("count");
			deadline=	req.getParameter("deadline");
			sql=
			"insert into delivery (ownner,driver,model,price,count,lng,lat,deadline,goal) values (" +
			"'"+ownner+"'," +
			"'"+driver+"'," +
			"'"+model+"'," +
			""+price+"," +
			""+count+"," +
			""+lng+"," +
			""+lat+"," +
			"'"+deadline+"'," +
			"'"+goal+"')";
			System.out.println("sql="+sql);
			delivery=	new Delivery();
			sResult	=	delivery.updateDelivery(sql);
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

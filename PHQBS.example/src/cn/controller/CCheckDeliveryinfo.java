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
		String 			ownner,driver,model,goal,price,lng,lat,count,deadline,state,sql;
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
			delivery=	new Delivery();
			sResult	=	delivery.updateSQL(sql);
			break;
		//	3.查询相应的货单;
		case 3:
			driver	=	req.getParameter("driver");
			state	=	req.getParameter("state");
			sql		=	"select ownner,model,price,count,lng,lat,deadline,goal,id from delivery where driver='"+driver+"' and state="+state;
			delivery=	new Delivery();
			sResult	=	delivery.queryBySQL(sql);
			break;
		//	4.输入相应的品牌;
		case 4:
			driver	=	req.getParameter("driver");
			state	=	req.getParameter("state");
			model	=	req.getParameter("model");
			sql		=	"select ownner,model,price,count,lng,lat,deadline,goal,id from delivery where driver='"+driver+"' and state="+state+" and model='"+model+"'";
			delivery=	new Delivery();
			sResult	=	delivery.queryBySQL(sql);
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

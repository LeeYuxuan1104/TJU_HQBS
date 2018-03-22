package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.Driver;

public class CCheckDriverinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		/////
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null;
		String 			id,pwd,carNumber,tel,name,lng,lat,sql,node_id;
		String[]		ids;
		Driver			driver	=	null;
		switch (operType) {
		//	1.查询所有的回收员信息;
		case 1:
			driver 	 = new Driver();			
			//	进行数据的校验;
			sql	 	 = "select id,carNumber,name  from driver ";
			sResult	 = driver.queryBySQL(sql);
		break;
		//	2.进行相应的回收员增加;
		case 2:
			node_id = req.getParameter("node_id");
			id	    = req.getParameter("driver_id");
			id	    = id.substring(0, id.lastIndexOf("_"));
			//	进行数据的加载,更新操作;
			driver  = new Driver();
			ids	    = id.split("_");
			sql		= "delete from node_driver where node_id='"+node_id+"'";
			driver.updateSQL(sql);
			for(String tmpid:ids){
				sql = "insert into node_driver (node_id,driver_id) values ('"+node_id+"','"+tmpid+"')";
				driver.updateSQL(sql);
			}
			sResult="success";
			break;
		//	3.进行相应查询操作;
		case 4:
			
			break;
		//	7.注册的相应内容;
		case 7:
			id		 = req.getParameter("id");
			pwd		 = req.getParameter("password");
			carNumber= new String(req.getParameter("carnumber").getBytes("ISO8859_1"),"utf-8");
			tel		 = req.getParameter("tel");
			name	 = new String(req.getParameter("name").getBytes("ISO8859_1"),"utf-8");
			lng		 = req.getParameter("lng");
			lat		 = req.getParameter("lat");
			sql		 = "insert into driver (id,password,carNumber,tel,lng,lat,name) values (" +
					   "'"+id+"','"+pwd+"','"+carNumber+"','"+tel+"',"+lng+","+lat+",'"+name+"')";
			driver	 = new Driver();			
			//	进行数据的校验;
			sResult  = driver.updateSQL(sql);
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

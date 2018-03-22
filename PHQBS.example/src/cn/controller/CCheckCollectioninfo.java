package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.Collection;
import cn.model.entity.Delivery;
import cn.model.tool.MTConfig;

public class CCheckCollectioninfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));  		
		Collection		collection= null;
		Delivery		delivery=	null;
		MTConfig		mtConfig=	null;
		String 			ownner,driver,photo,deadline,message,lng,lat,state,id,sql,folder,sResult =  null;
		switch (operType) {
		//	1.插入新的信息;
		case 1:
			ownner		= 	new String(req.getParameter("ownner").getBytes("ISO8859_1"),"utf-8");
			driver		=	new String(req.getParameter("driver").getBytes("ISO8859_1"),"utf-8");
			photo		=	new String(req.getParameter("photo").getBytes("ISO8859_1"),"utf-8");
			
			deadline	=	new String(req.getParameter("deadline").getBytes("ISO8859_1"),"utf-8");
			message		=	new String(req.getParameter("message").getBytes("ISO8859_1"),"utf-8");
			lng			=	new String(req.getParameter("lng").getBytes("ISO8859_1"),"utf-8");
			lat			=	new String(req.getParameter("lat").getBytes("ISO8859_1"),"utf-8");
			state		=	new String(req.getParameter("state").getBytes("ISO8859_1"),"utf-8");
			id			=	req.getParameter("id");
			//	修改delivery表;
			sql			=	"update delivery set state=1 where id="+id;
			delivery	=	new Delivery();
			delivery.updateSQL(sql);
			//	修改collection表;
			sql			=	
			"insert into collection (ownner,driver,photo,deadline,message,lng,lat,state) values (" +
			"'"+ownner+"','"+driver+"','"+photo+"','"+deadline+"','"+message+"',"+lng+","+lat+",'"+state+"')";
			collection	=	new Collection();
			sResult		=	collection.updateSQL(sql);
			break;
		//	进行文件的上传;
		case 2:
			folder		= 	new String(req.getParameter("folder").getBytes("ISO8859_1"),"utf-8");
			mtConfig	=	new MTConfig();
			sResult		=	mtConfig.uploadMap(req, "sign", folder);
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

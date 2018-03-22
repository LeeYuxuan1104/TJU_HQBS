package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.Node;

public class CCheckNodeinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		/////
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null;
		String 			ownner,driver,model,param,id,password,lng,lat,name,tel,address,sql;
		Node			node	=	null;
		
		switch (operType) {
		//	1.查询回收单;
		case 1:
			ownner	=	req.getParameter("ownner");
			node	=	new Node();
			sResult	=	node.queryDeliveryAll(ownner);
			break;
		//	2.条件查询回收单;
		case 2:
			driver	=	req.getParameter("driver");
			node	=	new Node();
			param	=	" where driver ='"+driver+"'";
			//	进行数据的校验;
			sResult	=	node.queryDeliveryByCondition(param);
			break;
		//	3.查询回收员
		case 3:
			ownner	=	req.getParameter("ownner");
			node	=	new Node();
			sResult	=	node.queryDriverAll(ownner);
			break;
		//	4.条件查询回收员;
		case 4:
			
			driver	=	req.getParameter("driver");
			ownner	=	req.getParameter("ownner");
			param	=	"where a.id=c.driver_id and b.id=c.node_id and b.id='"+ownner+"' and a.id='"+driver+"'" ;
			node	=	new Node();
			sResult	=	node.queryDriverByCondition(param);
			break;
		//	5.查询所有的商品类别;
		case 5:
			node	=	new Node();
			sResult	=	node.queryGoodsAll();
			break;
		//	6.进行数据的加载;
		case 6:
			model	=	req.getParameter("model");
			param	=	"where model='"+model+"'" ;
			node	=	new Node();
			sResult	=	node.queryGoodsByCondition(param);
			break;
		//	7.插入数据;
		case 7:
			id		=	req.getParameter("id");
			password=	req.getParameter("password");
			lng		=	req.getParameter("lng");
			lat		=	req.getParameter("lat");
			name	=	new String(req.getParameter("name").getBytes("ISO8859_1"),"utf-8");
			tel		=	req.getParameter("tel");
			address	=	new String(req.getParameter("address").getBytes("ISO8859_1"),"utf-8");
			sql		=	"insert into node (id,password,lng,lat,name,tel,address) values (" +
						"'"+id+"','"+password+"',"+lng+","+lat+",'"+name+"','"+tel+"','"+address+"')";
			node	=	new Node();
			sResult	=	node.updateSQL(sql);
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

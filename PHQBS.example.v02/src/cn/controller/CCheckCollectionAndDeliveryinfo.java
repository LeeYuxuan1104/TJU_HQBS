package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.CollectionAndDelivery;
import cn.model.tool.MTConfig;

public class CCheckCollectionAndDeliveryinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		//
		PrintWriter 	pWriter = 	resp.getWriter();
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   null,folder;
		MTConfig		mtConfig;
		//	
		CollectionAndDelivery	collectionAndDelivery=new CollectionAndDelivery();
		switch (operType) {
		//	1.获得相应的回收单的信息;
		case 1:
			sResult=collectionAndDelivery.getAllOrdersByStatus(req);
			break;
		//	2.获得条件的回收单的内容;
		case 2:
			sResult=collectionAndDelivery.getAllOrdersById(req);
			break;
		//	3.已读回收单;
		case 3:
			sResult=collectionAndDelivery.changeOrderStatusById(req);
			break;
		//	4.回收单的删除;
		case 4:
			sResult=collectionAndDelivery.deleteOrderStatusById(req);
			break;
		//	5.进行相应查询;
		case 5:
			sResult=collectionAndDelivery.queryItemByKind(req);
			break;
		//	6.修改数据状态;
		case 6:
			sResult=collectionAndDelivery.updateItemById(req);
			break;
		//	7.上传文件;
		case 7:
			folder		= 	new String(req.getParameter("folder").getBytes("ISO8859_1"),"utf-8");
			System.out.println("folder="+folder);
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

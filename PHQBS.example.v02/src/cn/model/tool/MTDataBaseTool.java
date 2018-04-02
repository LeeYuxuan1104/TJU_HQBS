package cn.model.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class MTDataBaseTool {
	private String driver 	= "com.mysql.jdbc.Driver";
	private String dbName 	= "six";
	private String password = "root";
	private String userName = "root";
	private String dbaddress= "localhost";
	private String dbport	= "3306";
	private String url 		= "jdbc:mysql://"+dbaddress+":"+dbport+"/" + dbName;
	//	使用单例模式;
	private static MTDataBaseTool dataBaseTool=null;
	public MTDataBaseTool() {
		super();
	}
	//	静态;
	public static MTDataBaseTool getInstance(){
		if(dataBaseTool==null){
			dataBaseTool=new MTDataBaseTool();
		}
		return dataBaseTool;
	}
	//	检测数据的检查状态
	public Connection doCheckDB(){
		Connection conn=null;
		try {
	       Class.forName(driver);
	       conn= DriverManager.getConnection(url, userName, password);
	    } catch (Exception e) {
	       return null;
	    }
		return conn;
	}
	//	数据库的更新操作
	public int doDBUpdate(String sqlLanguage){
		int 		 	  nCount	=	0;
		Connection		  conn		=	doCheckDB();
		PreparedStatement ptmt		=	null;;
		try {
			ptmt 	= conn.prepareStatement(sqlLanguage);
			nCount 	= ptmt.executeUpdate();
			if(conn!=null){
				conn.close();
			}
			if(ptmt!=null){
				ptmt.close();
			}
		} catch (SQLException e) {
			return 0;
		}
		return nCount;
	}
	//	查询结果;
	public String query(String[] params, String sql){
		Connection conn		=	doCheckDB();
		Statement  stmt		=	null;
		//	进行数据的字符串化;
		JSONArray  array	= 	new JSONArray();
		int		   column	=	params.length;
		try {
			stmt 		 = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象
			while (rs.next()){
				JSONObject obj = new JSONObject();
				for(int i=0;i<column;i++){
					int index=i+1;
					obj.put(params[i], rs.getString(index));
				}
				array.add(obj);
			}
			if(rs!=null){
				rs.close();				
			}
			if(stmt!=null){				
				stmt.close();
			}
			if(conn!=null){				
				conn.close();
			}
		} catch (SQLException e) { 
			return null;
		}
		return array.toString();
	}
	public JSONArray query2(String[] params, String sql){
		Connection conn		=	doCheckDB();
		Statement  stmt		=	null;
		//	进行数据的字符串化;
		JSONArray  array	= 	new JSONArray();
		int		   column	=	params.length;
		try {
			stmt 		 = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象
			while (rs.next()){
				JSONObject obj = new JSONObject();
				for(int i=0;i<column;i++){
					int index=i+1;
					obj.put(params[i], rs.getString(index));
				}
				array.add(obj);
			}
			if(rs!=null){
				rs.close();				
			}
			if(stmt!=null){				
				stmt.close();
			}
			if(conn!=null){				
				conn.close();
			}
		} catch (SQLException e) { 
			return null;
		}
		return array;
	}
}

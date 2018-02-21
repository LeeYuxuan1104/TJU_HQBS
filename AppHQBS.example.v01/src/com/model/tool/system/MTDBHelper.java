package com.model.tool.system;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//	数据库的管理帮助类;
public class MTDBHelper extends SQLiteOpenHelper {
	//	业务表的结构;
	private String sql_tmp = 
			"create table driver (" +
			"_id integer primary key autoincrement," +
			"id varchar(20)," +
			"carnumber varchar(20)," +
			"name varchar(20)," +
			"state varchar(20))";	//	数据库管理类的构造函数;
	public MTDBHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//	编辑列表;
		db.execSQL(sql_tmp);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


	}
}

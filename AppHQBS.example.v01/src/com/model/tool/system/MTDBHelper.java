package com.model.tool.system;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//	数据库的管理帮助类;
public class MTDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase 		 	sqlDB;		//  数据库件;
    private MTDBHelper 	   		 	mDBhelper;	//  辅助控件;
	
	//	业务表的结构;
	private String sql_tmp = 
			"create table driver (" +
			"_id integer primary key autoincrement," +
			"id varchar(20)," +
			"carnumber varchar(20)," +
			"name varchar(20)," +
			"state varchar(20))";	//	数据库管理类的构造函数;
	private String sql_history=
			"create table driver_history (" +
			"_id integer primary key autoincrement," +
			"did integer ," +
			"ownner varchar(20)," +
			"driver varchar(20)," +
			"name varchar(20)," +
			"model varchar(100)," +
			"price varchar(20)," +
			"count varchar(20)," +
			"photo varchar(255)," +
			"deadline varchar(30)," +
			"message varchar(500)," +
			"lng varchar(20)," +
			"lat varchar(20)," +
			"goal varchar(500)," +
			"state varchar(10)" +
			")";
	
	public MTDBHelper(Context context, String name, int version) {
		super(context, name, null, version);	
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//	编辑列表;
		db.execSQL(sql_tmp);
		//	历史列表;
		db.execSQL(sql_history);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


	}
	//	数据库内容;
	public SQLiteDatabase getSqlDB() {
		return sqlDB;
	}
	public void doCloseDataBase(){
		if(sqlDB!=null){
			sqlDB.close();
			sqlDB=null;
		}
		if(mDBhelper!=null){
			mDBhelper.close();
			mDBhelper=null;
		}
	}
	
}

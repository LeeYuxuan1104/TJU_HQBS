package com.model.tool.system;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MTSQLiteHelper {
	private SQLiteDatabase 		 	mDB;		//  数据库件;
    private MTDBHelper 	   		 	mDBhelper;	//  辅助控件;
	private final String DB_NAME	="db_hqbs";
	private final int 	 DB_VERSION =1;

	public SQLiteDatabase getmDB() {
		return mDB;
	}

	public void setmDB(SQLiteDatabase mDB) {
		this.mDB = mDB;
	}

	public MTSQLiteHelper(Context context) {
		File 	file	=	context.getCacheDir();		
		String 	path 	= 	file.getAbsolutePath() + "/"+DB_NAME;
		mDBhelper  		= 	new MTDBHelper(context, path, DB_VERSION);
		mDB 			= 	mDBhelper.getReadableDatabase();	
	}
	
	public void doCloseDataBase(){
		if(mDB!=null){
			mDB.close();
			mDB=null;
		}
		if(mDBhelper!=null){
			mDBhelper.close();
			mDBhelper=null;
		}
	}
}

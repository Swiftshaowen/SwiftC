package com.sw.sqlitedao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author  ChenShaoWen
 * 2017年4月28日  下午11:10:04
 */
public class MyHelper extends SQLiteOpenHelper{

//创建数据库的名称是shoucang.db
	public MyHelper(Context context) {
		super(context, "shoucang.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建表sc  标题、url两个字段
		db.execSQL("create table sc(title VARCHAR(7000),url VARCHAR(7000))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("onUpgrade");

	}

	
}

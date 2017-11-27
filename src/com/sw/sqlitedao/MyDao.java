package com.sw.sqlitedao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author  ChenShaoWen
 * 2017年4月28日  下午11:21:11
 */
public class MyDao {

	//编写操作数据库
	private MyHelper myhelper;

	public MyDao(Context context) {
		myhelper = new MyHelper(context);
	}
	
	//插入一条数据
	public void insert(String title,String url)
	{
		//获取数据库对象
		SQLiteDatabase db  = myhelper.getWritableDatabase();
		//用来装载插入的数据的Map<列名，列的值>
	    ContentValues values = new ContentValues();
	    values.put("title", title);
	    values.put("url", url);
	    //向sc表中插入一条数据
	    db.insert("sc", null, values);
	    //关闭数据库
	    db.close();
	}
	//查询所有收藏的数据
	public List<MyScBean> queryall()
	{
		SQLiteDatabase db  = myhelper.getWritableDatabase();
		Cursor c = db.query("sc", null, null, null, null, null,null);
		List<MyScBean> list = new ArrayList<MyScBean>();
	while(c.moveToNext())
	{
		String title = c.getString(0);
		String url = c.getString(1);
		list.add(new MyScBean(title, url));
		
	}
	c.close();
	db.close();
	return list;
	}
}

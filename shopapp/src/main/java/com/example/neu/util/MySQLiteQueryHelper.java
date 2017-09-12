package com.example.neu.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteQueryHelper extends SQLiteOpenHelper{
	
	private final String createSQL="create table shopcar(_id integer primary key autoincrement,goods_id integer,goods_name text,goods_num integer,goods_size text,goods_color text,goods_pic text,user_name text)";
    private final String dropSQL="drop table if exists shopcar";
	public MySQLiteQueryHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		if(!tableIsExist("shopcar"))
			db.execSQL(createSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(dropSQL);
        onCreate(db);
	}
	
	public boolean tableIsExist(String tableName){
        boolean result = false;
        if(tableName == null){
                return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
                db = this.getReadableDatabase();
                String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tableName.trim()+"' ";
                cursor = db.rawQuery(sql, null);
                if(cursor.moveToNext()){
                        int count = cursor.getInt(0);
                        if(count>0){
                                result = true;
                        }
                }
                
        } catch (Exception e) {
                // TODO: handle exception
        	e.printStackTrace();
        }               
        return result;
	}
	
}

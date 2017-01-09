package com.example.gjen.newproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by GJen on 2016/9/3.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DBHelper的物件呼叫getWritableDatabase() 或 getReadableDatabase()
        //會先檢查資料庫檔案是否存在，不存在則建立檔案並呼叫onCreate()方法
        //        final String TABLE_NAME="MyTable";
        //        final String SQL_CREATE_TABLE="CREATE TABLE  "+TABLE_NAME+"  (Id,Name,Phone,Address);";
        db.execSQL(SQLiteUtil.SQL_CREATE_TABLE_MONEY);
        db.execSQL(SQLiteUtil.SQL_CREATE_TABLE_CALENDAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if(oldVersion==1){
//            if(newVersion==2){
//                String SQL="ALTER TABLE "+SQLiteUtil.TABLE_NAME+ " ADD COLUMN CellPhone;";
//                db.execSQL(SQL);
//            }
//
//        }
    }
}

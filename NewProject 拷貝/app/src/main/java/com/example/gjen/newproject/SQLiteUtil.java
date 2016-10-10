package com.example.gjen.newproject;

/**
 * Created by student on 2016/3/15.
 */
public class SQLiteUtil {
    public static final int VERSION=2;
    public static final String MONEY_TABLE_NAME="MoneyTable";
    public static final String CALENDAR_TABLE_NAME="CalendarTable";
    public static final String DATABASE_NAME="ProjectDB.db";
    public static final String SQL_CREATE_TABLE_CALENDAR="CREATE TABLE IF NOT EXISTS "+CALENDAR_TABLE_NAME+"  (Hour,Lat,Long,Location,Event,Schedule);";
    public static final String SQL_CREATE_TABLE_MONEY="CREATE TABLE IF NOT EXISTS "+MONEY_TABLE_NAME+"  (Time,Event,Money,Need);";
}

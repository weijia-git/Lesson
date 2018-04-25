package com.example.pc.lesson;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//		id name sex protitle dep lessons
        String sql = "create table teacher (id text primary key,name text,sex text,protitle text,dep text,lessons text)";
        db.execSQL(sql);
        // week coursename learningperiod courseperiod classroom classes studentnum
        sql = "create table courseinfo (week integer primary key,coursename text,learningperiod text,courseperiod text,classroom text,classes text,studentnum text)";
        db.execSQL(sql);
        // Lesson: lessonname learningperiod credit units lessons;
        sql = "create table lesson (week integer primary key,coursename text,learningperiod text,courseperiod text,classroom text,classes text,studentnum text)";
        db.execSQL(sql);
        // week coursename learningperiod courseperiod classroom classes studentnum
        sql = "create table courseinfo (week integer primary key,coursename text,learningperiod text,courseperiod text,classroom text,classes text,studentnum text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

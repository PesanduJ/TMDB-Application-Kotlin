package com.example.tmdbapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context:Context) : SQLiteOpenHelper(context,"UserDB",null,1) {
    override fun onCreate(SQLiteDB: SQLiteDatabase?) {

        SQLiteDB?.execSQL("CREATE TABLE IF NOT EXISTS UserData(firstName varchar(100),lastName varchar(100),nic varchar(100),email varchar(100),phoneNo varchar(100),password varchar(100))")


    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}
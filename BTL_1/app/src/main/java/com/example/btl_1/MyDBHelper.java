package com.example.btl_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    //khởi tạo tham số
    public static final String DATABASE_NAME = "contact.db";
    public static final int DATABASE_VERSION =1;

    //Khai báo column table
    public static final String TblName = "tblFolder";
    public static final String ID = "id";
    public static final String NameFD = "namefd";

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTbl = "CREATE TABLE " + TblName + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NameFD + " TEXT)";
        db.execSQL(createTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TblName);
        onCreate(db);
    }
}

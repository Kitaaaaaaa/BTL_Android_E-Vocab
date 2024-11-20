package com.example.btl_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DBName = "E_vocab.db";
    public static final int DBVersion = 1;

    //Khai bao cac colum name va tbl name cua tblVocab
    public static final String vocab_tblName = "tblVocab";
    public static final String vocab_id = "idVocab";
    public static final String vocab_id_folder = "idFolder";
    public static final String vocab_terminology = "Terminology";
    public static final String vocab_type = "Type";
    public static final String vocab_definition = "Definition";
    public static final String vocab_example = "Example";


    //Cua tblFolder


    //Phuong thuc khoi tao
    public MyDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    //Bat ho tro foreign key
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Lenh tao bang tblFolder
        String sql1 = "CREATE TABLE tblFolder(idFolder INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)";
        db.execSQL(sql1);

        //Lenh tao bang tblVocab
        String sql2 = "CREATE TABLE " + vocab_tblName + "(" +
                vocab_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                vocab_id_folder + " INTEGER REFERENCES tblFolder(idFolder) " +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                vocab_terminology + " TEXT NOT NULL, " +
                vocab_type + " TEXT, " +
                vocab_definition + " TEXT, " +
                vocab_example + " TEXT)";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + vocab_tblName);
        db.execSQL("Drop table if exists tblFolder");
        onCreate(db);
    }
}

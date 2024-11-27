package com.example.btl_thuong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DBName = "BTL_Thuong.db";
    public static final int DBVersion = 1;

    //Bang User
    public static final String user_tblName = "tblUser";
    public static final String user_id = "idUser";
    public static final String user_username = "UserName";
    public static final String user_phoneNumber = "PhoneNumber";
    public static final String user_password = "Password";

    //Bang folder
    public static final String folder_tblName = "tblFolder";
    public static final String folder_id = "idFolder";
    public static final String folder_name = "FolderName";
    public static final String folder_id_user = "idUser";

    //Bang Vocab
    public static final String vocab_tblName = "tblVocab";
    public static final String vocab_id = "idVocab";
    public static final String vocab_id_folder = "idFolder";
    public static final String vocab_terminology = "Terminology";
    public static final String vocab_type = "Type";
    public static final String vocab_definition = "Definition";
    public static final String vocab_example = "Example";

    public MyDBHelper(@Nullable Context context) {
        super(context, DBName, null, DBVersion);
    }

    //Bat ho tro foreign key
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tao bang User
        String createTblUser = "CREATE TABLE " + user_tblName + "("
                + user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + user_username + " TEXT NOT NULL UNIQUE, "
                + user_phoneNumber + " TEXT, "
                + user_password + " TEXT NOT NULL)";
        db.execSQL(createTblUser);

//        db.execSQL("PRAGMA foreign_keys = ON;");
        // Tạo bảng tblFolder
        String createTblFolder = "CREATE TABLE " + folder_tblName + "("
                + folder_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + folder_name + " TEXT NOT NULL, "
                + folder_id_user + " INTEGER REFERENCES " + user_tblName + "(" + user_id + ")"
                + " ON DELETE CASCADE ON UPDATE CASCADE)";

        db.execSQL(createTblFolder);

        // Tạo bảng tblVocab
        String createTblVocab = "CREATE TABLE " + vocab_tblName + "(" +
                vocab_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                vocab_terminology + " TEXT NOT NULL, " +
                vocab_type + " TEXT, " +
                vocab_definition + " TEXT NOT NULL, " +
                vocab_example + " TEXT," +
                vocab_id_folder + " INTEGER REFERENCES " + folder_tblName + "(" + folder_id + ")" +
                "ON DELETE CASCADE ON UPDATE CASCADE)";

        db.execSQL(createTblVocab);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+user_tblName);
        db.execSQL("Drop table if exists " + folder_tblName);
        db.execSQL("Drop table if exists " + vocab_tblName);
        onCreate(db);
    }
}

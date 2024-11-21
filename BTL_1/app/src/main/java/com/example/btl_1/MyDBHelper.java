package com.example.btl_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu và phiên bản
    public static final String DATABASE_NAME = "btl_android.db";
    public static final int DATABASE_VERSION = 3;

    // Cột bảng tblFolder
    public static final String TblFolder = "tblFolder";
    public static final String FolderID = "folderId";
    public static final String NameFD = "namefd";

    // Cột bảng tblVocab
    public static final String vocab_tblName = "tblVocab";
    public static final String vocab_id = "idVocab";
    public static final String vocab_id_folder = "idFolder";
    public static final String vocab_terminology = "Terminology";
    public static final String vocab_type = "Type";
    public static final String vocab_definition = "Definition";
    public static final String vocab_example = "Example";

    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng tblFolder
        String createTblFolder = "CREATE TABLE " + TblFolder + "("
                + FolderID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NameFD + " TEXT NOT NULL)";
        db.execSQL(createTblFolder);

        // Tạo bảng tblVocab
        String createTblVocab = "CREATE TABLE " + vocab_tblName + "(" +
                vocab_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                vocab_terminology + " TEXT NOT NULL, " +
                vocab_type + " TEXT, " +
                vocab_definition + " TEXT, " +
                vocab_example + " TEXT," +
                vocab_id_folder + " INTEGER REFERENCES " + TblFolder + "(" + FolderID + ")" +
                "ON DELETE CASCADE ON UPDATE CASCADE)";

        db.execSQL(createTblVocab);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Thực hiện xóa bảng và tạo lại nếu cần
        db.execSQL("DROP TABLE IF EXISTS " + vocab_tblName);
        db.execSQL("DROP TABLE IF EXISTS " + TblFolder);
        // Tạo lại bảng với cấu trúc mới
        onCreate(db);
    }
}

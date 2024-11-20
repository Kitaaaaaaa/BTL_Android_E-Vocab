package com.example.btl_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManageger {
    private MyDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public DBManageger(Context context){
        this.context = context;
    }

    public DBManageger open(){
        dbHelper = new MyDBHelper (context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insFolder(FolderItem fditem){
        ContentValues contentValues= new ContentValues();
        contentValues.put(dbHelper.NameFD, fditem.getName());

        return db.insert(dbHelper.TblName, null, contentValues);

    }

    public int updFolder(FolderItem fditem, int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.NameFD,fditem.getName());
        return db.update(dbHelper.TblName, contentValues, dbHelper.ID + "="+id,null);
    }
    public int delContact(int id){
        return db.delete(dbHelper.TblName, dbHelper.ID +"="+id,null);
    }

    public ArrayList<FolderItem> getAllContact(){
        ArrayList<FolderItem> kq = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TblName,null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                kq.add(new FolderItem( id, name));
            } while (cursor.moveToNext());
        }
        cursor.close(); // Nhớ đóng cursor sau khi dùng


        return kq;
    }
}

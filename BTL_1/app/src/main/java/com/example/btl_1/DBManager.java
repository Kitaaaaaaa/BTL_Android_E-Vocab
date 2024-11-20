package com.example.btl_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    //Khai bao lop tao DB
    private MyDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;
    public DBManager(Context context){
        this.context=context;
    }

    //Mo ket noi den DB va lay ra doi tuong db de thuc thi
    public DBManager open(){
        dbHelper = new MyDBHelper(context);
        db=dbHelper.getWritableDatabase();
        return this;
    }

    //dong ket noi
    public void close(){
        dbHelper.close();
    }

//Thuc hien voi vocab
    //Them moi vocab
    public long insVocab(MyVocab vocab){
        ContentValues values = new ContentValues();
        values.put(dbHelper.vocab_terminology, vocab.getTerminology());
        values.put(dbHelper.vocab_type, vocab.getType());
        values.put(dbHelper.vocab_definition, vocab.getDefinition());
        values.put(dbHelper.vocab_example, vocab.getExample());
        values.put(dbHelper.vocab_example, vocab.getExample());
        return db.insert(dbHelper.vocab_tblName, null, values);
    }

    //Cap nhat vocab
    public int updayeVocab(MyVocab vocab, int id){
        ContentValues values = new ContentValues();
        values.put(dbHelper.vocab_terminology, vocab.getTerminology());
        values.put(dbHelper.vocab_type, vocab.getType());
        values.put(dbHelper.vocab_definition, vocab.getDefinition());
        values.put(dbHelper.vocab_example, vocab.getExample());
        values.put(dbHelper.vocab_example, vocab.getExample());
        return db.update(dbHelper.vocab_tblName, values, dbHelper.vocab_id +" = "+id, null);
    }

    //Xoa vocab
    public int deledeVocab(int id){
        return db.delete(dbHelper.vocab_tblName, dbHelper.vocab_id + " = "+id, null);
    }

    //truy xuat du lieu
    public ArrayList<MyVocab> getAllVocab(){
        ArrayList<MyVocab> lst = new ArrayList<>();
        Cursor c = db.rawQuery("Select * from "+dbHelper.vocab_tblName, null);
        if (c != null) {
            c.moveToFirst();
        }
        while (c.moveToNext()) {
            int id = c.getInt(0);
            int idF = c.getInt(1);
            String termi = c.getString(2);
            String type = c.getString(3);
            String defi = c.getString(4);
            String ex = c.getString(5);
            lst.add(new MyVocab(termi, type, defi, ex));
        }
        return lst;
    }

    //Dem so luong vocab
    public int getNumberOfVocab(){
        String sql = "SELECT COUNT(*) FROM "+dbHelper.vocab_tblName;
        Cursor c =db.rawQuery(sql, null);
        int count = 0;
        if (c != null) {
            c.moveToFirst();
            count=c.getInt(0);
            c.close();
        }
        return count;
    }



}











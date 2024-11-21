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
    public long insVocab(MyVocab vocab, int idFolder){
        ContentValues values = new ContentValues();
        values.put(dbHelper.vocab_terminology, vocab.getTerminology());
        values.put(dbHelper.vocab_type, vocab.getType());
        values.put(dbHelper.vocab_definition, vocab.getDefinition());
        values.put(dbHelper.vocab_example, vocab.getExample());
        values.put(dbHelper.vocab_id_folder,idFolder);
        return db.insert(dbHelper.vocab_tblName, null, values);
    }

    //Cap nhat vocab
    public int updateVocab(MyVocab vocab, int id){
        ContentValues values = new ContentValues();
        values.put(dbHelper.vocab_terminology, vocab.getTerminology());
        values.put(dbHelper.vocab_type, vocab.getType());
        values.put(dbHelper.vocab_definition, vocab.getDefinition());
        values.put(dbHelper.vocab_example, vocab.getExample());
        return db.update(dbHelper.vocab_tblName, values, dbHelper.vocab_id +" = "+id, null);
    }

    //Xoa vocab
    public int deleteVocab(int id){
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

//Thuc hien voi folder
    public long insFolder(FolderItem fditem){
        ContentValues contentValues= new ContentValues();
        contentValues.put(dbHelper.NameFD, fditem.getName());

        return db.insert(dbHelper.TblFolder, null, contentValues);
    }

    public int updFolder(FolderItem fditem, int id) {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase(); // Đảm bảo db được mở
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.NameFD, fditem.getName());
        return db.update(dbHelper.TblFolder, contentValues, dbHelper.FolderID + "=?", new String[]{String.valueOf(id)});
    }

    public int delFolder(int id){
        return db.delete(dbHelper.TblFolder, dbHelper.FolderID + "=?", new String[]{String.valueOf(id)});
    }


    public ArrayList<FolderItem> getAllFolder(){
        ArrayList<FolderItem> kq = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TblFolder,null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                kq.add(new FolderItem( id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return kq;
    }
    public ArrayList<MyVocab> getVocabByFolderId(int folderId) {
        ArrayList<MyVocab> vocabList = new ArrayList<>();

        // Truy vấn lấy vocab theo idFolder
        String query = "SELECT * FROM " + dbHelper.vocab_tblName +
                " WHERE " + dbHelper.vocab_id_folder + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(folderId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.vocab_id));
                String terminology = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.vocab_terminology));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.vocab_type));
                String definition = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.vocab_definition));
                String example = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.vocab_example));

                // Tạo đối tượng MyVocab và thêm vào danh sách
                vocabList.add(new MyVocab(id, terminology, type, definition, example));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return vocabList;
    }

}











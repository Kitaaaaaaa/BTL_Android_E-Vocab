package com.example.btl_thuong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    private MyDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        dbHelper = new MyDBHelper(context);
        this.context = context;
    }

    public DBManager open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //Do with user
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.user_username, user.getUsername());
        values.put(dbHelper.user_phoneNumber, user.getPhoneNumber());
        values.put(dbHelper.user_password, user.getPassword());
        return db.insert(dbHelper.user_tblName, null, values);
    }

    //check if the user exists with password
    public boolean isValidUser(String username, String password) {
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.user_tblName +
                        " WHERE " + dbHelper.user_username + " = ? AND " + dbHelper.user_password + " = ?",
                new String[]{username, password});
        boolean result = c.getCount() > 0;
        c.close();
        return result;
    }

    //username exists
    public boolean isUserNameExists(String username){
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.user_tblName +
                        " WHERE " + dbHelper.user_username + " = ?",
                new String[]{username});
        boolean exists = c.getCount()>0;
        c.close();
        return exists;
    }

    //Get user by username
    public User getUser(String username) {
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.user_tblName +
                        " WHERE " + dbHelper.user_username + " = ?",
                new String[]{username});
        if(c.moveToFirst()){
            int userID = c.getInt(0);
            String phoneNumber = c.getString(2);
            String password = c.getString(3);
            c.close();
            return new User(userID, username, phoneNumber, password);
        }
        c.close();
        return null;
    }
    public User getUserById(int userId) {
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.user_tblName +
                        " WHERE " + dbHelper.user_id + " = ?",
                new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            String username = c.getString(1);
            String phoneNumber = c.getString(2);
            String password = c.getString(3);
            c.close();
            return new User(userId, username, phoneNumber, password); // Khởi tạo đối tượng User
        }
        c.close();
        return null; // Trả về null nếu không tìm thấy người dùng
    }
    //Do with Folder
    public long insFolder(FolderItem f) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.folder_name, f.getFolderName());
        values.put(dbHelper.folder_id_user, f.getUserID());
        return db.insert(dbHelper.folder_tblName, null, values);
    }

    public int updateFolder(FolderItem f, int id) {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase(); //Make sure that db is opened
        }
        ContentValues values = new ContentValues();
        values.put(dbHelper.folder_name, f.getFolderName());
        return db.update(dbHelper.folder_tblName, values, dbHelper.folder_id + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteFolder(int id) {
        return db.delete(dbHelper.folder_tblName, dbHelper.folder_id + "=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<FolderItem> getAllFolder(int userID) {
        ArrayList<FolderItem> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.folder_tblName +
                " WHERE " +dbHelper.folder_id_user + " = ?", new String[]{String.valueOf(userID)});
        if (c != null && c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String name = c.getString(1);
                list.add(new FolderItem(id, name));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }


    //do with tblVocab
    public long insertVocab(VocabItem vocab) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.vocab_terminology, vocab.getTerminology());
        values.put(dbHelper.vocab_type, vocab.getType());
        values.put(dbHelper.vocab_definition, vocab.getDefinition());
        values.put(dbHelper.vocab_example, vocab.getExample());
        values.put(dbHelper.vocab_id_folder, vocab.getFolderID());
        return db.insert(dbHelper.vocab_tblName, null, values);
    }

    public int updateVocab(VocabItem vocab, int id) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.vocab_terminology, vocab.getTerminology());
        values.put(dbHelper.vocab_type, vocab.getType());
        values.put(dbHelper.vocab_definition, vocab.getDefinition());
        values.put(dbHelper.vocab_example, vocab.getExample());
        values.put(dbHelper.vocab_id_folder, vocab.getFolderID());
        return db.update(dbHelper.vocab_tblName, values, dbHelper.vocab_id + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteVocab(int id) {
        return db.delete(dbHelper.vocab_tblName, dbHelper.vocab_id + " = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<VocabItem> getAllVocab(int folderID) {
        ArrayList<VocabItem> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + dbHelper.vocab_tblName + " WHERE " + dbHelper.vocab_id_folder + " = ?", new String[]{String.valueOf(folderID)});
        if (c != null && c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String term = c.getString(1);
                String type = c.getString(2);
                String def = c.getString(3);
                String ex = c.getString(4);
                list.add(new VocabItem(id, term, type, def, ex, folderID));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}














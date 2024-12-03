package com.example.btl_thuong;

import java.io.Serializable;

public class FolderItem implements Serializable {
    int id;
    String folderName;
    int userID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public FolderItem() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public FolderItem(String folderName) {
        this.folderName = folderName;
    }

    public FolderItem(int id, String folderName) {
        this.id = id;
        this.folderName = folderName;
    }

    public FolderItem(int id, String folderName, int userID) {
        this.id = id;
        this.folderName = folderName;
        this.userID = userID;
    }

    public FolderItem(String folderName, int userID) {
        this.folderName = folderName;
        this.userID = userID;
    }
}

package com.example.btl_thuong;

import java.io.Serializable;

public class User implements Serializable {
    int userID;
    String username, phoneNumber, password;

    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String password, String username, int userID) {
        this.userID = userID;
        this.password = password;
        this.username = username;

    }

    public User(int userID, String username, String phoneNumber, String password) {
        this.userID = userID;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

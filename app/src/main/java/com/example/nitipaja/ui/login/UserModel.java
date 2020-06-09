package com.example.nitipaja.ui.login;

public class UserModel {

    private String userID, userName, userEmail, userPhonenumber;

    public UserModel() {
    }

    public UserModel(String userID, String userName, String userEmail, String userPhonenumber) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhonenumber = userPhonenumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhonenumber() {
        return userPhonenumber;
    }

    public void setUserPhonenumber(String userPhonenumber) {
        this.userPhonenumber = userPhonenumber;
    }
}

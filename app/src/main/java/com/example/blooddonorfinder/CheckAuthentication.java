package com.example.blooddonorfinder;

public class CheckAuthentication {
    private static boolean login = false;
    private static int userID;

    public boolean isLogin(){
        return login;
    }

    public void makeLogin(){
        login = true;
    }

    public void makeLogOut(){
        login = false;
        setUserID(-1);
    }

    public void setUserID(int id){
        userID = id;
    }

    public int getUserID(){
        return userID;
    }
}

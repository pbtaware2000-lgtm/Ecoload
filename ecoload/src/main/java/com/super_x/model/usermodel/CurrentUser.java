package com.super_x.model.usermodel;

public class CurrentUser {
    private static CurrentUser instance;

    private UserModel user;

    private CurrentUser(){

    }

    public static CurrentUser getInstance(){
        if(instance == null){
            instance = new CurrentUser();
        }
        return instance;
    }
    public void setUser(UserModel user){
        this.user = user;
    }
    public UserModel getUser(){
        return user;
    }
    public boolean isLoggedIn(){
        return user != null;
    }
    public void logout(){
        user = null;
    }
    
}

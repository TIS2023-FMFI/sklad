package com.example.warehouse;

public class User {

    private String username;
    private boolean admin;

    public User(String username, boolean admin) {
        this.username = username;
        this.admin = admin;
    }

    public String getName(){
        return username;
    }

    public boolean isAdmin(){
        return admin;
    }
}

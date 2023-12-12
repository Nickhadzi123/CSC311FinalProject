package com.example.csc311finalproject;

import com.google.cloud.firestore.annotation.DocumentId;

public class User {
    @DocumentId
    private String id; // Automatically generated by Firestore
    private String username;
    private String password;

    // Constructors, getters, and setters

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


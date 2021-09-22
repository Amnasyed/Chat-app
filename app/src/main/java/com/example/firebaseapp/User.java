package com.example.firebaseapp;

public class User {
    public String fullName;
    public String email;
    public String password;
    public String id;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }

    public User(String fullName, String email, String password, String id) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.id = id;
    }
}

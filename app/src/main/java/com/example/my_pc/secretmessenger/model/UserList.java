package com.example.my_pc.secretmessenger.model;

public class UserList {

    private String name;
    private String photoUrl;
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public UserList(String name, String photoUrl, String email) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.email = email;
    }
}

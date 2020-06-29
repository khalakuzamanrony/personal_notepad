package com.example.mininotes.Model;

public class ProfileModel {
    String name,email,id;

    public ProfileModel(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public ProfileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

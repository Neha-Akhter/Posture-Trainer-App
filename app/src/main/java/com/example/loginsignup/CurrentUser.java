package com.example.loginsignup;

public class CurrentUser {
    int id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrentUser(int id, String name) {
        this.id = id;
        this.name=name;
    }
}

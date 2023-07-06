package com.example.loginsignup;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class UsersPOJO extends RealmObject {
    @PrimaryKey
    @NotNull
    private ObjectId _id;
    @NotNull
    private String _partition;
    @NotNull
    private String username;
    @NotNull
    private String gender;
    @NotNull
    private String age;
    // empty constructor required for MongoDB Data Access POJO codec compatibility
    public UsersPOJO() {
    }

    public UsersPOJO(ObjectId _id, String _partition, String username, String gender, String  age)
    {
        this._id = _id;
        this._partition = _partition;
        this.username = username;
        this.gender=gender;
        this.age=age;
    }

    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public String get_partition() { return _partition; }
    public void set_partition(String _partition) { this._partition = _partition; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getGender(){return gender;}
    public void setGender(String gender){this.gender=gender;}
    public String getAge() {
        return age;
    }
    public void setAge(String  age) {
        this.age = age;
    }

}


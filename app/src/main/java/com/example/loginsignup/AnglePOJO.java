package com.example.loginsignup;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AnglePOJO extends RealmObject {
    @PrimaryKey
    @NotNull
    private ObjectId _id;
    @NotNull
    private String _partition;
    @NotNull
    private String name;
    @NotNull
    private String date;
    @NotNull
    private String time;
    @NotNull
    private String ForwardAngle;
    @NotNull
    private String BackwardAngle;
    @NotNull
    private String LeftAngle;
    @NotNull
    private String RightAngle;
    @NotNull
    private String MonthYear;

    // empty constructor required for MongoDB Data Access POJO codec compatibility
    public AnglePOJO() {
    }

    public AnglePOJO(ObjectId _id, String _partition, String name, String time, String date, String ForwardAngle, String BackwardAngle, String
            RightAngle, String LeftAngle, String MonthYear) {
        this._id = _id;
        this._partition = _partition;
        this.name = name;
        this.date = date;
        this.time = time;
        this.ForwardAngle = ForwardAngle;
        this.BackwardAngle = BackwardAngle;
        this.LeftAngle = LeftAngle;
        this.RightAngle = RightAngle;
        this.MonthYear=MonthYear;
    }

    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public String get_partition() { return _partition; }
    public void set_partition(String _partition) { this._partition = _partition; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getRightAngle() {
        return RightAngle;
    }

    public void setRightAngle(String rightAngle) {
        RightAngle = rightAngle;
    }

    public String getLeftAngle() {
        return LeftAngle;
    }

    public void setLeftAngle(String leftAngle) {
        LeftAngle = leftAngle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getForwardAngle() {
        return ForwardAngle;
    }

    public void setForwardAngle(String forwardAngle) {
        ForwardAngle = forwardAngle;
    }

    public String getBackwardAngle() {
        return BackwardAngle; }
    public void setBackwardAngle(String backwardAngle) {
        BackwardAngle = backwardAngle;
    }

    public String getMonthYear() {
        return MonthYear;
    }

    public void setMonthYear(String monthYear) {
       MonthYear = monthYear;
    }

}


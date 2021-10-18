package com.example.runningtracker.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "trackpoint")
public class TrackPoint {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "latitude")
    private double latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private double longitude;

    @NonNull
    @ColumnInfo(name = "time")
    private long time;

    public TrackPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = Calendar.getInstance().getTime().getTime();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(@NonNull double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(@NonNull double longitude) {
        this.longitude = longitude;
    }

    public void setTime(@NonNull long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public long getTime() {
        return time;
    }

    @NonNull
    public String getTimeFormatted() {
        return new SimpleDateFormat("HH:mm:ss")
                .format(new Date(time));
    }
}

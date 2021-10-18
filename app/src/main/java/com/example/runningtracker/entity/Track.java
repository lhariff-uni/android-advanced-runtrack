package com.example.runningtracker.entity;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "track")
public class Track {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "totalDistance")
    private float totalDistance;

    @NonNull
    @ColumnInfo(name = "startTime")
    private long startTime;

    @NonNull
    @ColumnInfo(name = "endTime")
    private long endTime;

    @NonNull
    @ColumnInfo(name = "pace")
    private float pace;

    @Ignore
    public Track(String name) {
        Calendar rn = Calendar.getInstance();
        this.name = name.equals("") ? ("Run " + this.id) : name;
        this.totalDistance = 0;
        this.startTime = rn.getTime().getTime();
        this.endTime = rn.getTime().getTime();
        this.pace = 0;
    }

    public Track(String name, String description) {
        this(name);
        this.description = description;
    }

    public float getPace() {
        return pace;
    }

    public void setPace(float pace) {
        this.pace = pace;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTimeFormatted() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(startTime));
    }

    public String getEndTimeFormatted() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(endTime));
    }

    public void setName(String name) {
        this.name = name.equals("") ? ("Run " + this.id) : name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(startTime));
    }

    @SuppressLint("DefaultLocale")
    public String getDuration() {
        long milliseconds = this.getEndTime() - this.getStartTime();
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}

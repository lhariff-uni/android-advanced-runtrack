package com.example.runningtracker.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "tracktopoint", primaryKeys = {"track_id", "trackpoint_id"})
public class TrackToPoint {
    @NonNull
    @ColumnInfo(name = "track_id")
    private final int track_id;


    @NonNull
    @ColumnInfo(name = "trackpoint_id")
    private final int trackpoint_id;

    public TrackToPoint(int track_id, int trackpoint_id) {
        this.track_id = track_id;
        this.trackpoint_id = trackpoint_id;
    }

    public int getTrack_id() {
        return track_id;
    }

    public int getTrackpoint_id() {
        return trackpoint_id;
    }
}

package com.example.runningtracker.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.runningtracker.entity.TrackPoint;

import java.util.List;

@Dao
public interface TrackPointDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TrackPoint trackPoint);

    @Query("DELETE FROM trackpoint")
    void deleteAll();

    @Query("SELECT * FROM trackpoint")
    LiveData<List<TrackPoint>> getAllTrackPoints();

    @Query("SELECT * FROM trackpoint")
    Cursor getAllTrackPointsCursor();

    @Query("SELECT * FROM trackpoint WHERE _id == :trackpoint_id")
    Cursor getTrackPointCursor(int trackpoint_id);

    @Query("DELETE FROM trackpoint WHERE _id == :trackpoint_id")
    void deleteTrackPoint(int trackpoint_id);
}

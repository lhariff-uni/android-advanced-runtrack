package com.example.runningtracker.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.runningtracker.entity.TrackPoint;
import com.example.runningtracker.entity.TrackToPoint;

import java.util.List;

@Dao
public interface TrackToPointDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TrackToPoint trackToPoint);

    @Query("DELETE FROM tracktopoint")
    void deleteAll();

    @Query("SELECT * FROM tracktopoint")
    LiveData<List<TrackToPoint>> getAllTrackToPoints();

    @Query("SELECT * FROM tracktopoint")
    Cursor getAllTrackToPointsCursor();

    @Query("SELECT p._id, p.latitude, p.longitude, p.time " +
            "from trackpoint p " +
            "join tracktopoint tp on (tp.trackpoint_id = p._id) where tp.track_id == :track_id")
    List<TrackPoint> getTrackAndTrackPoints(int track_id);

    @Query("DELETE FROM tracktopoint WHERE track_id == :track_id")
    void deleteTrackToPoint(int track_id);
}

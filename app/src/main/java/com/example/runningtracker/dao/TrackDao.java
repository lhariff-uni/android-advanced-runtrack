package com.example.runningtracker.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.runningtracker.entity.Track;

import java.util.List;

@Dao
public interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Track track);

    @Update(entity = Track.class)
    void updateTrack(Track track);

    @Query("DELETE FROM track WHERE _id == :track_id")
    void deleteTrack(int track_id);

    @Query("DELETE FROM track")
    void deleteAll();

    @Query("SELECT * FROM track")
    LiveData<List<Track>> getAllTracks();

    @Query("SELECT * FROM track WHERE _id == :track_id")
    Track getTrack(int track_id);

    @Query("SELECT * FROM track")
    Cursor getAllTracksCursor();

    @Query("SELECT * FROM track WHERE _id == :track_id")
    Cursor getTrackCursor(int track_id);
}

package com.example.runningtracker.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.runningtracker.dao.TrackDao;
import com.example.runningtracker.dao.TrackPointDao;
import com.example.runningtracker.dao.TrackToPointDao;
import com.example.runningtracker.entity.Track;
import com.example.runningtracker.entity.TrackPoint;
import com.example.runningtracker.entity.TrackToPoint;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Repository {
    private final TrackDao trackDao;
    private final TrackPointDao trackPointDao;
    private final TrackToPointDao trackToPointDao;
    private final LiveData<List<Track>> allTracks;
    private final LiveData<List<TrackPoint>> allTrackPoints;
    private final LiveData<List<TrackToPoint>> allTrackToPoints;

    public Repository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);

        trackDao = db.trackDao();
        trackPointDao = db.trackPointDao();
        trackToPointDao = db.trackToPointDao();

        allTracks = trackDao.getAllTracks();
        allTrackPoints = trackPointDao.getAllTrackPoints();
        allTrackToPoints = trackToPointDao.getAllTrackToPoints();
    }

    public LiveData<List<Track>> getAllTracks() {
        return allTracks;
    }

    public LiveData<List<TrackPoint>> getAllTrackPoints() {
        return allTrackPoints;
    }

    public LiveData<List<TrackToPoint>> getAllTrackToPoints() {
        return allTrackToPoints;
    }

    public void insert(Track track) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.insert(track);
        });
    }

    public void insert(TrackPoint trackPoint) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackPointDao.insert(trackPoint);
        });
    }

    public void insert(TrackToPoint trackToPoint) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackToPointDao.insert(trackToPoint);
        });
    }

    public void update(Track track) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.updateTrack(track);
        });
    }

    public void delete(Track track) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackDao.deleteTrack(track.getId());
        });
    }

    public void delete(TrackPoint trackPoint) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackPointDao.deleteTrackPoint(trackPoint.getId());
        });
    }

    public void deleteTrackToPoints(int track_id) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackToPointDao.deleteTrackToPoint(track_id);
        });
    }

    public Future<List<TrackPoint>> getTrackTrackPoints(int track_id) {
        Future<List<TrackPoint>> future = MyRoomDatabase.databaseWriteExecutor.submit(new Callable<List<TrackPoint>>() {
            @Override
            public List<TrackPoint> call() throws Exception {
                return trackToPointDao.getTrackAndTrackPoints(track_id);
            }
        });
        return future;
    }

    public Future<Track> getTrack(int track_id) {
        Future<Track> future = MyRoomDatabase.databaseWriteExecutor.submit(new Callable<Track>() {
            @Override
            public Track call() throws Exception {
                return trackDao.getTrack(track_id);
            }
        });
        return future;
    }
}

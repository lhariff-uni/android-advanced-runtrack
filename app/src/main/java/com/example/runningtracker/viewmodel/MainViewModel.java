package com.example.runningtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.runningtracker.db.Repository;
import com.example.runningtracker.entity.Track;
import com.example.runningtracker.entity.TrackPoint;
import com.example.runningtracker.entity.TrackToPoint;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainViewModel extends AndroidViewModel {
    private final Repository repository;

    private final LiveData<List<Track>> allTracks;
    private final LiveData<List<TrackPoint>> allTrackPoints;
    private final LiveData<List<TrackToPoint>> allTrackToPoints;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);

        allTracks = repository.getAllTracks();
        allTrackPoints = repository.getAllTrackPoints();
        allTrackToPoints = repository.getAllTrackToPoints();
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
        repository.insert(track);
    }

    public void insert(TrackPoint trackPoint) {
        repository.insert(trackPoint);
    }

    public void insert(TrackToPoint trackToPoint) {
        repository.insert(trackToPoint);
    }

}

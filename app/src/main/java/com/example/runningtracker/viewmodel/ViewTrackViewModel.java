package com.example.runningtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.runningtracker.db.Repository;
import com.example.runningtracker.entity.Track;
import com.example.runningtracker.entity.TrackPoint;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ViewTrackViewModel extends AndroidViewModel {

    private final Repository repository;
    private Track track;
    private List<TrackPoint> trackPoints;

    public ViewTrackViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void init(int track_id) throws ExecutionException, InterruptedException {
        Future<Track> futureTrack = repository.getTrack(track_id);
        track = futureTrack.get();
        Future<List<TrackPoint>> futurePoint = repository.getTrackTrackPoints(track_id);
        trackPoints = futurePoint.get();
    }

    public String getName() {
        return track.getName();
    }

    public String getDescription() {
        return track.getDescription();
    }

    public float getDistance() {
        return track.getTotalDistance();
    }

    public String getTime() {
        return track.getDuration();
    }

    public String getDate() {
        return track.getDateFormat();
    }

    public String getEndTime() {
        return track.getEndTimeFormatted();
    }

    public String getStartTime() {
        return track.getStartTimeFormatted();
    }

    public List<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    public float getPace() { return track.getPace(); }

    public void finish(String name, String description) {
        track.setName(name.equals("") ? "Run " + track.getId() : name);
        track.setDescription(description);
        repository.update(track);
    }

    public void delete() {
        repository.deleteTrackToPoints(track.getId());
        repository.delete(track);

        for(TrackPoint tp : trackPoints) {
            repository.delete(tp);
        }
    }
}

package com.example.runningtracker.viewmodel;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.runningtracker.db.Repository;
import com.example.runningtracker.entity.Track;
import com.example.runningtracker.entity.TrackPoint;
import com.example.runningtracker.entity.TrackToPoint;

import java.util.ArrayList;

public class NewTrackViewModel extends AndroidViewModel  {

    private final Repository repository;
    private final Track track;
    private final ArrayList<Location> locations;

    private int trackID;
    private int trackPointID;

    private MutableLiveData<ArrayList<TrackPoint>> trackPoints;
    private MutableLiveData<String> distance;
    private MutableLiveData<String> time;
    private MutableLiveData<String> pace;

    private NotificationCompat.Builder builder;

    public NewTrackViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        track = new Track("", "");
        locations = new ArrayList<Location>();

        builder = null;
    }

    public void init(int trackID, int trackPointID) {
        this.trackID = trackID;
        this.trackPointID = trackPointID;
    }

    public MutableLiveData<ArrayList<TrackPoint>> getTrackPoints() {
        if (trackPoints == null) {
            trackPoints = new MutableLiveData<ArrayList<TrackPoint>>();
            trackPoints.setValue(new ArrayList<TrackPoint>());
        }
        return trackPoints;
    }

    public void addTrackPoint(TrackPoint trackPoint) {
        ArrayList<TrackPoint> tp = getTrackPoints().getValue();
        tp.add(trackPoint);
        getTrackPoints().postValue(tp);
    }

    public MutableLiveData<String> getDistance() {
        if (distance == null) {
            distance = new MutableLiveData<String>();
            distance.setValue(String.valueOf(track.getTotalDistance()));
        }
        return distance;
    }

    public void addDistance(float addedDistance) {
        float value = track.getTotalDistance() + addedDistance;
        track.setTotalDistance(value);
        getDistance().setValue(String.format("%.2f", track.getTotalDistance()));
    }

    public MutableLiveData<String> getTimes() {
        if (time == null) {
            time = new MutableLiveData<String>();
            time.setValue("00:00:00");
        }
        return time;
    }

    public void incrementTime(long ms) {
        track.setEndTime(ms);
        getTimes().setValue(track.getDuration());
    }

    public MutableLiveData<String> getPace() {
        if (pace == null) {
            pace = new MutableLiveData<String>();
            pace.setValue(String.valueOf(0));
        }
        return pace;
    }

    public void setPace() {
        track.setPace(track.getTotalDistance() / ((track.getEndTime() - track.getStartTime()) / (float) 1000));
        getPace().setValue(String.format("%.2f", track.getPace()));
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(NotificationCompat.Builder builder) {
        this.builder = builder;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        builder.setContentIntent(pendingIntent);
    }

    public void newLocationPoint(Location location) {
        //Log.d("psylha", "Location Recieved: " + location.getLatitude() + " " + location.getLongitude());
        TrackPoint trackPoint = new TrackPoint(location.getLatitude(), location.getLongitude());
        addTrackPoint(trackPoint);

        locations.add(location);

        //  Add distance to Track object and to component
        if(locations.indexOf(location) == 0) {
            addDistance(0);
        } else {
            addDistance(locations.get(locations.indexOf(location) - 1).distanceTo(location));
        }
        //  Add time to Track object and to component
        incrementTime(location.getTime());
        //  Change pace to Track object and to component
        setPace();
    }

    public void finish(String name, String desc) {
        track.setName(name.equals("") ? "Run " + trackID : name);
        track.setDescription(desc);
        repository.insert(track);
        for(TrackPoint tp : trackPoints.getValue()) {
            repository.insert(tp);
            repository.insert(new TrackToPoint(trackID, trackPointID++));
        }
    }

    public Notification getNotification() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current Distance Run: ");
        sb.append(getDistance().getValue());
        sb.append(" || ");
        sb.append("Elapsed Time: ");
        sb.append(getTimes().getValue());

        builder.setContentText(sb.toString());
        return builder.build();
    }
}

package com.example.runningtracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
 *  Listener sends Location to added LocationObservers
 */
public class MyLocationListener implements LocationListener {

    private final List<LocationObserver> listeners = new ArrayList<LocationObserver>();

    public void addListener(LocationObserver toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(LocationObserver toRemove) { listeners.remove(toRemove); }

    public void sendLocation(Location location) {
        //Log.d("psylha", "Location Recieved: " + location.getLatitude() + " " + location.getLongitude());
        for(LocationObserver lo : listeners) {
            lo.newLocationPoint(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d("psylha", location.getLatitude() + " " + location.getLongitude());
        sendLocation(location);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // information about the signal, i.e. number of satellites
        //Log.d("psylha", "onStatusChanged: " + provider + " " + status);
    }
    @Override
    public void onProviderEnabled(String provider) {
        // the user enabled (for example) the GPS
        //Log.d("psylha", "onProviderEnabled: " + provider);
    }
    @Override
    public void onProviderDisabled(String provider) {
        // the user disabled (for example) the GPS
        //Log.d("psylha", "onProviderDisabled: " + provider);
    }
}

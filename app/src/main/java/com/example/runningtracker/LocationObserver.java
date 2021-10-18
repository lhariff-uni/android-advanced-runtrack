package com.example.runningtracker;

import android.location.Location;

/*
 *  Interface realised in RunService
 */
public interface LocationObserver {
    void newLocationPoint(Location location);
}

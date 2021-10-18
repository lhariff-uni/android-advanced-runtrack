package com.example.runningtracker.view;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runningtracker.R;
import android.os.Bundle;

/*
 *  Activity for use in TrackAdapter
 */
public class SingleTrackView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_track_view);
    }
}
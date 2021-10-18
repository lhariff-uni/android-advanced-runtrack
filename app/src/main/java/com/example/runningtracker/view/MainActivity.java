package com.example.runningtracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.runningtracker.R;
import com.example.runningtracker.adapter.TrackAdapter;
import com.example.runningtracker.viewmodel.MainViewModel;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainViewModel.class);
        trackAdapter = new TrackAdapter(this);

        viewModel.getAllTracks().observe(this, tracks -> {
            //Log.d("psylha", "observing tracks");
            trackAdapter.setData(tracks);
        });

        viewModel.getAllTrackPoints().observe(this, trackPoints -> {
            //Log.d("psylha", "observing trackpoints");
        });

        viewModel.getAllTrackToPoints().observe(this, trackToPoints -> {
            //Log.d("psylha", "observing tracktopoints");
        });

        RecyclerView recyclerView = findViewById(R.id.trackRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trackAdapter);

        trackAdapter.setClickListener(new TrackAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                //Log.d("psylha", "recycler click at " + position);
                onRecyclerClick(view, position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        viewModel.getAllTracks().removeObservers(this);
        viewModel.getAllTrackPoints().removeObservers(this);
        viewModel.getAllTrackToPoints().removeObservers(this);
        super.onDestroy();
    }

    public void onRecyclerClick(View v, int position) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(this, ViewTrack.class);
        intent.putExtra("track_id", (int) trackAdapter.getItemId(position));
        startActivity(intent);
    }

    public void onNewTrackClick(View v) {
        Intent intent = new Intent(this, NewTrack.class);
        intent.putExtra("track",
                viewModel.getAllTracks().getValue().get(viewModel.getAllTracks().getValue().size()-1).getId() + 1);
        intent.putExtra("trackpoint",
                viewModel.getAllTrackPoints().getValue().get(viewModel.getAllTrackPoints().getValue().size()-1).getId() + 1);
        startActivity(intent);
    }
}
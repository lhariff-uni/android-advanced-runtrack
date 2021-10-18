

package com.example.runningtracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningtracker.adapter.PointAdapter;
import com.example.runningtracker.R;
import com.example.runningtracker.viewmodel.ViewTrackViewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class ViewTrack extends AppCompatActivity {

    private ViewTrackViewModel vm;
    private PointAdapter pointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_track);

        vm = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ViewTrackViewModel.class);
        try {
            vm.init(getIntent().getExtras().getInt("track_id"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        EditText name = findViewById(R.id.trackTitle);
        EditText description = findViewById(R.id.trackDescription);
        TextView distance = findViewById(R.id.trackDistanceData);
        TextView time = findViewById(R.id.trackTimeData);
        TextView date = findViewById(R.id.trackDateData);
        TextView start = findViewById(R.id.trackStartTimeData);
        TextView end = findViewById(R.id.trackEndTimeData);
        TextView pace = findViewById(R.id.trackPaceData);

        name.setText(vm.getName());
        description.setText(vm.getDescription());
        distance.setText(String.valueOf(vm.getDistance()));
        time.setText(vm.getTime());
        date.setText(vm.getDate());
        start.setText(vm.getStartTime());
        end.setText(vm.getEndTime());
        pace.setText(String.valueOf(vm.getPace()));

        pointAdapter = new PointAdapter(this);
        pointAdapter.setData(vm.getTrackPoints());

        RecyclerView recyclerView = findViewById(R.id.trackPointRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pointAdapter);
    }

    public void onDeleteClick(View v) {
        vm.delete();
        finish();
    }

    public void onFinishClick(View v) {
        EditText name = findViewById(R.id.trackTitle);
        EditText desc = findViewById(R.id.trackDescription);
        vm.finish(String.valueOf(name.getText()), String.valueOf(desc.getText()));
        finish();
    }
}
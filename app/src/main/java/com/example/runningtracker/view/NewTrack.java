package com.example.runningtracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningtracker.MyLocationListener;
import com.example.runningtracker.adapter.PointAdapter;
import com.example.runningtracker.R;
import com.example.runningtracker.RunService;
import com.example.runningtracker.entity.TrackPoint;
import com.example.runningtracker.viewmodel.NewTrackViewModel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class NewTrack extends AppCompatActivity {

    private NewTrackViewModel vm;
    private PointAdapter pointAdapter;

    private NotificationManager notificationManager;

    private Messenger messenger;
    private Messenger replyMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_track);

        vm = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NewTrackViewModel.class);

        vm.init(getIntent().getIntExtra("track", 0),
                getIntent().getIntExtra("trackpoint", 0));

        pointAdapter = new PointAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.ntRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pointAdapter);

        EditText name = findViewById(R.id.ntName);

        TextView distance = findViewById(R.id.ntDistanceData);
        TextView time = findViewById(R.id.ntTimeData);
        TextView pace = findViewById(R.id.ntPaceData);

        final Observer<ArrayList<TrackPoint>> trackPointsObserver = new Observer<ArrayList<TrackPoint>>() {
            @Override
            public void onChanged(ArrayList<TrackPoint> trackPoints) {
                //Log.d("psylha", "trackpoints changed");
                pointAdapter.setData(trackPoints);
            }
        };

        final Observer<String> distanceObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //Log.d("psylha", "distance changed: " + s);
                distance.setText(s);
            }
        };

        final Observer<String> timeObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //Log.d("psylha", "time changed: " + s);
                time.setText(s);
            }
        };

        final Observer<String> paceObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //Log.d("psylha", "pace changed: " + s);
                pace.setText(s);
            }
        };

        vm.getTrackPoints().observe(this, trackPointsObserver);
        vm.getDistance().observe(this, distanceObserver);
        vm.getTimes().observe(this, timeObserver);
        vm.getPace().observe(this, paceObserver);

        replyMessenger = new Messenger(new MyHandler());
        this.bindService(new Intent(this, RunService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onDestroy() {
        //Log.d("psylha", "NewTrack: onDestroy");
        vm.getTrackPoints().removeObservers(this);
        vm.getDistance().removeObservers(this);
        vm.getTimes().removeObservers(this);
        vm.getPace().removeObservers(this);

        if(serviceConnection!=null) {
            //Log.d("psylha", "MainActivity: onDestroy - unbind");
            this.unbindService(serviceConnection);
            serviceConnection = null;
        }

        super.onDestroy();
    }

    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Log.d("psylha", "MainActivity: onServiceConnected");
            messenger = new Messenger(service);
            Message message = Message.obtain(null, RunService.REGISTER, 0, 0);
            message.replyTo = replyMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //Log.d("psylha", "MainActivity: onServiceDisconnected");
            messenger = null;
            Message message = Message.obtain(null, RunService.UNREGISTER, 0, 0);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RunService.NEWLOCATION:
                    //Log.d("psylha", "* NewTrack: NEWLOCATION Received");
                    vm.newLocationPoint((Location) msg.obj);
                    notificationManager.notify(RunService.ONGOING_NOTIFICATION_ID, vm.getNotification());
                    break;
                case RunService.SENDNOTIF:
                    //Log.d("psylha", "* NewTrack: SENDNOTIF Received");
                    if(vm.getBuilder() == null) {
                        vm.setBuilder((NotificationCompat.Builder) msg.obj);
                    }
                    break;
                case RunService.SENDPENDINGINTENT:
                    //Log.d("psylha", "* NewTrack: SENDPENDINGINTENT Received");
                    vm.setPendingIntent((PendingIntent) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    public void onFinishClick(View v) {
        EditText name = findViewById(R.id.ntName);
        EditText desc = findViewById(R.id.ntDescription);
        vm.finish(String.valueOf(name.getText()), String.valueOf(desc.getText()));
        try {
            messenger.send(Message.obtain(null, RunService.UNREGISTER));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finish();
    }
}
package com.example.runningtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.runningtracker.view.NewTrack;

import java.util.ArrayList;

public class RunService extends Service implements LocationObserver {
    private Messenger messenger;
    private final ArrayList<Messenger> clientMessengers = new ArrayList<Messenger>();

    public static final int REGISTER = 0;
    public static final int UNREGISTER = 1;
    public static final int NEWLOCATION = 2;
    public static final int SENDNOTIF = 3;
    public static final int SENDPENDINGINTENT = 4;
    public static final int ONGOING_NOTIFICATION_ID = 1;

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Intent notificationIntent;

    private MyLocationListener locationListener;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(new MyHandler());

        notificationIntent = new Intent(this, NewTrack.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //  NotificationChannel setup
        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "RunService";
            String description = "Run";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("101", name,
                    importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        builder = new NotificationCompat.Builder(this, "101");
        builder.setContentTitle("Currently Tracking Run")
                .setContentText("Notification Description")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setSound(null)
                .setVibrate(null)
                .setTicker("Ticker Text");

        //  Notification ID cannot be 0
        startForeground(ONGOING_NOTIFICATION_ID, builder.build());

        //  LocationManager setup
        locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationListener.addListener(this::newLocationPoint);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5, // minimum time interval between updates
                    5, // minimum distance between updates, in metres
                    locationListener);
        } catch(SecurityException e) {
            Log.d("psylha", e.toString());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Log.d("psylha", "* RunService: onDestroy");
        locationListener.removeListener(this::newLocationPoint);
        locationManager.removeUpdates(locationListener);
        stopForeground(true);
        notificationManager.cancelAll();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void newLocationPoint(Location location) {
        //Log.d("psylha", "Location Recieved: " + location.getLatitude() + " " +
        //        location.getLongitude());
        Message message = Message.obtain(null, RunService.NEWLOCATION, location);
        Message message1 = Message.obtain(null, RunService.SENDPENDINGINTENT,
                PendingIntent.getActivity(this, (int) System.currentTimeMillis(),
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        for(Messenger clientMessenger : clientMessengers) {
            try {
                clientMessenger.send(message1); // send updated PendingIntent for Notification
                clientMessenger.send(message); // send last received Location from LocationListener
            } catch (RemoteException e) {
                clientMessengers.remove(clientMessenger);
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case RunService.REGISTER:
                    //Log.d("psylha", "* RunService: REGISTER Received");
                    clientMessengers.add(msg.replyTo);
                    Message message1 = Message.obtain(null, RunService.SENDNOTIF, builder);
                    try {
                        msg.replyTo.send(message1);
                    } catch (RemoteException e) {
                        clientMessengers.remove(msg.replyTo);
                    }
                    break;
                case RunService.UNREGISTER:
                    //Log.d("psylha", "* RunService: UNREGISTER Received");
                    clientMessengers.remove(msg.replyTo);
                    break;
            }
        }
    }
}



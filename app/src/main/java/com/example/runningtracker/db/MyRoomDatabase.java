package com.example.runningtracker.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.runningtracker.dao.TrackDao;
import com.example.runningtracker.dao.TrackPointDao;
import com.example.runningtracker.dao.TrackToPointDao;
import com.example.runningtracker.entity.Track;
import com.example.runningtracker.entity.TrackPoint;
import com.example.runningtracker.entity.TrackToPoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Track.class, TrackPoint.class, TrackToPoint.class}, version = 2, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract TrackDao trackDao();
    public abstract TrackPointDao trackPointDao();
    public abstract TrackToPointDao trackToPointDao();

    private static volatile MyRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (MyRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class, "runtrackerDB")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //Log.d("psylha", "dboncreate");
            databaseWriteExecutor.execute(() -> {
                TrackToPointDao trackToPointDao = INSTANCE.trackToPointDao();
                trackToPointDao.deleteAll();
                TrackDao trackDao = INSTANCE.trackDao();
                trackDao.deleteAll();
                TrackPointDao trackPointDao = INSTANCE.trackPointDao();
                trackPointDao.deleteAll();

                Track track = new Track("Run 1", "testing description");
                track.setEndTime(track.getStartTime() + 3600000);
                trackDao.insert(track);

                TrackPoint trackPoint = new TrackPoint(0.0, 0.0);
                trackPointDao.insert(trackPoint);
                trackPoint = new TrackPoint(5.1, 3.2);
                trackPointDao.insert(trackPoint);
                trackPoint = new TrackPoint(6.1, 3.4);
                trackPointDao.insert(trackPoint);

                TrackToPoint trackToPoint = new TrackToPoint(1,1);
                trackToPointDao.insert(trackToPoint);
                trackToPoint = new TrackToPoint(1,2);
                trackToPointDao.insert(trackToPoint);
                trackToPoint = new TrackToPoint(1,3);
                trackToPointDao.insert(trackToPoint);
            });
        }
    };
}

package com.example.runningtracker.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.runningtracker.dao.TrackDao;
import com.example.runningtracker.dao.TrackPointDao;
import com.example.runningtracker.dao.TrackToPointDao;
import com.example.runningtracker.db.MyRoomDatabase;
import com.example.runningtracker.entity.Track;
import com.example.runningtracker.entity.TrackPoint;

public class TrackProvider extends ContentProvider {

    private MyRoomDatabase database;

    private TrackDao trackDao;
    private TrackPointDao trackPointDao;
    private TrackToPointDao trackToPointDao;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TrackContract.AUTHORITY, "track", 1);
        uriMatcher.addURI(TrackContract.AUTHORITY, "track/#", 2);
        uriMatcher.addURI(TrackContract.AUTHORITY, "trackpoint", 3);
        uriMatcher.addURI(TrackContract.AUTHORITY, "trackpoint/#", 4);
        uriMatcher.addURI(TrackContract.AUTHORITY, "tracktopoint", 5);
        uriMatcher.addURI(TrackContract.AUTHORITY, "*", 6);
    }

    @Override
    public boolean onCreate() {
        database = Room.databaseBuilder(getContext(), MyRoomDatabase.class, "runtrackerDB").build();

        trackDao = database.trackDao();
        trackPointDao = database.trackPointDao();
        trackToPointDao = database.trackToPointDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case 2:
                return trackDao.getTrackCursor(Integer.parseInt(uri.getLastPathSegment()));
            case 1:
                return trackDao.getAllTracksCursor();
            case 4:
                return trackPointDao.getTrackPointCursor(Integer.parseInt(uri.getLastPathSegment()));
            case 3:
                return trackPointDao.getAllTrackPointsCursor();
            case 5:
                return trackToPointDao.getAllTrackToPointsCursor();
            case 6:
                Cursor[] cursors = new Cursor[3];
                cursors[0] = trackDao.getAllTracksCursor();
                cursors[1] = trackPointDao.getAllTrackPointsCursor();
                cursors[2] = trackToPointDao.getAllTrackToPointsCursor();
                MergeCursor mergeCursor = new MergeCursor(cursors);
                return mergeCursor;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String contentType;

        if (uri.getLastPathSegment()==null) {
            contentType = TrackContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = TrackContract.CONTENT_TYPE_SINGLE;
        }

        return contentType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Insert not implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch(uriMatcher.match(uri)) {
            case 2:
                trackDao.deleteTrack(Integer.parseInt(uri.getLastPathSegment()));
                return 0;
            case 1:
                trackDao.deleteAll();
                return 0;
            case 4:
                trackPointDao.deleteTrackPoint(Integer.parseInt(uri.getLastPathSegment()));
                return 0;
            case 3:
                trackPointDao.deleteAll();
                return 0;
            case 5:
                trackToPointDao.deleteAll();
                return 0;
            case 6:
                trackDao.deleteAll();
                trackPointDao.deleteAll();
                trackToPointDao.deleteAll();
                return 0;
            default:
                throw new IllegalArgumentException
                        ("Unknown URI:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update not implemented");
    }
}

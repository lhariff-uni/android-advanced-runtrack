package com.example.runningtracker.contentprovider;

import android.net.Uri;

public class TrackContract {

    public static final String AUTHORITY = "com.example.runningtracker.contentprovider.TrackProvider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri TRACK_URI = Uri.parse("content://" + AUTHORITY + "/track/");
    public static final Uri TRACKPOINT_URI = Uri.parse("content://" + AUTHORITY + "/trackpoint/");
    public static final Uri TRACKTOPOINT_URI = Uri.parse("content://" + AUTHORITY + "/tracktopoint/");
    public static final Uri ALL_URI = Uri.parse("content://" + AUTHORITY + "/");

    public static final String TRACK_ID = "_id";
    public static final String TRACK_NAME = "name";
    public static final String TRACK_DESCRIPTION = "description";
    public static final String TRACK_TOTALDISTANCE = "totalDistance";
    public static final String TRACK_STARTTIME = "startTime";
    public static final String TRACK_ENDTIME = "endTime";
    public static final String TRACK_PACE = "pace";

    public static final String TRACKPOINT_ID = "_id";
    public static final String TRACKPOINT_LATITUDE = "latitude";
    public static final String TRACKPOINT_LONGITUDE = "longitude";
    public static final String TRACKPOINT_TIME = "time";

    public static final String TRACKTOPOINT_TRACK_ID = "track_id";
    public static final String TRACKTOPOINT_TRACKPOINT_ID = "trackpoint_id";

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/track.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/track.data.text";
}

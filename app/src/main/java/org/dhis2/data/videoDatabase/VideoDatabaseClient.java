package org.dhis2.data.videoDatabase;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

import java.util.List;

public class VideoDatabaseClient extends AsyncTask<Object, Void, Object> {

    private int taskID;
    private final static String TAG = "dbClientAsync";

    private Context context;

    public VideoDatabaseClient(Context context, int taskID) {
        this.context = context;
        this.taskID = taskID;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {
        Log.i("button4", "We should do database stuff now!");

        switch (this.taskID) {
            case 0: //get all videos in DB
                List<StoredVideoEntity> videos = VideoDatabaseInstance.getInstance(context).getVideoDatabase()
                        .storedVideoDao().getAll();
                return videos;
            case 1: //insert a video.
                StoredVideoEntity videoEntity = (StoredVideoEntity) params[0];
                VideoDatabaseInstance.getInstance(context).getVideoDatabase()
                        .storedVideoDao().insert(videoEntity);
                break;
            case 2: //delete all entries
                VideoDatabaseInstance.getInstance(context).getVideoDatabase()
                        .storedVideoDao().deleteAll();
            default:
                break;
        }
        return null;
    }
}

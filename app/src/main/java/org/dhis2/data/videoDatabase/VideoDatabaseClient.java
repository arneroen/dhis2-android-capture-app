package org.dhis2.data.videoDatabase;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;
import org.dhis2.data.videoDatabase.entities.VideoLanguageEntity;

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

        VideoDatabaseInstance dbInstance = VideoDatabaseInstance.getInstance(context);

        switch (this.taskID) {
            case 0: //get all videos in DB
                List<StoredVideoEntity> videos = dbInstance.getVideoDatabase()
                        .storedVideoDao().getAll();

                for (StoredVideoEntity video : videos) {
                    List<VideoLanguageEntity> videoLanguages = dbInstance.getVideoDatabase()
                            .videoLanguageDao().getAll(video.getUid());

                    video.setVideoLanguages(videoLanguages);
                }
                return videos;
            case 1: //insert a video.
                StoredVideoEntity videoEntity = (StoredVideoEntity) params[0];


                dbInstance.getVideoDatabase()
                        .storedVideoDao().insert(videoEntity);
                dbInstance.getVideoDatabase()
                        .videoLanguageDao().insertAll(videoEntity.getVideoLanguages());
                break;
            case 2: //delete all entries
                dbInstance.getVideoDatabase()
                        .storedVideoDao().deleteAll();
            default:
                break;
        }
        return null;
    }
}

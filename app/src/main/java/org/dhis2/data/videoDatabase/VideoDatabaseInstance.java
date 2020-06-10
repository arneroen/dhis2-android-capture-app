package org.dhis2.data.videoDatabase;

import android.content.Context;
import androidx.room.Room;

public class VideoDatabaseInstance {
    private static VideoDatabaseInstance dbInstance;
    private VideoDatabase videoDatabase;

    private VideoDatabaseInstance(Context context) {
        videoDatabase = Room.databaseBuilder(context, VideoDatabase.class, "video-database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized VideoDatabaseInstance getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new VideoDatabaseInstance(context);
        }
        return dbInstance;
    }

    public VideoDatabase getVideoDatabase() { return videoDatabase; }
}

package org.dhis2.data.videoDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.dhis2.data.videoDatabase.daos.StoredVideoDao;
import org.dhis2.data.videoDatabase.daos.VideoLanguageDao;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;
import org.dhis2.data.videoDatabase.entities.VideoLanguageEntity;

@Database(entities = {StoredVideoEntity.class, VideoLanguageEntity.class}, version = 5)
public abstract class VideoDatabase extends RoomDatabase {
    public abstract StoredVideoDao storedVideoDao();
    public abstract VideoLanguageDao videoLanguageDao();
}

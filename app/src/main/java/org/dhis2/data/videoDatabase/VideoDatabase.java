package org.dhis2.data.videoDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.dhis2.data.videoDatabase.daos.StoredVideoDao;
import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

@Database(entities = {StoredVideoEntity.class}, version = 1)
public abstract class VideoDatabase extends RoomDatabase {
    public abstract StoredVideoDao storedVideoDao();
}

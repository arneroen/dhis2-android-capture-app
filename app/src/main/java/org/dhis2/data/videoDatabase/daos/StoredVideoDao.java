package org.dhis2.data.videoDatabase.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;

import java.util.List;

@Dao
public interface StoredVideoDao {
    @Query("SELECT * FROM storedVideoEntity")
    List<StoredVideoEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoredVideoEntity storedVideoEntity);

    @Query("DELETE FROM storedvideoentity")
    void deleteAll();
}

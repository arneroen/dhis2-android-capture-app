package org.dhis2.data.videoDatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.dhis2.data.videoDatabase.entities.VideoLanguageEntity;

import java.util.List;

@Dao
public interface VideoLanguageDao {
    @Query("SELECT * FROM videoLanguageEntity")
    VideoLanguageEntity[] getAll();

    @Query("SELECT * FROM videoLanguageEntity WHERE video_uid == :uid")
    List<VideoLanguageEntity> getAll(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoLanguageEntity videoLanguageEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VideoLanguageEntity> videoLanguageEntities);

    @Delete
    void delete(VideoLanguageEntity videoLanguageEntity);

}

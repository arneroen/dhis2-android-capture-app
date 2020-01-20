package org.dhis2.data.videoDatabase.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

@Entity
@JsonAdapter(StoredVideoEntityDeserializer.class)
public class StoredVideoEntity {

    @NonNull
    @PrimaryKey
    public String uid;

    @NonNull
    @ColumnInfo(name = "video_title")
    public String videoTitle;

    @NonNull
    @ColumnInfo(name = "file_name")
    public String fileName;

    @ColumnInfo(name = "description")
    public String description;

    @NonNull
    @ColumnInfo(name = "version")
    public int version;

    @Ignore
    public List<VideoLanguageEntity> videoLanguages;

    public StoredVideoEntity(String uid, String videoTitle, String fileName, String description, int version) {
        this.uid = uid;
        this.videoTitle = videoTitle;
        this.fileName = fileName;
        this.description = description;
        this.version = version;
    }
    public StoredVideoEntity(String uid, String videoTitle, String fileName, String description, int version, List<VideoLanguageEntity> videoLanguages) {
        this.uid = uid;
        this.videoTitle = videoTitle;
        this.fileName = fileName;
        this.videoLanguages = videoLanguages;
        this.description = description;
        this.version = version;
    }

    public String getUid() { return uid; }

    public String getVideoTitle() { return videoTitle; }

    public String getFileName() { return fileName; }

    public List<VideoLanguageEntity> getVideoLanguages() { return videoLanguages; }

    public String getDescription() { return description; }


    public int getVersion() { return version; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public void setUid(String uid) { this.uid = uid; }

    public void setVideoLanguages(List<VideoLanguageEntity> videoLanguages) {
        this.videoLanguages = videoLanguages;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof StoredVideoEntity)) return false;
        return (this.uid.equals(((StoredVideoEntity) other).getUid()) && this.version == ((StoredVideoEntity) other).getVersion());
    }
}

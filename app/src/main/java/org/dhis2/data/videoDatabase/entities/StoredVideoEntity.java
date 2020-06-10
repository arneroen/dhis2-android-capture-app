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

    @ColumnInfo(name = "thumbnail_file_name")
    public String thumbnailFileName;

    public String tags;

    @Ignore
    public List<VideoLanguageEntity> videoLanguages;

    public StoredVideoEntity(String uid, String videoTitle, String fileName, String description, int version, String thumbnailFileName, String tags) {
        this.uid = uid;
        this.videoTitle = videoTitle;
        this.fileName = fileName;
        this.description = description;
        this.version = version;
        this.thumbnailFileName = thumbnailFileName;
        this.tags = tags;
    }
    public StoredVideoEntity(String uid, String videoTitle, String fileName, String description, int version, String thumbnailFileName, String tags, List<VideoLanguageEntity> videoLanguages) {
        this.uid = uid;
        this.videoTitle = videoTitle;
        this.fileName = fileName;
        this.description = description;
        this.version = version;
        this.thumbnailFileName = thumbnailFileName;
        this.tags = tags;
        this.videoLanguages = videoLanguages;
    }

    public String getUid() { return uid; }

    public String getVideoTitle() { return videoTitle; }

    public String getFileName() { return fileName; }

    public List<VideoLanguageEntity> getVideoLanguages() { return videoLanguages; }

    public String getDescription() { return description; }

    public String getThumbnailFileName() {
        return thumbnailFileName;
    }

    public int getVersion() { return version; }

    public String getTags() {
        return tags;
    }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public void setUid(String uid) { this.uid = uid; }

    public void setVideoLanguages(List<VideoLanguageEntity> videoLanguages) {
        this.videoLanguages = videoLanguages;
    }

    public void setThumbnailFileName(String thumnailFileName) {
        this.thumbnailFileName = thumnailFileName;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof StoredVideoEntity)) return false;
        return (this.uid.equals(((StoredVideoEntity) other).getUid()) && this.version == ((StoredVideoEntity) other).getVersion());
    }
}

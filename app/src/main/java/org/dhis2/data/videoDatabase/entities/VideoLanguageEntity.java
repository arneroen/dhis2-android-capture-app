package org.dhis2.data.videoDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

@Entity(foreignKeys = @ForeignKey(entity = StoredVideoEntity.class, parentColumns = "uid", childColumns = "video_uid", onDelete = ForeignKey.CASCADE),
        primaryKeys = {"video_uid", "language_name"})
public class VideoLanguageEntity {

    @NotNull
    @ColumnInfo(name = "video_uid")
    private String videoUid;

    @NotNull
    @ColumnInfo(name = "language_name")
    private String languageName;

    @NotNull
    private int index;

    public VideoLanguageEntity(String videoUid, String languageName, int index) {
        this.videoUid = videoUid;
        this.languageName = languageName;
        this.index = index;
    }

    public String getVideoUid() {
        return videoUid;
    }

    public String getLanguageName() {
        return languageName;
    }

    public int getIndex() {
        return index;
    }

}

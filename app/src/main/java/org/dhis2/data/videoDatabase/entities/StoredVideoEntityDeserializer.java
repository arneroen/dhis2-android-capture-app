package org.dhis2.data.videoDatabase.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.dhis2.data.videoDatabase.entities.StoredVideoEntity;
import org.dhis2.data.videoDatabase.entities.VideoLanguageEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoredVideoEntityDeserializer implements JsonDeserializer<StoredVideoEntity> {
    @Override
    public StoredVideoEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray jsonLanguages = jsonObject.get("videoLanguages").getAsJsonArray();

        List<VideoLanguageEntity> languages = new ArrayList<>();
        jsonLanguages.get(0).getAsJsonObject();

        for (int i = 0; i < jsonLanguages.size(); i++) {
            languages.add(new VideoLanguageEntity(
                    jsonObject.get("uid").getAsString(),
                    jsonLanguages.get(i).getAsJsonObject().get("languageName").getAsString(),
                    jsonLanguages.get(i).getAsJsonObject().get("index").getAsInt()

            ));
        }

        return new StoredVideoEntity(
                jsonObject.get("uid").getAsString(),
                jsonObject.get("videoTitle").getAsString(),
                jsonObject.get("fileName").getAsString(),
                jsonObject.get("description").getAsString(),
                jsonObject.get("version").getAsInt(),
                jsonObject.get("thumbnailFileName").getAsString(),
                jsonObject.get("tags").getAsString(),
                languages
        );
    }
}

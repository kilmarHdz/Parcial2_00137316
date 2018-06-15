package com.cabrera.parcial2_00137316.API;

import com.cabrera.parcial2_00137316.API.Res.NewRes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NewsDes implements JsonDeserializer<NewRes> {
    @Override
    public NewRes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        NewRes notice = new NewRes();

        JsonObject repoJsonObject = json.getAsJsonObject();
        notice.set_id(repoJsonObject.get("_id").getAsString());
        notice.setTitle(repoJsonObject.get("title").getAsString());
        notice.setBody(repoJsonObject.get("body").getAsString());
        notice.setGame(repoJsonObject.get("game").getAsString());
        notice.setCreated_date(repoJsonObject.get("created_date").getAsString());
        if(repoJsonObject.get("coverImage")!=null) {
            notice.setCoverImage(repoJsonObject.get("coverImage").getAsString());
        }else{
            notice.setCoverImage("http://");
        }
        if(repoJsonObject.get("description")!=null) {
            notice.setDescription(repoJsonObject.get("description").getAsString());
        }else{
            notice.setDescription("--*--");
        }

        return notice;
    }
}

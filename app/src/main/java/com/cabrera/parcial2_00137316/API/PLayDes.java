package com.cabrera.parcial2_00137316.API;

import com.cabrera.parcial2_00137316.API.Res.PlayRes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PLayDes implements JsonDeserializer<PlayRes> {
    @Override
    public PlayRes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PlayRes response = new PlayRes();
        JsonObject object = json.getAsJsonObject();
        response.set_id(object.get("_id").getAsString());
        if(object.get("name")!=null){
            response.setName(object.get("name").getAsString());
        }else{
            response.setName("--*--");
        }
        if(object.get("biografia")!=null){
            response.setBiografia(object.get("biografia").getAsString());
        }else{
            response.setBiografia("--*--");
        }
        if(object.get("avatar")!=null){
            response.setAvatar(object.get("avatar").getAsString());
        }else{
            response.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9tu041mqm6g3UrEvWgGjfjZfn4OXsGfiffQDB82-Psxa52L3AA");
        }
        response.setGame(object.get("game").getAsString());

        return response;
    }
}
package com.cabrera.parcial2_00137316.API;

import com.cabrera.parcial2_00137316.API.Res.UserRes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UserDes implements JsonDeserializer<UserRes> {
    @Override
    public UserRes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        UserRes user = new UserRes();
        JsonObject object = json.getAsJsonObject();


        user.set_id(object.get("_id").getAsString());
        user.setUsername(object.get("user").getAsString());
        user.setPassword(object.get("password").getAsString());
        if(object.get("avatar")!=null){
            if(!object.get("avatar").equals("")) {
                user.setAvatar(object.get("avatar").getAsString());
            }else{
                user.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9tu041mqm6g3UrEvWgGjfjZfn4OXsGfiffQDB82-Psxa52L3AA");
            }
        }else{
            user.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9tu041mqm6g3UrEvWgGjfjZfn4OXsGfiffQDB82-Psxa52L3AA");
        }
        if(object.get("created_date")!=null) {
            user.setCreateDate(object.get("created_date").getAsString());
        }

        return user;
    }
}
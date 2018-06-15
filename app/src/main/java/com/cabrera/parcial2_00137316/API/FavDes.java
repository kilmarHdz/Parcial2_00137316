package com.cabrera.parcial2_00137316.API;

import android.util.Log;

import com.cabrera.parcial2_00137316.API.Res.FavRes;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavDes implements JsonDeserializer<FavRes> {

    @Override
    public FavRes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        final JsonArray jsonNewsID = object.get("favoriteNews").getAsJsonArray();
        final List<String> favoritesNewsID = new ArrayList<>();

        for(int i=0;i<jsonNewsID.size();i++){
            final JsonElement favorite = jsonNewsID.get(i);
            favoritesNewsID.add(favorite.getAsString());
            Log.d("ID_FAV",favorite.getAsString());
        }

        /*final NewsResponse[] favListResponse = context.deserialize(object.get("favoriteNews"),NewsResponse[].class);
        final List<String> favoritesNewsID = new ArrayList<>();

        for(NewsResponse response:favListResponse){
            favoritesNewsID.add(response.get_id());
        }*/

        return new FavRes(favoritesNewsID);
    }
}

package com.cabrera.parcial2_00137316.API.Res;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavRes {
    @SerializedName("_id")
    private List<String> _id;

    public FavRes(List<String> _id) {
        this._id = _id;
    }

    public List<String> get_id() {
        return _id;
    }
}

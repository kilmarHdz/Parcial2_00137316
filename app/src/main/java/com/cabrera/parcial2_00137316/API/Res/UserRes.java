package com.cabrera.parcial2_00137316.API.Res;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class UserRes {
    @SerializedName("_id")
    private String _id;
    @SerializedName("user")
    private String username;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("password")
    private String password;
    @SerializedName("created_date")
    private String createDate;


    public UserRes() {
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setAvatar(@NonNull String avatar) {
        this.avatar = avatar;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setCreateDate(@NonNull String createDate) {
        this.createDate = createDate;
    }


    public String get_id() {
        return _id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getAvatar() {
        return avatar;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getCreateDate() {
        return createDate;
    }

}

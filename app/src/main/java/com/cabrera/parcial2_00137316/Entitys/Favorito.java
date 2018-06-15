package com.cabrera.parcial2_00137316.Entitys;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites_table")
public class Favorito {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String _id;

    public Favorito(@NonNull String _id) {
        this._id = _id;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "_id='" + _id + '\'' +
                '}';
    }
}
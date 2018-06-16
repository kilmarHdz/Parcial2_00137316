package com.cabrera.parcial2_00137316.Interfaz;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cabrera.parcial2_00137316.Entitys.Favorito;

import java.util.List;

@Dao
public interface FavDao {
    @Query("SELECT * FROM favorites_table")
    LiveData<List<Favorito>> getAllFavorite();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Favorito favorite);

    @Query("DELETE FROM favorites_table")
    void deleteAll();
}
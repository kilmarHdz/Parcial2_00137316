package com.cabrera.parcial2_00137316.Interfaz;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cabrera.parcial2_00137316.Entitys.Players;

import java.util.List;

@Dao
public interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayer(Players player);

    @Query("SELECT * FROM player_table")
    LiveData<List<Players>> getAllPlayer();

    @Query("SELECT * FROM player_table WHERE game = :game")
    LiveData<List<Players>> getAllPlayerByGame(String game);

}
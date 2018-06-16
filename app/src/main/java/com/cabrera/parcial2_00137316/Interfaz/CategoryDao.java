package com.cabrera.parcial2_00137316.Interfaz;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cabrera.parcial2_00137316.Entitys.Categoria;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category_table")
    LiveData<List<Categoria>> getAllCategories();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Categoria categoryGame);
}
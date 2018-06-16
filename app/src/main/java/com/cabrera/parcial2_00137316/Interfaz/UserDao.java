package com.cabrera.parcial2_00137316.Interfaz;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cabrera.parcial2_00137316.Entitys.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_table")
    LiveData<User> getCurrentUser();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
    @Update
    void modifyUser(User... users);
    @Query("SELECT * FROM user_table WHERE username = :username")
    User getUser(String username);
    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();
    @Query("DELETE FROM user_table")
    void deleteAllUser();
}

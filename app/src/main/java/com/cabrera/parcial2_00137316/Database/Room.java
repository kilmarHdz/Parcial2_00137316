package com.cabrera.parcial2_00137316.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.cabrera.parcial2_00137316.Entitys.Categoria;
import com.cabrera.parcial2_00137316.Entitys.Favorito;
import com.cabrera.parcial2_00137316.Entitys.News;
import com.cabrera.parcial2_00137316.Entitys.Players;
import com.cabrera.parcial2_00137316.Entitys.User;
import com.cabrera.parcial2_00137316.Interfaz.CategoryDao;
import com.cabrera.parcial2_00137316.Interfaz.FavDao;
import com.cabrera.parcial2_00137316.Interfaz.NewDao;
import com.cabrera.parcial2_00137316.Interfaz.PlayerDao;
import com.cabrera.parcial2_00137316.Interfaz.UserDao;

@Database(entities = {News.class, User.class, Players.class, Categoria.class, Favorito.class},version = 1)
public abstract class Room extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract NewDao newDao();
    public abstract PlayerDao playerDao();
    public abstract CategoryDao categoryGameDao();
    public abstract FavDao favoriteDAO();


    private static Room INSTANCE;

    public static Room getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (Room.class){
                if(INSTANCE==null){
                    INSTANCE = android.arch.persistence.room.Room.databaseBuilder(context.getApplicationContext(),Room.class,"game_news_db")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new CleanUserCache(INSTANCE).execute();
        }
    };

    private static class CleanUserCache extends AsyncTask<User,Void,Void> {

        private UserDao userDao;
        public CleanUserCache(Room db){
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteAllUser();
            return null;
        }
    }
}
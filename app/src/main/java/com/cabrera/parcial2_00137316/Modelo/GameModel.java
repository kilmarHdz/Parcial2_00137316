package com.cabrera.parcial2_00137316.Modelo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.cabrera.parcial2_00137316.Entitys.Favorito;
import com.cabrera.parcial2_00137316.Entitys.News;
import com.cabrera.parcial2_00137316.Entitys.Players;
import com.cabrera.parcial2_00137316.Entitys.User;
import com.cabrera.parcial2_00137316.Entitys.Categoria;
import com.cabrera.parcial2_00137316.Repository.Repository;

import java.util.List;

public class GameModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<News>> newList;
    private LiveData<List<Players>> playerList;
    private LiveData<User> currentUser;

    public GameModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        newList = repository.getAllNews();
        playerList = repository.getAllPlayer();
    }

    public void refreshNews(){
        repository.refreshNews();
    }
    public void refreshNewsListID(){
        repository.refreshFavoritesListID();
    }
    public void refreshTopPlayers(){
        repository.refreshTopPlayers();}
    public void refreshCurrentUser(){
        repository.refreshCurrentUser();
    }

    public void deleteAllUsers(){
        repository.deleteAllUsers();
    }

    public LiveData<List<News>> getFavoriteObjectNews(){
        return repository.getFavoritesObjectNews();
    }
    public void updateNewFaState(String value,String idNew){
        repository.updateFavNewState(value,idNew);
    }

    public void addFavoriteNew(String idUser,String idNew){
        repository.addFavoriteNew(idUser,idNew);
    }
    public void removeFavoriteNew(String idUser,String idNew){
        repository.removeFavoriteNew(idUser,idNew);
    }
    public void addToFavList(Favorito favorite){
        repository.exectInserFavorite(favorite);
    }

    public LiveData<List<Favorito>> getFavorieList(){
        return repository.getAllFavorites();
    }
    public LiveData<User> getCurrentUser(){
        currentUser = repository.getCurrentUser();
        return currentUser;
    }

    public LiveData<List<News>> getAllNews() {
        return newList;
    }

    public LiveData<List<News>> getNewsByGame(String game){
        newList = repository.getNewsByGame(game);
        return newList;
    }
    public LiveData<News> getNew(String id){
        return repository.getNew(id);
    }

    public LiveData<List<Players>> getPlayersByGame(String game){
        playerList = repository.getPlayersByGame(game);
        return playerList;
    }
    public LiveData<List<Players>> getAllPlayers(){
        playerList = repository.getAllPlayer();
        return playerList;
    }

    public LiveData<List<Categoria>> getGameList() {
        LiveData<List<Categoria>> gameList = repository.getAllGames();
        return gameList;
    }
}

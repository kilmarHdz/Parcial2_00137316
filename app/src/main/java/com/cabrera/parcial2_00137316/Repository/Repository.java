package com.cabrera.parcial2_00137316.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.cabrera.parcial2_00137316.API.FavDes;
import com.cabrera.parcial2_00137316.API.NewsAPI;
import com.cabrera.parcial2_00137316.API.NewsDes;
import com.cabrera.parcial2_00137316.API.PLayDes;
import com.cabrera.parcial2_00137316.API.Res.FavRes;
import com.cabrera.parcial2_00137316.API.Res.NewRes;
import com.cabrera.parcial2_00137316.API.Res.PlayRes;
import com.cabrera.parcial2_00137316.API.Res.UserRes;
import com.cabrera.parcial2_00137316.API.UserDes;
import com.cabrera.parcial2_00137316.Activity.MainActivity;
import com.cabrera.parcial2_00137316.Database.Room;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private NewsAPI api;
    private CompositeDisposable disposable = new CompositeDisposable();
    private UserDao userDao;
    private NewDao newDao;
    private PlayerDao playerDao;
    private CategoryDao gameDao;
    private FavDao favoriteDAO;

    private LiveData<List<User>> userList;
    private LiveData<List<News>> newList;
    private LiveData<List<Players>> playerList;
    private LiveData<List<Categoria>> gameList;
    private LiveData<User> currentUser;
    private LiveData<List<Favorito>> favoriteList;

    public Repository(Application application){
        Room db = Room.getDatabase(application);
        userDao = db.userDao();
        newDao = db.newDao();
        playerDao = db.playerDao();
        gameDao = db.categoryGameDao();
        favoriteDAO = db.favoriteDAO();

        userList = userDao.getAllUsers();
        newList = newDao.getAllNews();
        playerList = playerDao.getAllPlayer();
        gameList = gameDao.getAllCategories();
        currentUser = userDao.getCurrentUser();
        favoriteList = favoriteDAO.getAllFavorite();
        createAPI();
    }

    /**
     *GETTERS
     */

    public LiveData<List<Favorito>> getAllFavorites(){
        return favoriteList;
    }
    public LiveData<User> getCurrentUser(){
        return currentUser;
    }
    public LiveData<List<Categoria>> getAllGames(){
        api = getGamesFromAPI();
        disposable.add(api.getGameList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getGameList()));
        return gameList;
    }
    public LiveData<News> getNew(String id){
        return newDao.getNew(id);
    }
    public LiveData<List<News>> getNewsByGame(String game){
        newList = newDao.getNewsByCategory(game);
        return newList;
    }
    public LiveData<List<Players>> getPlayersByGame(String game){
        playerList = playerDao.getAllPlayerByGame(game);
        return playerList;
    }
    public LiveData<List<Players>> getAllPlayer(){
        return playerList;
    }
    public LiveData<List<User>> getAllUsers(){
        return userList;
    }
    public LiveData<List<News>> getAllNews() {
        return newList;
    }
    public LiveData<List<News>> getFavoritesObjectNews(){
        return newDao.getFavoritesNews();
    }

    /**
     * SETTERS
     */

    public void addFavoriteNew(String idUser,String idNew){
        api = createAddFavRequest();
        disposable.add(api.addFavorite(idUser,idNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(addFavObserver()));
    }
    public void removeFavoriteNew(String User,String idNew){
        api = createAddFavRequest();
        disposable.add(api.removeFavorite(User,idNew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(removeFavObserver()));
    }
    public void exectInserFavorite(Favorito fab){
        insertFavorite(fab);
    }
    /**
     * Metodos para obtener informacion de la API
     */
    public void refreshCurrentUser(){
        api = getCurrentUserByRepo();
        disposable.add(api.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getUserLogged()));
    }
    public void refreshNews(){
        disposable.add(api.getNewsByRepo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getNewsRepoObserver()));
    }
    public void refreshFavoritesListID(){
        api = getFavoritesNoticesByRepo();
        disposable.add(api.getFavoritesListUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getFavoritesObserver()));
    }
    public void refreshTopPlayers(){
        api = getPlayersFromAPI();
        disposable.add(api.getAllPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getPlayerRepoResponse()));
    }


    /**
     *METODOS QUE EJECTUTAN LOS THREADS
     */
    public void insertGame(Categoria game){
        new categoryInsertAsyncTask(gameDao).execute(game);
    }

    public void insertUser(User user){
        new userInsertAsyncTask(userDao).execute(user);
    }

    public void insertNews(News news){
        new newsInsertAsyncTask(newDao).execute(news);
    }

    public void insertPlayer(Players player){
        new playerInsertAsyncTask(playerDao).execute(player);
    }

    private void insertFavorite(Favorito fab) {
        new favoritesInsertAsyncTask(favoriteDAO).execute(fab);
    }

    public void updateFavNewState(String value, String idNew){
        new favoritesUpdateAsyncTask(newDao).execute(value,idNew);
    }
    public void deleteAllFavotitesID(){
        new deleteAllFavotitesIDAsyncTask(favoriteDAO).execute();
    }

    public void deleteAllUsers(){
        new deleteAllUsersAsyncTask(userDao).execute();
    }





    /**
     * CREACION DE THREADS ENCARGADOS DE LA INSERCION DE DATOS EN LA BASE DE DATOS
     */
    private static class categoryInsertAsyncTask extends AsyncTask<Categoria,Void,Void> {
        private CategoryDao gameDao;

        public categoryInsertAsyncTask(CategoryDao gameDao){
            this.gameDao = gameDao;
        }
        @Override
        protected Void doInBackground(Categoria... categoryGames) {
            gameDao.insertCategory(categoryGames[0]);
            return null;
        }
    }
    private static class playerInsertAsyncTask extends AsyncTask<Players,Void,Void>{
        private PlayerDao playerDao;

        public playerInsertAsyncTask(PlayerDao playerDao){
            this.playerDao = playerDao;
        }
        @Override
        protected Void doInBackground(Players... players) {
            playerDao.insertPayer(players[0]);
            return null;
        }
    }
    private static class userInsertAsyncTask extends AsyncTask<User,Void,Void>{
        private UserDao userDao;

        public userInsertAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }
    private  static class newsInsertAsyncTask extends AsyncTask<News,Void,Void>{
        private NewDao newDao;

        public newsInsertAsyncTask(NewDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(News... news) {
            newDao.insertNew(news[0]);
            return null;
        }
    }
    private static class favoritesInsertAsyncTask extends AsyncTask<Favorito,Void,Void>{

        private FavDao favoriteDAO;

        public favoritesInsertAsyncTask(FavDao favoriteDAO){
            this.favoriteDAO = favoriteDAO;
        }

        @Override
        protected Void doInBackground(Favorito... favorites) {
            favoriteDAO.insertFavorite(favorites[0]);
            return null;
        }
    }
    private static class favoritesUpdateAsyncTask extends AsyncTask<String,Void,Void>{
        private NewDao dao;

        public favoritesUpdateAsyncTask(NewDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... values) {
            Log.d("UPDATE_VALUES",values[0] +" "+ values[1]);
            dao.updateFavoriteState(Integer.parseInt(values[0]),values[1]);
            return null;
        }
    }

    private static class deleteAllFavotitesIDAsyncTask extends AsyncTask<Void,Void,Void>{
        private FavDao dao;
        public deleteAllFavotitesIDAsyncTask(FavDao dao){
            this.dao =dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
    private static class deleteAllUsersAsyncTask extends AsyncTask<Void,Void,Void>{
        private UserDao userDao;
        public deleteAllUsersAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUser();
            return null;
        }
    }



    private void createAPI(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(NewRes.class,new NewsDes())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(NewsAPI.class);

    }
    private DisposableSingleObserver<List<NewRes>> getNewsRepoObserver(){
        return new DisposableSingleObserver<List<NewRes>>() {
            @Override
            public void onSuccess(List<NewRes> news) {
                if(!news.isEmpty()){
                    for(NewRes notice:news){
                        News newNotice = new News();
                        newNotice.set_id(notice.get_id());
                        newNotice.setTitle(notice.getTitle());
                        newNotice.setDescription(notice.getDescription());
                        newNotice.setCoverImage(notice.getCoverImage());
                        newNotice.setBody(notice.getBody());
                        newNotice.setCreated_date(notice.getCreated_date());
                        newNotice.setGame(notice.getGame());
                        insertNews(newNotice);
                    }
                    refreshFavoritesListID();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO_USER: ",e.getMessage());
            }
        };
    }

    private NewsAPI getPlayersFromAPI(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlayRes.class,new PLayDes())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(NewsAPI.class);
        return api;
    }
    private DisposableSingleObserver<List<PlayRes>> getPlayerRepoResponse(){
        return new DisposableSingleObserver<List<PlayRes>>() {
            @Override
            public void onSuccess(List<PlayRes> players) {
                if(!players.isEmpty()){
                    for(PlayRes player:players){
                        Players p = new Players();
                        p.set_id(player.get_id());
                        p.setAvatar(player.getAvatar());
                        p.setBiografia(player.getBiografia());
                        p.setGame(player.getGame());
                        p.setName(player.getName());
                        insertPlayer(p);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_REPO",e.getMessage());
            }
        };
    }

    private NewsAPI getGamesFromAPI(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NewsAPI.class);
    }
    private DisposableSingleObserver<List<String>> getGameList(){
        return new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> games) {
                if(!games.isEmpty()){
                    for(String game:games){
                        insertGame(new Categoria(game));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR_GAME_LIST",e.getMessage());
            }
        };
    }

    private NewsAPI getCurrentUserByRepo(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(UserRes.class,new UserDes())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(NewsAPI.class);
        return api;
    }
    private DisposableSingleObserver<UserRes> getUserLogged(){
        return new DisposableSingleObserver<UserRes>() {
            @Override
            public void onSuccess(UserRes value) {
                User user = new User();
                user.set_id(value.get_id());
                user.setUsername(value.getUsername());
                user.setAvatar(value.getAvatar());
                user.setPassword(value.getPassword());
                user.setCreateDate(value.getCreateDate());
                insertUser(user);
            }

            @Override
            public void onError(Throwable e) {
                //Log.d("ERROR_GET_USER",e.getMessage());
            }
        };
    }

    private NewsAPI getFavoritesNoticesByRepo(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(FavRes.class,new FavDes())
                .registerTypeAdapter(NewRes.class,new NewsDes())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(newRequest);
                    }

                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(NewsAPI.class);
        return api;
    }
    private DisposableSingleObserver<FavRes> getFavoritesObserver(){
        return new DisposableSingleObserver<FavRes>() {
            @Override
            public void onSuccess(FavRes values) {
                deleteAllFavotitesID();
                for(String value:values.get_id()){
                    insertFavorite(new Favorito(value));
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("FAVORITESIDREPO",e.getMessage());
            }
        };
    }

    private NewsAPI createAddFavRequest(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization","Bearer "+ MainActivity.securityToken.getTokenSecurity())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NewsAPI.class);
    }
    private DisposableSingleObserver<Void> addFavObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
    private DisposableSingleObserver<Void> removeFavObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

}
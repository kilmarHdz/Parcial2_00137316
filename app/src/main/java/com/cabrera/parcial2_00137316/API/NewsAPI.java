package com.cabrera.parcial2_00137316.API;

import com.cabrera.parcial2_00137316.API.Res.FavRes;
import com.cabrera.parcial2_00137316.API.Res.NewRes;
import com.cabrera.parcial2_00137316.API.Res.PlayRes;
import com.cabrera.parcial2_00137316.API.Res.UserRes;
import com.cabrera.parcial2_00137316.Entitys.Token;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NewsAPI {
    String ENDPOINT = "http://gamenewsuca.herokuapp.com";
    /** ENDSPOINTS ABOUT USERS **/

    /** @POST("/login") ENDPOINT
     @FormUrlEncoded ENCABEZADO
     Single<SecurityToken> getSecurityToken(@Field("user")String username,@Field("password")String password); **/
    @POST("/login")
    @FormUrlEncoded
    Single<Token> getSecurityToken(@Field("user")String username, @Field("password")String password);

    @POST("/users/{idUser}/fav")
    @FormUrlEncoded
    Single<Void> addFavorite(@Path("idUser")String idUser, @Field("new")String idNew);

    @HTTP(method = "DELETE",path = "/users/{idUser}/fav",hasBody = true)
    @FormUrlEncoded
    Single<Void> removeFavorite(@Path("idUser")String idUser,@Field("new")String idNew);

    @GET("/users/detail")
    Single<UserRes> getCurrentUser();

    @GET("/users/detail")
    Single<FavRes> getFavoritesListUser();

    /**ENDPOINTS ABOUT NEWS**/
    @GET("/news")
    Single<List<NewRes>> getNewsByRepo();

    @GET("/news/type/list")
    Single<List<String>> getGameList();

    /**ENDS POINTS ABOUT PLAYERS**/
    @GET("/players")
    Single<List<PlayRes>> getAllPlayers();




}

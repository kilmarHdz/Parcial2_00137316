package com.cabrera.parcial2_00137316.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabrera.parcial2_00137316.Adapter.AdapterNews;
import com.cabrera.parcial2_00137316.Entitys.News;
import com.cabrera.parcial2_00137316.Modelo.GameModel;
import com.cabrera.parcial2_00137316.R;

import java.util.List;

public class Fav extends Fragment {

    private FavoriteNewsTools tools;
    private List<News> favoritesNews;
    private GameModel model;
    private RecyclerView recyclerView;
    private AdapterNews adapter;
    private SwipeRefreshLayout refreshContent;


    public Fav() {
    }

    public static Fav newInstance(List<News> favoritesNews){
        Fav fragment = new Fav();
        fragment.setFavoritesNews(favoritesNews);
        return fragment;
    }

    public void setFavoritesNews(List<News> favoritesNews){
        this.favoritesNews = favoritesNews;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_games,container,false);
        recyclerView = view.findViewById(R.id.recyclerview_news_by_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshContent = view.findViewById(R.id.refreshContent);
        refreshContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tools.refreshFavorites();
                refreshContent.setRefreshing(false);
            }
        });
        adapter = new AdapterNews(getActivity());
        adapter.fillNews(favoritesNews);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public interface FavoriteNewsTools{
        void refreshFavorites();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FavoriteNewsTools){
            tools = (FavoriteNewsTools) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools=null;
    }
}
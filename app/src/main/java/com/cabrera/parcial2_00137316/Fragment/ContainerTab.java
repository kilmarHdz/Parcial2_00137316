package com.cabrera.parcial2_00137316.Fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabrera.parcial2_00137316.Adapter.AdapterNews;
import com.cabrera.parcial2_00137316.Adapter.AdapterPlayer;
import com.cabrera.parcial2_00137316.Adapter.ViewPagerAdapter;
import com.cabrera.parcial2_00137316.Entitys.News;
import com.cabrera.parcial2_00137316.Entitys.Players;
import com.cabrera.parcial2_00137316.Modelo.GameModel;
import com.cabrera.parcial2_00137316.R;

import java.util.List;

public class ContainerTab extends Fragment {

    private String game;
    private GameModel model;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private AdapterNews newsAdapter;
    private AdapterPlayer playersAdapter;


    public ContainerTab() {
    }

    public static ContainerTab newInstance(String game){
        ContainerTab fragment = new ContainerTab();
        fragment.setGame(game);
        return fragment;
    }

    public void setGame(String game){
        this.game = game;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        model = ViewModelProviders.of(this).get(GameModel.class);
        model.getNewsByGame(game).observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newList) {
                newsAdapter.fillNews(newList);

            }
        });
        model.getPlayersByGame(game).observe(this, new Observer<List<Players>>() {
            @Override
            public void onChanged(@Nullable List<Players> playerList) {
                playersAdapter.fillPlayers(playerList);
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_tab,container,false);
        tabLayout = view.findViewById(R.id.tablayout_container);
        viewPager = view.findViewById(R.id.viewpager_fragment_container);

        newsAdapter = new AdapterNews(view.getContext());
        playersAdapter = new AdapterPlayer(view.getContext());

        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(NewsGames.newInstance(newsAdapter),"News");
        viewPagerAdapter.addFragment(Top.newInstance(playersAdapter),"TOP PLAYERS");

        viewPagerAdapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        return view;
    }
}
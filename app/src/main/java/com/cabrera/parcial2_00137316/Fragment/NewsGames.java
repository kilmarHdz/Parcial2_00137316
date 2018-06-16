package com.cabrera.parcial2_00137316.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabrera.parcial2_00137316.Adapter.AdapterNews;
import com.cabrera.parcial2_00137316.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsGames extends Fragment {

    View view;
    private AdapterNews adapter;
    private RecyclerView rv;
    public NewsGames() {
        // Required empty public constructor
    }

    public static NewsGames newInstance(AdapterNews adapter) {
        NewsGames fragment = new NewsGames();
        fragment.setAdapter(adapter);
        return fragment;
    }
    private void setAdapter(AdapterNews adapter){
        this.adapter = adapter;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_games, container, false);
        rv = view.findViewById(R.id.recyclerview_news_by_games);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        return view;
    }

}

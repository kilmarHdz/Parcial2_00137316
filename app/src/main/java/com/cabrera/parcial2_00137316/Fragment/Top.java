package com.cabrera.parcial2_00137316.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabrera.parcial2_00137316.Adapter.AdapterPlayer;
import com.cabrera.parcial2_00137316.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Top extends Fragment {
    AdapterPlayer playersAdapter;
    RecyclerView rv;
    private TopPlayersTools tools;
    private SwipeRefreshLayout refreshContent;
    public Top() {
        // Required empty public constructor
    }

    public static Top newInstance(AdapterPlayer adapter) {
        Top fragment = new Top();
        fragment.setAdapter(adapter);
        return fragment;
    }
    public void setAdapter(AdapterPlayer adapter){
        playersAdapter= adapter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_top, container, false);
        rv = view.findViewById(R.id.recyclerview_top_players);
        refreshContent = view.findViewById(R.id.refresh_content_players);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(playersAdapter);

        refreshContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tools.refreshTopPlayers();
                refreshContent.setRefreshing(false);
            }
        });
        return view;
    }

    public interface TopPlayersTools{
        void refreshTopPlayers();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof TopPlayersTools){
            tools = (TopPlayersTools) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tools=null;
    }
}

package com.cabrera.parcial2_00137316.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabrera.parcial2_00137316.Entitys.Players;
import com.cabrera.parcial2_00137316.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPlayer extends RecyclerView.Adapter<AdapterPlayer.PlayersViewHolder>{
    View view;
    private final LayoutInflater layoutInflater;
    private List<Players> playerList;
    private Context context;


    public static class PlayersViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name,ranking;
        LinearLayout container;
        public PlayersViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageview_player);
            name = itemView.findViewById(R.id.text_name_player);
            ranking = itemView.findViewById(R.id.text_ranking_player);
            container = itemView.findViewById(R.id.player_container);
        }
    }
    public AdapterPlayer(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }
    public void fillPlayers(List<Players> playerList){
      this.playerList = playerList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.top_player,parent,false);
        return new PlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersViewHolder holder, final int position) {
        Picasso.get().load(playerList.get(position).getAvatar()).into(holder.avatar);
        holder.name.setText(playerList.get(position).getName());
        holder.ranking.setText("Ranking "+(position+1));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog playerDialog = new Dialog(context);
                playerDialog.setContentView(R.layout.player_info);
                playerDialog.setTitle(playerList.get(position).getName());
                ImageView avatarDialog = playerDialog.findViewById(R.id.avatar_player_dialog);
                TextView nameDialog = playerDialog.findViewById(R.id.text_name_player_dialog);
                TextView bioDialog = playerDialog.findViewById(R.id.text_bio_player_dialog);
                Picasso.get().load(playerList.get(position).getAvatar()).into(avatarDialog);
                nameDialog.setText(playerList.get(position).getName());
                bioDialog.setText(playerList.get(position).getBiografia());
                playerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(playerList!=null){
            return playerList.size();
        }else {
            return 0;
        }
    }

}

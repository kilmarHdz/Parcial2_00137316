package com.cabrera.parcial2_00137316.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabrera.parcial2_00137316.Entitys.News;
import com.cabrera.parcial2_00137316.Modelo.GameModel;
import com.cabrera.parcial2_00137316.R;
import com.squareup.picasso.Picasso;

public class onlyNews extends AppCompatActivity {

    private String idNew;
    private GameModel model;
    private News notice;
    private ImageView imageView;
    private TextView title,game,date,body;
    private FloatingActionButton favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_news);

        Intent responseIntent = getIntent();
        if(responseIntent!=null){
            idNew = responseIntent.getStringExtra("ID_NEW");
            Log.d("ID_NEW",idNew);
        }
        imageView = findViewById(R.id.imageview_new_single);
        title = findViewById(R.id.text_titulo_single);
        game = findViewById(R.id.text_category_single);
        date = findViewById(R.id.text_date_single);
        body = findViewById(R.id.text_body_new_single);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        model = ViewModelProviders.of(this).get(GameModel.class);
        model.getNew(idNew).observe(this, new Observer<News>() {
            @Override
            public void onChanged(@Nullable News aNew) {
                notice = aNew;
                if(notice!=null){
                    title.setText(notice.getTitle());
                    game.setText(notice.getGame());
                    date.setText(notice.getCreated_date());
                    body.setText(notice.getBody());
                }
            }
        });

    }
}

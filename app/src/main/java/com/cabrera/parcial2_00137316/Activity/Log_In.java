package com.cabrera.parcial2_00137316.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cabrera.parcial2_00137316.API.NewsAPI;
import com.cabrera.parcial2_00137316.Entitys.Token;
import com.cabrera.parcial2_00137316.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Log_In extends AppCompatActivity {
    private TextView text_error;
    private EditText text_username,text_password;
    private Button btn_iniciarSesion;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NewsAPI api;
    private Token securityToken;
    public static String TOKEN_SECURITY = "SECURITY_PREFERENCE_TOKEN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String savedToken = getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE).getString(TOKEN_SECURITY,"");
        if(!savedToken.equals("")){
            securityToken  = new Token(savedToken);
            Intent i = new Intent(Log_In.this,MainActivity.class);
            //i.putExtra("SECURITY_TOKEN",securityToken);
            startActivity(i);
        }
        setContentView(R.layout.activity_log__in);
        text_username = findViewById(R.id.text_username_loggin);
        text_password = findViewById(R.id.text_password_loggin);
        btn_iniciarSesion = findViewById(R.id.btn_loggin);
        text_error = findViewById(R.id.text_error);
        api = createAPI();
        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();
                compositeDisposable.add(api.getSecurityToken(username,password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getTokenSecurity()));

            }
        });

    }

    private NewsAPI createAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsAPI.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NewsAPI.class);
    }

    private DisposableSingleObserver<Token> getTokenSecurity(){
        return new DisposableSingleObserver<Token>() {
            @Override
            public void onSuccess(Token value) {
                securityToken = value;
                SharedPreferences shared = Log_In.this.getApplicationContext().getSharedPreferences("Token",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString(TOKEN_SECURITY,securityToken.getTokenSecurity());
                editor.apply();
                Intent i = new Intent(Log_In.this,MainActivity.class);
                i.putExtra("SECURITY_TOKEN",securityToken);
                startActivity(i);
            }

            @Override
            public void onError(Throwable e) {
                text_error.setVisibility(View.VISIBLE);
            }
        };
    }
}


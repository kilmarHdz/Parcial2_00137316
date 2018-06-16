package com.cabrera.parcial2_00137316.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabrera.parcial2_00137316.Adapter.AdapterNews;
import com.cabrera.parcial2_00137316.Entitys.Categoria;
import com.cabrera.parcial2_00137316.Entitys.Favorito;
import com.cabrera.parcial2_00137316.Entitys.News;
import com.cabrera.parcial2_00137316.Entitys.Token;
import com.cabrera.parcial2_00137316.Entitys.User;
import com.cabrera.parcial2_00137316.Fragment.ContainerTab;
import com.cabrera.parcial2_00137316.Fragment.Fav;
import com.cabrera.parcial2_00137316.Fragment.Top;
import com.cabrera.parcial2_00137316.Fragment.homeNews;
import com.cabrera.parcial2_00137316.Interfaz.ToolDao;
import com.cabrera.parcial2_00137316.Modelo.GameModel;
import com.cabrera.parcial2_00137316.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        homeNews.MainSetters ,
        Fav.FavoriteNewsTools,
        Top.TopPlayersTools,
        ToolDao {


    public static Token securityToken;
    private final static int ID_INFLATED_MENU = 101010101;
    private AdapterNews newsAdapter;
    private GameModel viewModel;
    private ImageView avatar;
    private TextView username,created_date;
    private List<Categoria> gameList;
    private ActionBar actionBar;
    public static String TOKEN_SECURITY = "SECURITY_PREFERENCE_TOKEN";
    private User currentUser;
    private List<Favorito> idNewList;
    private List<News> favoritesNewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String value = getApplicationContext().getSharedPreferences("Token",MODE_PRIVATE).getString(TOKEN_SECURITY,"");
        securityToken = new Token(value);
        Log.d("TOKEN",securityToken.getTokenSecurity());
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        executeLists();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.pantalla_fragment, new homeNews());
        ft.commit();

    }

    private void executeLists() {
        newsAdapter = new AdapterNews(this);
        viewModel = ViewModelProviders.of(this).get(GameModel.class);

        viewModel.getCurrentUser();
        viewModel.refreshNews();
        viewModel.refreshNewsListID();
        viewModel.refreshTopPlayers();

        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    currentUser = user;
                    if(currentUser!=null) {
                        initControls(currentUser);
                    }
                }
            }
        });

        viewModel.getGameList().observe(this, new Observer<List<Categoria>>() {
            @Override
            public void onChanged(@Nullable List<Categoria> categoryGames) {
                if(gameList!=null){
                    gameList.clear();
                }
                gameList = categoryGames;
                addMenuItemInNavMenuDrawer();
            }
        });
        viewModel.getFavorieList().observe(this, new Observer<List<Favorito>>() {
            @Override
            public void onChanged(@Nullable List<Favorito> favorites) {
                if(idNewList!=null) {
                    idNewList.clear();
                }
                idNewList = favorites;
                if (favorites != null) {
                    for(Favorito value:favorites){
                        viewModel.updateNewFaState("1",value.get_id());
                        Log.d("ID_FAVS",value.get_id());
                    }
                }
            }
        });
        viewModel.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newList) {
                if(newList!=null) {
                    newsAdapter.fillNews(newList);
                }
            }
        });

        viewModel.getFavoriteObjectNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newList) {
                if(newList!=null) {
                    if(favoritesNewList!=null) {
                        favoritesNewList.clear();
                    }
                    favoritesNewList = newList;
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_news_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if(id==R.id.News_menu){
            actionBar.setTitle(R.string.app_name);
            fragment = new homeNews();

        }
        if(id == R.id.favorites){
            fragment = Fav.newInstance(favoritesNewList);
        }
        if(id == R.id.logout){
            viewModel.deleteAllUsers();
            loggOut();
        }

        int i = 0;
        if(gameList!=null) {
            for (i = 0; i < gameList.size(); i++) {
                if (id == ID_INFLATED_MENU + i) {
                    actionBar.setElevation(0);
                    actionBar.setTitle(gameList.get(i).getCategoryName());
                    fragment = ContainerTab.newInstance(gameList.get(i).getCategoryName());
                    break;
                }
            }
        }

        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.pantalla_fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void initControls(User user){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username_bar);
        avatar = headerView.findViewById(R.id.avatar_user_bar);

        username.setText(user.getUsername());
        created_date.setText(user.getCreateDate());
        Picasso.get().load(user.getAvatar()).into(avatar);

    }

    private void addMenuItemInNavMenuDrawer(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuGames = menu.findItem(R.id.menu_games);
        menuGames.setTitle("Games");
        SubMenu subMenuGames = menuGames.getSubMenu();
        //AÑADIENDO LISTA DE JUEGOS
        subMenuGames.clear();
        for(int i=0;i<gameList.size();i++){
            subMenuGames.add(R.id.grup_games,ID_INFLATED_MENU+i,i,gameList.get(i).getCategoryName()).setCheckable(true);
        }
        navigationView.invalidate();
    }


    @Override
    public void addFavorites(String idNew) {
        viewModel.updateNewFaState("1",idNew);
        viewModel.addFavoriteNew(currentUser.get_id(),idNew);
        viewModel.refreshNews();
    }

    @Override
    public void removeFavorites(String idNew) {
        viewModel.updateNewFaState("0",idNew);
        viewModel.removeFavoriteNew(currentUser.get_id(),idNew);
        viewModel.refreshNews();
    }
    @Override
    public void setAdapters(RecyclerView rv) {
        rv.setAdapter(newsAdapter);
    }

    @Override
    public void refreshNews() {
        viewModel.refreshNews();
        //viewModel.refreshNewsListID();
    }

    @Override
    public void refreshFavorites() {
        viewModel.refreshNewsListID();
    }

    @Override
    public void refreshTopPlayers() {
        viewModel.refreshTopPlayers();
    }

    public void timeTokenExceeded(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,R.style.myDialog));
        builder.setMessage("Session time exceeded, Please loggin again")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loggOut();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loggOut(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        startActivity(new Intent(MainActivity.this,Log_In.class));
    }

}

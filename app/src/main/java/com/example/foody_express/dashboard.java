package com.example.foody_express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.A;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private HorizontalScrollView horizontalScrollView;
    private Handler scrolHandler = new Handler();
    private Runnable scrolRunnable;
    private int scrolSpeed = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        horizontalScrollView = findViewById(R.id.horizontal1);

        setupAutoScrolling();





        drawerLayout=findViewById(R.id.drawerlayout1);
        navigationView =findViewById(R.id.naview);
        toolbar = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawerbar_open, R.string.navigation_drawerbar_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupAutoScrolling() {

        scrolRunnable = new Runnable() {
            @Override
            public void run() {

                horizontalScrollView.smoothScrollBy(scrolSpeed,0);
                scrolHandler.postDelayed(this,20);
            }
        };
        scrolHandler.postDelayed(scrolRunnable,500);
    }

    @Override
    protected void onPause(){
        super.onPause();
        scrolHandler.removeCallbacks(scrolRunnable);
    }

    @Override
    protected void onResume(){
        super.onResume();
        scrolHandler.postDelayed(scrolRunnable,1000);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()== R.id.lg1){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(dashboard.this , login.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
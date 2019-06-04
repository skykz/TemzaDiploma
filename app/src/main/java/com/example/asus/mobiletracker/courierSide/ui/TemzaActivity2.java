package com.example.asus.mobiletracker.courierSide.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.courierSide.fragments.DoneFragment;
import com.example.asus.mobiletracker.courierSide.fragments.ProcessFragment;
import com.example.asus.mobiletracker.courierSide.fragments.WaitingFragment;
import com.example.asus.mobiletracker.userSide.googleApi.fragments.MySampleFabFragment;
import com.example.asus.mobiletracker.userSide.googleApi.ui.ProfileActivity;
import com.example.asus.mobiletracker.userSide.googleApi.ui.ScrollingActivity;
import com.example.asus.mobiletracker.userSide.googleApi.ui.SettingsActivity;
import com.example.asus.mobiletracker.userSide.googleApi.ui.SplashActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.asus.mobiletracker.Constants.USER_DATA;


public class TemzaActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "TemzaActivity";

    private TextView orders;
    private FloatingActionButton fab;

    private SharedPreferences sharedPreferences;

    private TextView username,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temza2);

        //toolbar elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        sharedPreferences  = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);

        //floating button to filter data
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySampleFabFragment dialogFrag = MySampleFabFragment.newInstance();
                dialogFrag.setParentFab(fab);
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });


        //navigation bars
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);


        View view = navigationView.getHeaderView(0);
        username  = (TextView) view.findViewById(R.id.username2);
        email = (TextView) view.findViewById(R.id.email2);
        email.setText(sharedPreferences.getString("email",null));


//        getProductList();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new WaitingFragment()).commit();
            navigationView.setCheckedItem(R.id.fragment_container2);
        }

        orders =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_waiting));

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
        getMenuInflater().inflate(R.menu.temza, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_progress) {
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new WaitingFragment()).commit();
//        }
//        else if (id == R.id.action_waiting)
//        {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new WaitingFragment()).commit();
//        }
//        else if (id == R.id.action_done)
//        {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new WaitingFragment()).commit();
//        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_waiting) {
            Log.i("Waiting", "FRAGMENT");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new WaitingFragment()).commit();

        }
        if (id == R.id.nav_process) {
            Log.i("Waiting", "FRAGMENT");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new ProcessFragment()).commit();

        }
        if (id == R.id.nav_done) {
            Log.i("Waiting", "FRAGMENT");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new DoneFragment()).commit();

        }else if (id == R.id.nav_settings) {

            Log.i("Settings", "FRAGMENT");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();

            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info) {
            info();}
            else if (id == R.id.nav_exit) {
            logout();
        } else if (id == R.id.nav_account){
            profile();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onResult(Object result) {
        Log.d("k9res", "onResult: " + result.toString());
        if (result.toString().equalsIgnoreCase("swiped_down")) {
            //do something or nothing
        } else {
            //handle result
        }
    }

    //logout by Yerassyl
    public void logout(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        actionLogout();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы действительно хотите выйти?").setPositiveButton("Да", dialogClickListener)
                .setNegativeButton("Отмена", dialogClickListener).show();



    }
    private void actionLogout(){

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(TemzaActivity2.this, SplashActivity.class));
                        finish();
                    }
                });
        SharedPreferences.Editor sPref = sharedPreferences.edit();

        sPref.remove("user_uid");
        sPref.remove("email");
        sPref.apply();
    }


    //by Yerassyl
    void info(){
        Intent intent = new Intent(this,ScrollingActivity.class);
        startActivity(intent);
    }

    void profile(){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

}

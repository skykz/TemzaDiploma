package com.example.asus.mobiletracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.mobiletracker.courierSide.ui.TemzaActivity2;
import com.example.asus.mobiletracker.userSide.googleApi.ui.TemzaActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.asus.mobiletracker.Constants.USER_DATA;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter ;
    private TabLayout tabIndicator;
    private Button btnNext;
    private int position = 0 ;
    private Button btnGetStarted;
    private Animation btnAnim ;
    private TextView tvSkip;

    private final static int LOGIN_PERMISSION = 1000;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // when this activity is about to be launch we need to check if its opened before or not
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(),TemzaActivity.class );
            startActivity(mainActivity);
            finish();
        }

        //TODO check whenever the user is logged in , if not redirect to another activity
        setContentView(R.layout.intro_activity);

        // hide the action bar

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_anim);
        tvSkip = findViewById(R.id.tv_skip);


        //TODO: change style of Auth UI
        //TODO: testing of App, take a video to show
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setLogo(R.drawable.temza)
                                .setTheme(R.style.Theme_AppCompat_DayNight_DarkActionBar)
                                .setAllowNewEmailAccounts(true).build(), LOGIN_PERMISSION
                );
            }
        });
        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Экспресс доставка","Доставка в течении 1-5 дней по всему Казахстану",R.drawable.curier));
        mList.add(new ScreenItem("Отслеживание ","Отслеживаете свой товар в реальном времени на карте",R.drawable.tracking));
        mList.add(new ScreenItem("Обзор заказов","Будьте в курсе всех выполненных процессов в приложении",R.drawable.order));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);

                }
                if (position == mList.size()-1) { // when we rech to the last screen
                    loaddLastScreen();
                }
            }
        });



        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });

        sharedPreferences  = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);

        if (sharedPreferences.getString("email",null) != null) {
            String userEmail = sharedPreferences.getString("email",null);

            if (!userEmail.isEmpty()){

                Pattern pattern = Pattern.compile("courier");
                Matcher matcher = pattern.matcher(userEmail);

                if (matcher.find())
                {
                    Intent intent = new Intent(this, TemzaActivity2.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Intent intent = new Intent(this, TemzaActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }


    void saveData(String user_uid,String email) {
        SharedPreferences.Editor sPref = sharedPreferences.edit();

        sPref.putString("user_uid", user_uid);
        sPref.putString("email",email);
        sPref.commit();

        Toast.makeText(this, "USer data saved", Toast.LENGTH_SHORT).show();
    }

    private boolean restorePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == LOGIN_PERMISSION)
        {
            startNewActivity(resultCode,data);
        }
    }

    private void startNewActivity(int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            String userEmail = null;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                userEmail = user.getEmail();

                if (!userEmail.isEmpty()){

                    Pattern pattern = Pattern.compile("courier");
                    Matcher matcher = pattern.matcher(userEmail);

                    saveData(user.getUid(),user.getEmail());


                    if (matcher.find())
                    {
                        Intent intent = new Intent(this, TemzaActivity2.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Intent intent = new Intent(this, TemzaActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        }else{
            Toast.makeText(this,"Login is failed! Try again ...",Toast.LENGTH_SHORT).show();
        }
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }
}

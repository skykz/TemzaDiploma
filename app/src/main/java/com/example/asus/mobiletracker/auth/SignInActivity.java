package com.example.asus.mobiletracker.auth;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.userSide.googleApi.ui.TemzaActivity;
import com.firebase.ui.auth.AuthUI;


public class SignInActivity extends AppCompatActivity {

    Button btnLogin;

    private final static int LOGIN_PERMISSION=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);


        btnLogin = (Button)findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.drawable.temza)
                        .setTheme(R.style.AppTheme_PopupOverlay)
                        .setAllowNewEmailAccounts(true).build(), LOGIN_PERMISSION
                );
            }
        });

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
            Intent intent = new Intent(SignInActivity.this, TemzaActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"Login is failed!",Toast.LENGTH_SHORT).show();
        }
    }
}

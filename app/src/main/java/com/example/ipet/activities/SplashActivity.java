package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.example.ipet.firebase.UserUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Se a ong estiver logada, já inicia na tela de gerenciamento da ong
                if (UserUtils.getUser() != null) {
                    startActivity(new Intent(getBaseContext(), ListagemDeCasos.class));
                    finish();
                } else { //senão, inicia a tela main
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }

            }
        }, 2000);
    }

}

package com.example.ipet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Se a ong estiver logada, já inicia na tela de gerenciamento com os dados da mesma.
                if (user != null) {
                    Intent intent = new Intent(getBaseContext(), ListagemDeCasos.class);
                    intent.putExtra("emailOng", user.getEmail());
                    startActivity(intent);
                    finish();
                } else {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }


            }
        }, 2000);
    }

}

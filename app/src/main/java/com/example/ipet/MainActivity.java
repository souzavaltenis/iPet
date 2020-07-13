package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bSouUmaOng;
    private Button QueroAjudarOng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bSouUmaOng = (Button) findViewById(R.id.bSouUmaOng);
        bSouUmaOng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSouUmaOng();
            }
        });

        QueroAjudarOng = (Button) findViewById(R.id.bQueroAjudarOng);
        QueroAjudarOng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQueroAjudarOng();
            }
        });
    }
    public void openSouUmaOng(){
        Intent intent = new Intent(this, SouUmaOngActivity.class);
        startActivity(intent);
    }
    public void openQueroAjudarOng(){
        Intent intent = new Intent(this, QueroAjudarOng.class);
        startActivity(intent);
    }
}
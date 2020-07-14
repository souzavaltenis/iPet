package com.example.ipet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
     * Método do onClick do Botão Sou Uma Ong
     * Serve para chamar a activity que irá credenciar uma Ong
     * */
    public void openSouUmaOng(View view){
        Intent intent = new Intent(this, SouUmaOngActivity.class);
        startActivity(intent);
    }

    /*
     * Método do onClick do Botão Quero Ajudar Uma Ong
     * Serve para chamar a activity que irá listar os casos de todas ongs
     * */
    public void openQueroAjudarOng(View view){
        Intent intent = new Intent(this, QueroAjudarOng.class);
        startActivity(intent);
    }
}
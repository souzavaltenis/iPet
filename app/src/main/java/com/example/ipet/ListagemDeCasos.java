package com.example.ipet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ListagemDeCasos extends AppCompatActivity {

    private String emailOng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_de_casos);

        emailOng = getIntent().getStringExtra("emailOng");
    }
}
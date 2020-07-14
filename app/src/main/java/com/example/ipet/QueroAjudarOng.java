package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ipet.entities.CasoOng;
import com.example.ipet.firebase.CasoUtils;
import com.example.ipet.recyclerview.RvCasoOngAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class QueroAjudarOng extends AppCompatActivity {

    FirebaseFirestore db;
    List<CasoOng> casosOngs;
    RecyclerView rvQueroAjudar;
    RvCasoOngAdapter rvCasoOngAdapter;
    CasoUtils casoUtils;
    TextView tvTotalCasos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quero_ajudar_ong);

        db = FirebaseFirestore.getInstance();
        casosOngs = new ArrayList<>();

        tvTotalCasos = findViewById(R.id.tvTotalCasos);

        rvQueroAjudar = findViewById(R.id.rvQueroAjudar);
        rvQueroAjudar.setLayoutManager(new LinearLayoutManager(this));
        rvQueroAjudar.setItemAnimator(new DefaultItemAnimator());
        rvQueroAjudar.setHasFixedSize(true);

        rvCasoOngAdapter = new RvCasoOngAdapter(this, casosOngs,
                new RvCasoOngAdapter.EstadoOnClickListener() {
            @Override
            public void onClickCaso(RvCasoOngAdapter.CasoViewHolder holder, int id) {

                Intent intent = new Intent(getApplicationContext(), DetalhesCasoActivity.class);
                intent.putExtra("casoOng", casosOngs.get(id));
                startActivity(intent);

            }
        });

        rvQueroAjudar.setAdapter(rvCasoOngAdapter);

        casoUtils = new CasoUtils(db, casosOngs, rvCasoOngAdapter, null,
                new CasoUtils.Changes() {
            //quanto houver requisições dentro do casoUtils, será coletado a quantidade de casos
            //foi usado callbacks para conseguir alterar o valor do textview da forma certa.
            @Override
            public void setarQuantidadeCasos(int qtd) {
                tvTotalCasos.setText(String.valueOf(qtd));
            }
        });

        casoUtils.listenerCasos();
    }

}
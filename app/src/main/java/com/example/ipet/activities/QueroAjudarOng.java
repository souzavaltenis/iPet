package com.example.ipet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.example.ipet.R;
import com.example.ipet.entities.Caso;
import com.example.ipet.firebase.CasoUtils;
import com.example.ipet.recyclerview.RvTodosCasosOngAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class QueroAjudarOng extends AppCompatActivity {

    FirebaseFirestore db;
    List<Caso> casosOngs;
    RecyclerView rvQueroAjudar;
    RvTodosCasosOngAdapter rvTodosCasosOngAdapter;
    CasoUtils<RvTodosCasosOngAdapter.CasoViewHolder> casoUtils;
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

        rvTodosCasosOngAdapter = new RvTodosCasosOngAdapter(this, casosOngs,
                new RvTodosCasosOngAdapter.CasoOnClickListener() {
                    @Override
                    public void onClickDetails(int position) {
                        Intent intent = new Intent(getApplicationContext(),
                                DetalhesCasoActivity.class);
                        intent.putExtra("casoOng", (Parcelable)casosOngs.get(position));
                        startActivity(intent);
                    }
         });

        rvQueroAjudar.setAdapter(rvTodosCasosOngAdapter);

        casoUtils = new CasoUtils<>(db, casosOngs,
                rvTodosCasosOngAdapter, new CasoUtils.Changes() {
            //quanto houver requisições dentro do casoUtils, será coletado a quantidade de casos
            //foi usado callbacks para conseguir alterar o valor do textview da forma certa.
            @Override
            public void setarQuantidadeCasos(int qtd) {
                tvTotalCasos.setText(String.valueOf(qtd));
            }
        }, false, null);

        casoUtils.listenerCasos();
    }

}
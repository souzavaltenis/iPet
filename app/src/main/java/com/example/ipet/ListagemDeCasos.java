package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ipet.entities.CasoOng;
import com.example.ipet.entities.Ong;
import com.example.ipet.firebase.CasoUtils;
import com.example.ipet.recyclerview.RvCasoOngAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListagemDeCasos extends AppCompatActivity {

    FloatingActionButton btCriarCaso;
    RecyclerView rvCasosOng;
    TextView tvNomeDaOng;
    TextView tvTotCases;

    String emailOng;
    Ong ong;
    FirebaseFirestore db;
    List<CasoOng> casosOngs;
    String pathDocOng;

    RvCasoOngAdapter rvCasoOngAdapter;
    CasoUtils casoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_de_casos);

        emailOng = getIntent().getStringExtra("emailOng");
        db = FirebaseFirestore.getInstance();
        ong = new Ong();
        casosOngs = new ArrayList<>();

        tvNomeDaOng = findViewById(R.id.tvNomeDaOng);

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false); //desativa até que os dados da ong sejam carregados

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        rvCasoOngAdapter = new RvCasoOngAdapter(this, casosOngs, null);

        rvCasosOng.setAdapter(rvCasoOngAdapter);

        getDadosOng();
    }

    /*
    * Obtem os dados da Ong filtrada pelo email vindo do login, e por meio do documento ong,
    * realiza uma filtragem de casos somente da ong atual feita no método SetDadosCaseOng.
    * */
    public void getDadosOng(){

        db.collection("ongs")
                .whereEqualTo("email", emailOng)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        setDadosCaseOng(Objects.requireNonNull(task.getResult()));
                        btCriarCaso.setEnabled(true); //dados da ong carregado, pode criar caso
                    }
                });
    }

    /*
    * Recebe um QuerySnapshot, que é o resultado da pesquisa de ong por email, por meio deste,
    * é pego os dados da primeira ong, pois não existirá mais de uma ong com o mesmo email.
    *
    * Por fim, instancia um CasoUtils para obter os casos filtrados por Ong, no caso, a atual.
    * */
    public void setDadosCaseOng(QuerySnapshot result){

        DocumentSnapshot docOng = result.iterator().next();

        pathDocOng = "ongs/" + docOng.getId();

        ong = docOng.toObject(Ong.class);
        tvNomeDaOng.setText(ong.getNome());

        casoUtils = new CasoUtils(db, casosOngs, rvCasoOngAdapter, docOng.getReference(),
               new CasoUtils.Changes() {
            //quanto houver requisições dentro do casoUtils, será coletado a quantidade de casos
            //foi usado callbacks para conseguir alterar o valor do textview da forma certa.
            @Override
            public void setarQuantidadeCasos(int qtd) {
              //tvTotCases.setText(String.valueOf(qtd));
           }
       });

        casoUtils.listenerCasos();
    }

    /*
    * Método setado no onClick do botão de criar caso, irá apenas iniciar a activity, passando
    * o path do documento da Ong atual, para referenciação no momento da criação do caso.
    * */
    public void criarCaso(View view){
        Intent intent = new Intent(getApplicationContext(), CriarCasoActivity.class);
        intent.putExtra("pathDocOng", pathDocOng);
        startActivity(intent);
    }
    public void deslogar(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
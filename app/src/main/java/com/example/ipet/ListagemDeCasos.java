package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipet.entities.CasoOng;
import com.example.ipet.entities.Ong;
import com.example.ipet.firebase.CasoUtils;
import com.example.ipet.recyclerview.RvCasoOngAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        tvNomeDaOng.setText("");

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false); //desativa até que os dados da ong sejam carregados

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        rvCasoOngAdapter = new RvCasoOngAdapter(this, casosOngs,
                new RvCasoOngAdapter.CasoOnClickListener() {
            @Override
            public void onClickCaso(RvCasoOngAdapter.CasoViewHolder holder, int id) {

            }

            @Override
            public void onClickTrash(int position) {
                apagarCaso(casosOngs.get(position).getCaso().getId());
            }
        });

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
        tvNomeDaOng.setText("Bem-vinda " + ong.getNome());

        casoUtils = new CasoUtils(db, casosOngs, rvCasoOngAdapter, docOng.getReference(), null);
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

    /*
    * Método que apaga um documento de caso na coleção casos, filtrado pela sua id
    * */
    public void apagarCaso(String id){

        db.collection("casos")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //pegou o primeiro documento e salvou o seu id
                        //a garantia do primeiro é pelo fato de que só irá existir
                        //1 caso com essa id.
                        String idCaso = task.getResult().iterator().next().getId();
                        apagarDocumento(idCaso);
                    }
                });
    }

    /*
    * Recebe o id do documento para que a exclusão seja realizada na coleção casos.
    * */
    public void apagarDocumento(String pathDocCaso){

        db.collection("casos").document(pathDocCaso)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Caso apagado",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
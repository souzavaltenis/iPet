package com.example.ipet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipet.R;
import com.example.ipet.entities.Caso;
import com.example.ipet.entities.Ong;
import com.example.ipet.firebase.CasoUtils;
import com.example.ipet.firebase.UserUtils;
import com.example.ipet.recyclerview.RvCasoOngAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListagemDeCasos extends AppCompatActivity {

    FloatingActionButton btCriarCaso;
    RecyclerView rvCasosOng;
    TextView tvNomeDaOng;

    String emailOng;
    Ong ong;
    FirebaseFirestore db;
    List<Caso> casosOng;

    RvCasoOngAdapter rvCasoOngAdapter;
    CasoUtils<RvCasoOngAdapter.CasoViewHolder> casoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_de_casos);

        emailOng = UserUtils.getEmail();
        db = FirebaseFirestore.getInstance();
        ong = new Ong();
        casosOng = new ArrayList<>();

        tvNomeDaOng = findViewById(R.id.tvNomeDaOng);
        tvNomeDaOng.setText("");

        btCriarCaso = findViewById(R.id.btAddCase);
        btCriarCaso.setEnabled(false); //desativa até que os dados da ong sejam carregados

        rvCasosOng = findViewById(R.id.rvCasosOng);
        rvCasosOng.setLayoutManager(new LinearLayoutManager(this));
        rvCasosOng.setItemAnimator(new DefaultItemAnimator());
        rvCasosOng.setHasFixedSize(true);

        rvCasoOngAdapter = new RvCasoOngAdapter(this, casosOng,
                new RvCasoOngAdapter.CasoOnClickListener() {
                    @Override
                    public void onClickTrash(int position, TextView tv) {
                        System.out.println("entrou no método de apagar caso " + position);
                        tv.setEnabled(false); //garante que o icone nao será clicável durante a op
                        apagarCaso(casosOng.get(position).getId(), tv);
                    }
                });

        rvCasosOng.setAdapter(rvCasoOngAdapter);

        getDadosOng();
    }

    /*
    * Obtem os dados da Ong filtrada pelo email vindo do login
    * */
    public void getDadosOng(){
        db.collection("ongs")
                .document(emailOng)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        ong = documentSnapshot.toObject(Ong.class);

                        tvNomeDaOng.setText("Bem-vinda " + ong.getNome());
                        btCriarCaso.setEnabled(true); //dados da ong carregado, pode criar caso

                        casoUtils = new CasoUtils<>(db, casosOng, rvCasoOngAdapter,
                                null, true, emailOng);

                        casoUtils.listenerCasosOng();
                    }
                });
    }

    /*
    * Método setado no onClick do botão de criar caso, irá apenas iniciar a activity para criação
    * de um novo caso, passando os dados da ong atual
    * */
    public void criarCaso(View view){
        Intent intent = new Intent(getApplicationContext(), CriarCasoActivity.class);
        intent.putExtra("ong", (Parcelable)ong);
        startActivity(intent);
    }

    /*
    * Desloga o usuário atual presente no sistema de autenticação do firebase authenticator
    * e volta para a tela inicial
    * */
    public void deslogar(View view){
        UserUtils.logoutUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /*
    * Método que apaga um documento de caso na sub-coleção casos do documento da ong atual pelo id
    * */
    public void apagarCaso(String id, final TextView tv){

        db.collection("ongs")
                .document(emailOng)
                .collection("casos")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Caso apagado",
                                Toast.LENGTH_LONG).show();
                        tv.setEnabled(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao apagar, " +
                                        "tente novamente mais tarde", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /*
    * Método para fechar o app quando o botão de voltar for pressionado na tela de gerenciamento
    * de ong, forçando o usuário a deslogar caso deseje voltar nas opções da main activity.
    * */
    @Override
    public void onBackPressed() {
        finish();
    }

}
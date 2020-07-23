package com.example.ipet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ipet.R;
import com.example.ipet.entities.Ong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CriarCasoActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Ong ong;
    EditText etTituloCaso, etDescricaoCaso, etValorCaso;
    Button btCriarCaso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_caso);

        db = FirebaseFirestore.getInstance();
        ong = getIntent().getParcelableExtra("ong");

        etTituloCaso = findViewById(R.id.etTituloCaso);
        etDescricaoCaso = findViewById(R.id.etDescricaoCaso);
        etValorCaso = findViewById(R.id.etValorCaso);

        btCriarCaso = findViewById(R.id.btCriarCaso);
    }

    /*
    * Extrai os dados da interface de criar caso e salva na subcoleção casos no doc da ong atual
    * no firestore.
    * */
    public void criarUmCaso(View view){

        String id = getRandomId();
        String titulo = etTituloCaso.getText().toString();
        String descricao = etDescricaoCaso.getText().toString();
        Double valor = Double.parseDouble(etValorCaso.getText().toString());

        Map<String, Object> caso = new HashMap<>();
        caso.put("id", id);
        caso.put("titulo", titulo);
        caso.put("descricao", descricao);
        caso.put("valor", valor);

        setEnableViews(false);

        db.collection("ongs")
                .document(ong.getEmail())
                .collection("casos")
                .document(id)
                .set(caso)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Caso Criado.",
                                Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Falha ao criar um caso.",
                        Toast.LENGTH_LONG).show();
                setEnableViews(true);
            }
        });
    }

    /*
    * Método para habilidar/desabilidar todas views da interface de criar um caso
    * */
    public void setEnableViews(boolean op){
        etTituloCaso.setEnabled(op);
        etDescricaoCaso.setEnabled(op);
        etValorCaso.setEnabled(op);
        btCriarCaso.setEnabled(op);
    }

    /*
     * Cria um id único usando conjunto de letras aleatória, junto com o sistema do tempo
     * multiplicado por um número randomico, por fim, adiciona mais letras aleatórias
     * */
    public String getRandomId(){
        return  getLetrasRand() +
                System.currentTimeMillis() * new Random().nextInt(1000) +
                getLetrasRand();
    }

    /*
    * Gera um conjunto de 4 letras armazendas em uma string.
    * As letras podem ser de 'a' até 'z'.
    * */
    public String getLetrasRand(){

        StringBuilder str = new StringBuilder();

        int min = 97; //a
        int max = 122; //z

        for(int i=0; i<4; i++){
            str.append((char) (new Random().nextInt((max - min) + 1) + min));
        }

        return str.toString();
    }
}
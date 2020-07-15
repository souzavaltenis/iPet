package com.example.ipet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ipet.entities.Caso;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class CriarCasoActivity extends AppCompatActivity {

    FirebaseFirestore db;
    String pathDocOng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_caso);

        db = FirebaseFirestore.getInstance();
        pathDocOng = getIntent().getStringExtra("pathDocOng");

    }

    /*
    * Extrai os dados da interface de criar caso e salva na coleção casos no firestore.
    * */
    public void criarUmCaso(View view){

        String id = getRandomId();
        String titulo = getTextOfEt(R.id.etTitleCase);
        String descricao = getTextOfEt(R.id.etDescricaoCaso);
        Double valor = Double.parseDouble(getTextOfEt(R.id.etValorCaso));
        DocumentReference docOng = db.document(pathDocOng);

        Caso caso = new Caso(id, titulo, descricao, valor, docOng);

        db.collection("casos")
                .add(caso)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Caso Criado.",
                                Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
    }

    /*
     * Método que recebe o id de um EditText e pega o seu conteudo
     * */
    public String getTextOfEt(int idEditText){
        EditText editText = findViewById(idEditText);
        return editText.getText().toString();
    }

    /*
     * Cria um id único usando conjunto de letras aleatória, junto com o sistema do tempo
     * multiplicado por um número randomico, por fim, adiciona mais letras aleatórias
     * */
    public String getRandomId(){
        return "_" + getLetrasRand() +
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
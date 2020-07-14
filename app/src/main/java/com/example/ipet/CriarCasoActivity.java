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

        String titulo = getTextOfEt(R.id.etTitleCase);
        String descricao = getTextOfEt(R.id.etDescricaoCaso);
        Double valor = Double.parseDouble(getTextOfEt(R.id.etValorCaso));
        DocumentReference docOng = db.document(pathDocOng);

        Caso caso = new Caso(titulo, descricao, valor, docOng);

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

}
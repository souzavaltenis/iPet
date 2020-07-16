package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ipet.confspinner.SpinnerUtils;
import com.example.ipet.entities.Ong;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroOng extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Spinner spinnerUf = findViewById(R.id.spUf);
        Spinner spinnerCidade = findViewById(R.id.spCidade);

        SpinnerUtils.confSpinners(getApplicationContext(),
                spinnerUf, "UF",
                spinnerCidade, "Cidade");
    }

    /*
    * Método executado no onClick do botão cadastrar
    * Irá extrair informações da interface de cadastro, criando um novo usuário e documento
    * por meio do método criarUserOng
    * */
    public void cadastrar(View view){

        String nome = getTextOfEt(R.id.etNome);
        String email = getTextOfEt(R.id.etEmail);
        String senha = getTextOfEt(R.id.etSenha);
        String whatsapp = verificaNumero(getTextOfEt(R.id.etWhatsapp));
        String uf = getDataOfSp(R.id.spUf);
        String cidade = getDataOfSp(R.id.spCidade);

        Ong ong = new Ong(nome, email, whatsapp, uf, cidade);

        criarUserOng(ong, senha);
    }

    public String verificaNumero(String num){
        String dddPais = "55";
        String doisPrimeirosDigitos = num.substring(0, 2);
        return !doisPrimeirosDigitos.equals(dddPais) ? dddPais + num : num;
    }

    /*
    * Método que receberá todas as informações dos campos da tela de cadastro
    * Primeiro passo: cria um usuário no firebase authentication utilizando somente email e senha
    * Segundo passo: quando o usuário for criado com sucesso, salvará todas as informações menos
    * a senha, em um novo documento.
    * */
    public void criarUserOng(final Ong ong, String senha){
        mAuth.createUserWithEmailAndPassword(ong.getEmail(), senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            salvarDadosOng(ong);
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro no cadastro.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
    * Recebe um objeto ong e salva um documento na coleção ongs no bd firestore.
    * */
    public void salvarDadosOng(Ong ong){
        db.collection("ongs")
                .add(ong)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Cadastro Relizado.",
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
     * Método que recebe o id de um Spinner e pega o conteudo selecionado
     * */
    private String getDataOfSp(int idSpinner){
        Spinner sp = findViewById(idSpinner);
        return sp.getSelectedItem().toString();
    }
}
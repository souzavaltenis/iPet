package com.example.ipet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ipet.R;
import com.example.ipet.confspinner.SpinnerUtils;
import com.example.ipet.entities.Ong;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroOng extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    EditText etNome, etEmail, etSenha, etWhatsapp;
    Spinner spUf, spCidade;
    Button bCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etWhatsapp = findViewById(R.id.etWhatsapp);

        spUf = findViewById(R.id.spUf);
        spCidade = findViewById(R.id.spCidade);

        SpinnerUtils.confSpinners(getApplicationContext(),
                spUf, "UF",
                spCidade, "Cidade");

        bCadastrar = findViewById(R.id.bCadastrar);
    }

    /*
    * Método executado no onClick do botão cadastrar
    * Irá extrair informações da interface de cadastro, criando um novo usuário e documento
    * por meio do método criarUserOng
    * */
    public void cadastrar(View view){

        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        String whatsapp = verificaNumero(etWhatsapp.getText().toString());
        String uf = getDataOfSp(R.id.spUf);
        String cidade = getDataOfSp(R.id.spCidade);

        Ong ong = new Ong(nome, email, whatsapp, uf, cidade);

        setEnableViews(false); //desativa as views enquanto o cadastro esta sendo realizado

        criarUserOng(ong, senha);
    }

    /*
    * Validação de número com ou sem ddd do pais
    * */
    public String verificaNumero(String num){
        String dddPais = "55";
        String doisPrimeirosDigitos = num.substring(0, 2);
        return !doisPrimeirosDigitos.equals(dddPais) ? dddPais + num : num;
    }

    /*
     * Método para habilidar/desabilidar todas views da interface de cadastro ong
     * */
    public void setEnableViews(boolean op){
        etNome.setEnabled(op);
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        etWhatsapp.setEnabled(op);
        spUf.setEnabled(op);
        spCidade.setEnabled(op);
        bCadastrar.setEnabled(op);
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
                            setEnableViews(true); //ativa caso algo deu errado
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
                .document(ong.getEmail()).set(ong)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Cadastro Relizado.",
                                Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
    }

    /*
     * Método que recebe o id de um Spinner e pega o conteudo selecionado
     * */
    private String getDataOfSp(int idSpinner){
        Spinner sp = findViewById(idSpinner);
        return sp.getSelectedItem().toString();
    }
}
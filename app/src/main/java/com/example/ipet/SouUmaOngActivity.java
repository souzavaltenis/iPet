package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SouUmaOngActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etEmail;
    EditText etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sou_uma_ong);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);

        mAuth = FirebaseAuth.getInstance();
    }

    /*
    * Método executado dentro do onClick do botão Login
    * Irá extrair as informações dos campos email e senha, repassando para o método realizarLogin
    * */
    public void login(View view){

        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        realizarLogin(email, senha);
    }

    /*
    * Utilizando a autenticação do Firebase, o método recebe email e senha
    * Com os dados ele verificará se as credenciais estão corretas
    * Caso sim, o método listagemDeCasos será chamado, repassando o email
    * */
    public void realizarLogin(final String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listagemDeCasos(email);
                        } else {
                            Toast.makeText(getApplicationContext(), "Credenciais Inválidas!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
     * Método chamado assim que as credenciais forem validadas
     * Serve para chamar a activity de listagem de casos passando a informação do email da ong
     * */
    public void listagemDeCasos(String email){
        Intent intent = new Intent(this, ListagemDeCasos.class);
        intent.putExtra("emailOng", email);
        startActivity(intent);
    }

    /*
    * Método do onClick do TextView não tenho cadastro
    * Serve para chamar a activity de cadastro
    * */
    public void naoTenhoCadastro(View view){
        Intent intent = new Intent(this, CadastroOng.class);
        startActivity(intent);
    }

}
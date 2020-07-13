package com.example.ipet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sou_uma_ong);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etSenha = (EditText) findViewById(R.id.etSenha);

        mAuth = FirebaseAuth.getInstance();

        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString().trim();
                final String senha = etSenha.getText().toString().trim();
                testLogin(email, senha);
            }
        });
    }

    public void testLogin(String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Logou", Toast.LENGTH_LONG).show();
                            ListagemDeCasos();
                        } else {
                            Toast.makeText(getApplicationContext(), "Credenciais Inválidas!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void NaoTenhoCadastro(View view){
        Intent intent = new Intent(this, CadastroOng.class);
        startActivity(intent);
    }

    public void ListagemDeCasos(){
        Intent intent = new Intent(this, ListagemDeCasos.class);
        startActivity(intent);
    }

}
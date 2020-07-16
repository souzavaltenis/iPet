package com.example.ipet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ipet.entities.Caso;
import com.example.ipet.entities.CasoOng;
import com.example.ipet.entities.Ong;

public class DetalhesCasoActivity extends AppCompatActivity {

    CasoOng casoOng;
    Button email;
    Button Whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_caso);

        email = (Button) findViewById(R.id.btEmailOngCase);
        Whatsapp = (Button) findViewById(R.id.btWhatsappOngCase);

        casoOng = getIntent().getParcelableExtra("casoOng");

        setarInformacoes();

    }

    /*
    * Pega todos os dados do casoOng e seta nos TextView's
    * */
    public void setarInformacoes(){

        Caso caso = casoOng.getCaso();
        Ong ong = casoOng.getOng();

        setTextTv(R.id.tvOngData, ong.getNome());
        setTextTv(R.id.tvTitleData, caso.getTitulo());
        setTextTv(R.id.tvDescricaoData, caso.getDescricao());
        setTextTv(R.id.tvValorData, String.valueOf(caso.getValor()));

    }

    /*
    * Método que recebe id de um TextView e uma string, instanciando um TextView e setando o texto.
    * */
    public void setTextTv(int idTextView, String text){
        TextView tv = findViewById(idTextView);
        tv.setText(text);
    }

    public void voltar(View view){
        Intent intent = new Intent(getApplicationContext(), QueroAjudarOng.class);
        startActivity(intent);
    }

    public void Email(View view){
        Ong ong = casoOng.getOng();
        String destinatario = ong.getEmail();
        Intent EnviarEmail = new Intent(Intent.ACTION_SEND);
        EnviarEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {destinatario});
        EnviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Teste de e-mail");
        EnviarEmail.putExtra(Intent.EXTRA_TEXT,"O corpo do Email");
        EnviarEmail.setType("text/plain");
        startActivity(Intent.createChooser(EnviarEmail, "Escolha o cliente de e-mail"));
    }

    public void Whatsapp(View view){
        Ong ong = casoOng.getOng();
        String destinatario = ong.getWhatsapp();
        Intent EnviarEmail = new Intent(Intent.ACTION_SEND);
        EnviarEmail.putExtra(Intent.EXTRA_PHONE_NUMBER, new String[] {destinatario});
        EnviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Teste de e-mail");
        EnviarEmail.putExtra(Intent.EXTRA_TEXT,"O corpo da mensagem");
        EnviarEmail.setType("text/plain");
        EnviarEmail.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(EnviarEmail, "Escolha o cliente de Whatsapp"));
    }

}
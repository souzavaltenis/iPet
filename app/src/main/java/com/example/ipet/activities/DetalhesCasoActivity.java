package com.example.ipet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ipet.R;
import com.example.ipet.entities.Caso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetalhesCasoActivity extends AppCompatActivity {

    Caso caso;
    Button email;
    Button Whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_caso);

        email = findViewById(R.id.btEmailOngCase);
        Whatsapp = findViewById(R.id.btWhatsappOngCase);

        caso = getIntent().getParcelableExtra("casoOng");

        setarInformacoes();
    }

    /*
    * Pega todos os dados do caso e seta nos TextView's
    * */
    public void setarInformacoes(){
        setTextTv(R.id.tvOngData, caso.getOng().getNome());
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

    /*
    * Método chamado no onClick da setinha na parte superior da interface de detalhes de caso
    * fazendo com que apenas simule o clique no botão de voltar
    * */
    public void voltar(View view){
        onBackPressed();
    }

    /*
    * Abre uma lista de aplicativos para mandar um email com o propósito de avisar a ong
    * que a pessoa quer ajudar no caso.
    * */
    public void email(View view){

        String msg = getMsgParaOng();
        String destinatario = caso.getOng().getEmail();

        Intent EnviarEmail = new Intent(Intent.ACTION_SEND);
        EnviarEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {destinatario});
        EnviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Quero ajudar um caso");
        EnviarEmail.putExtra(Intent.EXTRA_TEXT,msg);
        EnviarEmail.setType("text/plain");
        startActivity(Intent.createChooser(EnviarEmail, "Escolha o cliente de e-mail"));
    }

    /*
    * Abre o whatsapp ja na conversa com o número da ong, mandando uma mensagem informando
    * o interesse no caso.
    * */
    public void whatsapp(View view){

        String msg = getMsgParaOng();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" +
                caso.getOng().getWhatsapp() +
                "&text=" +
                msg));

        startActivity(intent);

    }

    /*
    * Método que define a mensagem que será enviada no email ou whatsapp
    * */
    public String getMsgParaOng(){
        return getMsgHoras() + " " + caso.getOng().getNome() + ", Gostaria de ajudar no caso " +
                caso.getTitulo() + ", com o valor de " + String.format("R$ %.2f", caso.getValor());
    }

    /*
    * Coleta a hora atual e retorna uma mensagem de saudação
    * */
    public String getMsgHoras(){

        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH");
        int horas = Integer.parseInt(dateFormat_hora.format(new Date()));

        if(horas >= 0 && horas < 12){
            return "Bom Dia";
        }else if(horas < 18){
            return "Boa tarde";
        }else{
            return "Boa Noite";
        }

    }

}
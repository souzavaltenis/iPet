package com.example.ipet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ipet.entities.Caso;
import com.example.ipet.entities.CasoOng;
import com.example.ipet.entities.Ong;

public class DetalhesCasoActivity extends AppCompatActivity {

    CasoOng casoOng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_caso);

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
        setTextTv(R.id.tvEmailOngCase, ong.getEmail());
        setTextTv(R.id.tvWhatsappOngCase, ong.getWhatsapp());
    }

    /*
    * Método que recebe id de um TextView e uma string, instanciando um TextView e setando o texto.
    * */
    public void setTextTv(int idTextView, String text){
        TextView tv = findViewById(idTextView);
        tv.setText(text);
    }
}
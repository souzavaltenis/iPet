package com.example.ipet.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.entities.Caso;
import com.example.ipet.entities.Ong;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CasoUtils<T extends RecyclerView.ViewHolder> {

    public interface Changes{
        void setarQuantidadeCasos(int qtd);
    }

    FirebaseFirestore db;
    List<Caso> casosOngs;
    RecyclerView.Adapter<T> rvAdapter;
    Changes changes;
    Boolean filterOng;
    String emailOng;

    public CasoUtils(FirebaseFirestore db, List<Caso> casosOngs,
                     RecyclerView.Adapter<T> rvAdapter, Changes changes, Boolean filterOng,
                     String emailOng) {
        this.db = db;
        this.casosOngs = casosOngs;
        this.rvAdapter = rvAdapter;
        this.changes = changes;
        this.filterOng = filterOng;
        this.emailOng = emailOng;
    }

    /*
    * Listener que chamará o método obterDadosCasos quando descobrir alguma alteração,
    * sendo configurado para sem filtro de ong e com filtro.
    * */
    public void listenerCasos(){

        if(filterOng){ //adiciona listener de alterações apenas na subcoleção casos de uma ong
            db.collection("ongs")
                    .document(emailOng)
                    .collection("casos")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException error) {
                            obterDadosCasos();
                        }
                    });

        }else{ //add listener de alterações em todas as subcoleções de todas ongs

            db.collectionGroup("casos")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException error) {
                            obterDadosCasos();
                        }
                    });

        }
    }

    /*
    * Método responsável por preencher a lista de casos de acordo com as configurações passadas
    * ao instanciar o mesmo.
    * */
    public void obterDadosCasos(){

        casosOngs.clear(); //garante que a lista vai estar vazia e preparada para receber dados

        if(!filterOng) { //pesquisa sem filtro de ong

            //pega todos os documentos de ong e passa para o método getCasosOf que irá acumular
            //os casos da ong atual, fazendo isso com todas, conseguindo todos os casos.
            db.collection("ongs")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot document : queryDocumentSnapshots){
                                Ong ong = document.toObject(Ong.class);
                                getCasosOf(ong);
                            }
                        }
                    });

        }else{ //pesquisa com filtro de ong

            //pesquisa direta no documento da ong passada no construtor, quando dado sucesso
            //insere na lista apenas os casos da ong escolhida.
            db.collection("ongs")
                    .document(emailOng)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Ong ong = task.getResult().toObject(Ong.class);
                            getCasosOf(ong);
                        }
                    });
        }
    }

    /*
    * Recebe um objeto ong para realizar uma pesquisa no documento de ong correspondente, pegando
    * todos os documentos do mesmo.
    * */
    public void getCasosOf(final Ong ong){

        db.collection("ongs")
                .document(ong.getEmail())
                .collection("casos")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        extractDataCasos(ong, queryDocumentSnapshots);
                    }
                });
    }

    /*
    * Extrai os dados presentes no QuerySnapshot documents e add na lista cada caso juntamente com
    * a ong. Depois de ler todos documentos, é adicionado essa lista temporária na lista principal
    * e avisa ao adaptador do recyclerview que houve mudanças nela para que ele atualize.
    * */
    public void extractDataCasos(Ong ong, QuerySnapshot documents) {

        List<Caso> casos = new ArrayList<>();

        for(DocumentSnapshot document : documents){
            String id = document.getString("id");
            String titulo = document.getString("titulo");
            String descricao = document.getString("descricao");
            Double valor = document.getDouble("valor");
            casos.add(new Caso(id, titulo, descricao, valor, ong));
        }

        casosOngs.addAll(casos);
        rvAdapter.notifyDataSetChanged();

        if(changes != null){
            changes.setarQuantidadeCasos(casosOngs.size());
        }
    }

}

package com.example.ipet.firebase;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ipet.entities.Caso;
import com.example.ipet.entities.CasoOng;
import com.example.ipet.entities.Ong;
import com.example.ipet.recyclerview.RvCasoOngAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class CasoUtils {

    public interface Changes{
        void setarQuantidadeCasos(int qtd);
    }

    FirebaseFirestore db;
    List<CasoOng> casosOngs;
    RvCasoOngAdapter rvCasoOngAdapter;
    DocumentReference filterOngId;
    Changes changes;

    public CasoUtils(FirebaseFirestore db, List<CasoOng> casosOngs,
                     RvCasoOngAdapter rvCasoOngAdapter, DocumentReference filterOngId,
                     Changes changes) {
        this.db = db;
        this.casosOngs = casosOngs;
        this.rvCasoOngAdapter = rvCasoOngAdapter;
        this.filterOngId = filterOngId;
        this.changes = changes;
    }

    /*
     * Listener que ficará responsável para alterar as informações caso detectado mudanças
     * na coleção de casos.
     * */
    public void listenerCasos(){

        db.collection("casos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        obterDadosCasos();
                    }
                });
    }

    /*
     * Método para coletar todos os documentos de casos e passar o resultado para
     * o método chamado extractDataCasos.
     * */
    public void obterDadosCasos(){

        if(filterOngId == null) { //pesquisa sem filtro de ong

            db.collection("casos")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot documents = Objects.requireNonNull(task.getResult());
                            changes.setarQuantidadeCasos(documents.size());//seta qtd de casos atual
                            extractDataCasos(documents);
                        }
                    });

        }else{ //pesquisa com filtro de ong

            db.collection("casos")
                    .whereEqualTo("ong", filterOngId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot documents = Objects.requireNonNull(task.getResult());
                            changes.setarQuantidadeCasos(documents.size());//seta qtd de casos atual
                            extractDataCasos(documents);
                        }
                    });

        }
    }

    /*
     * Extração de dados de um QuerySnapshot para uma lista de casos e uma listagem de cada caso
     * usando um for, onde que para acessar os dados da Ong correspondente, foi necessário
     * acessar a referencia da mesma na coleção ongs.
     *
     * Com os dados do caso e da ong, é criado um objeto da classe CasoOng e adicionado na lista
     * casos criada globalmente, e, por fim, cada item desta lista será adicionado no RecyclerView
     * por meio do método setarDadoCaso.
     * */
    public void extractDataCasos(QuerySnapshot documents) {

        casosOngs.clear(); //limpando possíveis dados anteriores

        List<Caso> casos = documents.toObjects(Caso.class); //pegando dados atualizados

        for(final Caso caso : casos){

            //recuperando os dados da referencia ong
            caso.getOng().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Ong ong = task.getResult().toObject(Ong.class);
                    CasoOng casoOng = new CasoOng(caso, ong);
                    setarDadoCaso(casoOng);
                }
            });

        }
    }

    /*
     * Método que recebe dados do caso e a ong relacionada por meio do objeto CasoOng e adiciona
     * na lista casosOngs, lista essa que está associada com o adaptador, logo só será necessário
     * avisar o adaptador que houve mudanças, usando o notifyDataSetChanged.
     * */
    public void setarDadoCaso(CasoOng casoOng){
        casosOngs.add(casoOng);
        rvCasoOngAdapter.notifyDataSetChanged();
    }
}

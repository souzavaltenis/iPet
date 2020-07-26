package com.example.ipet.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.entities.Caso;
import com.example.ipet.entities.Ong;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public CasoUtils(FirebaseFirestore db, List<Caso> casosOngs, RecyclerView.Adapter<T> rvAdapter,
                     Changes changes, Boolean filterOng, String emailOng) {
        this.db = db;
        this.casosOngs = casosOngs;
        this.rvAdapter = rvAdapter;
        this.changes = changes;
        this.filterOng = filterOng;
        this.emailOng = emailOng;
    }

    /*
    * Listener exclusivo para somente os casos de uma ong, caso haja alterações, atualiza a lista
    * usando o runActions.
    * */
    public void listenerCasosOng(){

        db.collection("ongs")
                .document(emailOng)
                .collection("casos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(value != null){
                            runActions(value.getDocumentChanges());
                        }
                    }
                });
    }

    /*
    * Listener que ficará responsável por ouvir modificações em todas coleções de casos, e caso
    * haja, atualiza a lista usando o runActions.
    * */
    public void listenerAllCasos(){

        db.collectionGroup("casos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if(value != null){
                            runActions(value.getDocumentChanges());
                        }
                    }
                });
    }

    /*
    * Recebe a lista de documentos captados no listener, pega cada um e ve o tipo de ação, e para
    * cada ação, há um case no switch, fazendo suas respectivas alterações com os métodos: addDoc,
    * removeDoc, modifyDoc.
    * */
    public void runActions(List<DocumentChange> docs){

        for(DocumentChange documentChange : docs){

            DocumentChange.Type tipoAcao = documentChange.getType();
            QueryDocumentSnapshot document = documentChange.getDocument();

            switch (tipoAcao){
                case ADDED: addDoc(document); break;
                case REMOVED: removeDoc(document); break;
                case MODIFIED: modifyDoc(document); break;
            }

        }
    }

    /*
    * Método que irá adicionar os dados de um documento caso na lista de casos.
    *
    * Primeiramente é necessário obter o email caso seja preciso pegar todos os casos,
    * para isso foi pegado o path do documento, ex: ongs/anjospet@gmail.com/casos/sfcj540983788fshj
    * usando split com '/' e pegando o index 1, que é exatamente o email da ong deste caso.
    *
    * Segundamente, é acessado o documento da ong do email escolhido, e obter os dados da ong,
    * depois só foi necessário criar um caso com os dados ja presente no QueryDocumentSnapshot
    * juntamente com o objeto ong, adicionando na lista de casos principal e avisando as views
    * que houve modificações usando o método sendSinalsToViews();
    * */
    public void addDoc(final QueryDocumentSnapshot document){

        final String email = filterOng ? emailOng : document.getReference().getPath().split("/")[1];

        db.collection("ongs")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Ong ong = task.getResult().toObject(Ong.class);

                        casosOngs.add(new Caso(
                           document.getString("id"),
                           document.getString("titulo"),
                           document.getString("descricao"),
                           document.getDouble("valor"),
                           ong
                        ));

                        sendSinalsToViews();
                    }
                });
    }


    /*
    * Método que irá apenas detectar o index do caso na lista de casos local, usando o método
    * getPosiCaso(), removendo usando este index, e avisando as views sobre a atualização com o
    * método sendSinalsToView();
    * */
    public void removeDoc(QueryDocumentSnapshot document){

        int posicao = getPosiCaso(document.getId());

        if(posicao != -1){
            casosOngs.remove(posicao);
            sendSinalsToViews();
        }
    }

    /*
     * Método que irá apenas detectar o index na lista de casos local, usando o método
     * getPosiCaso(), modificando os valores do caso usando este index, e avisando as views sobre
     * a atualização com o método sendSinalsToView();
     * */
    public void modifyDoc(QueryDocumentSnapshot document){

        int posicao = getPosiCaso(document.getId());

        if(posicao != -1){

            Caso caso = casosOngs.get(posicao);
            caso.setId(document.getString("id"));
            caso.setTitulo(document.getString("titulo"));
            caso.setDescricao(document.getString("descricao"));
            caso.setValor(document.getDouble("valor"));

            rvAdapter.notifyDataSetChanged();
        }
    }

    /*
    * Pega a posição na lista de casos que tenha o mesmo id passado como parâmetro, caso não ache,
    * retorne -1.
    * */
    public int getPosiCaso(String id){

        for(int i=0; i<casosOngs.size(); i++){
            if(casosOngs.get(i).getId().equals(id)){
                return i;
            }
        }

        return -1;
    }

    /*
    * Avisa ao adaptador do recyclerview que houve modificações na lista que ele esta usando, usando
    * o método notifyDataSetChanged() e caso a interface de changes não for nula, seta o textview
    * contador de casos com o valor atual do tamanho da lista.
    * */
    public void sendSinalsToViews(){

        rvAdapter.notifyDataSetChanged();

        if(changes != null){
            changes.setarQuantidadeCasos(casosOngs.size());
        }
    }
}

package com.example.ipet.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.example.ipet.entities.Caso;

import java.util.ArrayList;
import java.util.List;

public class RvTodosCasosOngAdapter extends RecyclerView.Adapter<RvTodosCasosOngAdapter.CasoViewHolder> {

    private final Context context;
    private List<Caso> casosOng;
    private final RvTodosCasosOngAdapter.CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onClickDetails(int position);
    }

    public RvTodosCasosOngAdapter(Context context, List<Caso> casosOng,
                                  RvTodosCasosOngAdapter.CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<Caso>();
        this.onClickListener = onClickListener;
    }

    @Override
    public  RvTodosCasosOngAdapter.CasoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_card_todos_casos,
                viewGroup, false);
        RvTodosCasosOngAdapter.CasoViewHolder holder = new  RvTodosCasosOngAdapter.CasoViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size() : 0;
    }

    @Override
    public void onBindViewHolder(final  RvTodosCasosOngAdapter.CasoViewHolder holder, final int position) {

        Caso caso = casosOng.get(position);

        holder.tvOng.setText(caso.getOng().getNome());
        holder.tvTitulo.setText(caso.getTitulo());
        holder.tvDescricao.setText(caso.getDescricao());
        holder.tvAnimalData.setText(caso.getNomeAnimal() + " (" + caso.getEspecie() + ")");
        holder.tvValor.setText(String.valueOf(caso.getValor()));

        holder.tvMaisDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClickDetails(position);
            }
        });
    }

    public static class CasoViewHolder extends RecyclerView.ViewHolder {

        TextView tvOng;
        TextView tvTitulo;
        TextView tvDescricao;
        TextView tvAnimalData;
        TextView tvValor;
        TextView tvMaisDetalhes;
        View view;

        public CasoViewHolder(View view) {
            super(view);
            this.view = view;
            tvOng = view.findViewById(R.id.tvOngData);
            tvTitulo = view.findViewById(R.id.tvTitleData);
            tvDescricao = view.findViewById(R.id.tvDescricaoData);
            tvAnimalData = view.findViewById(R.id.tvAnimalData);
            tvValor = view.findViewById(R.id.tvValorData);
            tvMaisDetalhes = view.findViewById(R.id.tvMaisDetalhes);
        }
    }

}

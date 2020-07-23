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

public class RvCasoOngAdapter extends RecyclerView.Adapter<RvCasoOngAdapter.CasoViewHolder> {

    private final Context context;
    private List<Caso> casosOng;
    private final CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onClickTrash(int position);
    }

    public RvCasoOngAdapter(Context context, List<Caso> casosOng,
                            CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<Caso>();
        this.onClickListener = onClickListener;
    }

    @Override
    public CasoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_card_caso,
                viewGroup, false);
        CasoViewHolder holder = new CasoViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size() : 0;
    }

    @Override
    public void onBindViewHolder(final CasoViewHolder holder, final int position) {

        Caso caso = casosOng.get(position);

        holder.tvOng.setText(caso.getOng().getNome());
        holder.tvTitulo.setText(caso.getTitulo());
        holder.tvDescricao.setText(caso.getDescricao());
        holder.tvValor.setText(String.valueOf(caso.getValor()));

        holder.trashTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.trashTv.setEnabled(false); //garante que não será mais "clicável" na espera
                onClickListener.onClickTrash(position);
            }
        });
    }

    public static class CasoViewHolder extends RecyclerView.ViewHolder {

        TextView tvOng;
        TextView tvTitulo;
        TextView tvDescricao;
        TextView tvValor;
        TextView trashTv;
        View view;

        public CasoViewHolder(View view) {
            super(view);
            this.view = view;
            tvOng = view.findViewById(R.id.tvOngData);
            tvTitulo = view.findViewById(R.id.tvTitleData);
            tvDescricao = view.findViewById(R.id.tvDescricaoData);
            tvValor = view.findViewById(R.id.tvValorData);
            trashTv = view.findViewById(R.id.trashTv);
        }
    }
}
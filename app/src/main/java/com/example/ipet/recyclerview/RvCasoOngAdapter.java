package com.example.ipet.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.example.ipet.entities.Caso;
import com.example.ipet.entities.CasoOng;
import com.example.ipet.entities.Ong;

import java.util.ArrayList;
import java.util.List;

public class RvCasoOngAdapter extends RecyclerView.Adapter<RvCasoOngAdapter.CasoViewHolder> {

    private final Context context;
    private List<CasoOng> casosOngs;
    private final EstadoOnClickListener onClickListener;

    public interface EstadoOnClickListener {
        void onClickCaso(CasoViewHolder holder, int id);
    }

    public RvCasoOngAdapter(Context context, List<CasoOng> casosOngs,
                            EstadoOnClickListener onClickListener) {
        this.context = context;
        this.casosOngs = casosOngs != null ? casosOngs : new ArrayList<CasoOng>();
        this.onClickListener = onClickListener;
    }

    @Override
    public CasoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_card_caso,
                viewGroup, false);
        CasoViewHolder holder = new CasoViewHolder(view);
        return holder;
    }

    public void addItem(CasoOng casoOng) {
        casosOngs.add(casoOng);
        notifyDataSetChanged();
    }

    public void addItems(List<CasoOng> casosOngs) {
        this.casosOngs = casosOngs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return casosOngs != null ? casosOngs.size() : 0;
    }

    @Override
    public void onBindViewHolder(final CasoViewHolder holder, final int position) {

        CasoOng casoOng = casosOngs.get(position);

        Caso caso = casoOng.getCaso();
        Ong ong = casoOng.getOng();

        holder.tvOng.setText(ong.getNome());
        holder.tvTitulo.setText(caso.getTitulo());
        holder.tvDescricao.setText(caso.getDescricao());
        holder.tvValor.setText(String.valueOf(caso.getValor()));

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickCaso(holder, position);
                }
            });
        }
    }

    public static class CasoViewHolder extends RecyclerView.ViewHolder {

        TextView tvOng;
        TextView tvTitulo;
        TextView tvDescricao;
        TextView tvValor;
        View view;

        public CasoViewHolder(View view) {
            super(view);
            this.view = view;
            tvOng = view.findViewById(R.id.tvOngData);
            tvTitulo = view.findViewById(R.id.tvTitleData);
            tvDescricao = view.findViewById(R.id.tvDescricaoData);
            tvValor = view.findViewById(R.id.tvValorData);
        }
    }
}
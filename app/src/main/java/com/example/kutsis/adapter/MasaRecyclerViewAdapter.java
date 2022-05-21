package com.example.kutsis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kutsis.R;
import com.example.kutsis.model.Masa;

import java.util.List;

public class MasaRecyclerViewAdapter extends RecyclerView.Adapter<MasaRecyclerViewAdapter.MasaViewHolder> {
    private static ClickListener clickListener;

    private List<Masa> masaList;
    class MasaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView lblAdi;
        MasaViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            lblAdi = view.findViewById(R.id.masaId);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAbsoluteAdapterPosition(), view);

        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAbsoluteAdapterPosition(), view);
            return false;
        }
    }
    public MasaRecyclerViewAdapter(List<Masa> masaList) {
        this.masaList = masaList;
    }
    @Override
    public MasaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_masa, parent, false);
        return new MasaViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MasaViewHolder holder, int position) {
        Masa masa = masaList.get(position);
        holder.lblAdi.setText("Masa : " + masa.getId());

    }
    @Override
    public int getItemCount() {
        return masaList.size();
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        MasaRecyclerViewAdapter.clickListener = clickListener;
    }


}
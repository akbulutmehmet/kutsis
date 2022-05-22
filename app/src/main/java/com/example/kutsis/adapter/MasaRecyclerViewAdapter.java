package com.example.kutsis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kutsis.R;
import com.example.kutsis.model.Masa;
import co.dift.ui.SwipeToAction;

import java.util.List;

public class MasaRecyclerViewAdapter extends RecyclerView.Adapter<MasaRecyclerViewAdapter.MasaViewHolder> {
    private List<Masa> masaList;
    public class MasaViewHolder extends SwipeToAction.ViewHolder {
        private TextView lblAdi;
        MasaViewHolder(View view) {
            super(view);
            lblAdi = view.findViewById(R.id.masaId);
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
        holder.data = masa;
    }
    @Override
    public int getItemCount() {
        return masaList.size();
    }
    @Override
    public long getItemId(int position) {
        Masa masa = masaList.get(position);
        return masa.getId();
    }

}
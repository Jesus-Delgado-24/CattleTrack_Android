package com.example.cattletrack.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cattletrack.R;
import com.example.cattletrack.models.Sector;

import java.util.List;

public class SectorListAdapter extends RecyclerView.Adapter<SectorListAdapter.SectorViewHolder> {
    private final Context context;
    private final List<Sector> sectorList;

    public SectorListAdapter(Context context, List<Sector> sectorList){
        this.context = context;
        this.sectorList = sectorList;
    }

    @NonNull
    @Override
    public SectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_item_negro, parent, false);
        return new SectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectorViewHolder holder, int position) {
        Sector sector = sectorList.get(position);
        holder.tvNombreSector.setText(sector.getNombre());
    }
    @Override
    public int getItemCount() {
        return sectorList != null ? sectorList.size() : 0;
    }

    public void setData(List<Sector> nuevaLista) {
        if (nuevaLista == null) {
            return;
        }
        this.sectorList.clear();
        this.sectorList.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    public static class SectorViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreSector;

        public SectorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreSector = itemView.findViewById(R.id.txtItemSpinner);
        }
    }
}
package com.example.cattletrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.models.Enfermedad;
import com.example.cattletrack.models.MonitoringRequest;

import java.util.List;

public class EnfermedadAdapter extends RecyclerView.Adapter<EnfermedadAdapter.ViewHolder> {

    private List<MonitoringRequest.EnfermedadItem> historial;
    private List<Enfermedad> listaEnfermedades;

    public EnfermedadAdapter(List<MonitoringRequest.EnfermedadItem> historial, List<Enfermedad> listaEnfermedades) {
        this.historial = historial;
        this.listaEnfermedades = listaEnfermedades;
    }

    public void setListaEnfermedades(List<Enfermedad> listaEnfermedades) {
        this.listaEnfermedades = listaEnfermedades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enfermedad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonitoringRequest.EnfermedadItem item = historial.get(position);

        String nombre = String.valueOf(item.getIdEnfermedad());
        if (listaEnfermedades != null) {
            for (Enfermedad e : listaEnfermedades) {
                if (e.getId() == item.getIdEnfermedad()) {
                    nombre = e.getNombre();
                    break;
                }
            }
        }

        holder.txtNombre.setText(nombre);

        String fecha = item.getFechaHora();
        if (fecha != null && fecha.contains("T")) {
            holder.txtFecha.setText(fecha.split("T")[0]);
        } else if (fecha != null && fecha.length() >= 10) {
            holder.txtFecha.setText(fecha.substring(0, 10));
        } else {
            holder.txtFecha.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return historial.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.lblNameAlimento);
            txtFecha = itemView.findViewById(R.id.lblCantidadAlimento);
        }
    }
}


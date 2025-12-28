package com.example.cattletrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cattletrack.R;
import com.example.cattletrack.models.*;
import java.util.List;

public class MonitoreoAdapter extends RecyclerView.Adapter<MonitoreoAdapter.MonitoreoViewHolder> {

    private List<MonitoreoItem> monitoreoList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(MonitoreoItem item);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MonitoreoAdapter(List<MonitoreoItem> monitoreoList) {
        this.monitoreoList = monitoreoList;
    }

    @NonNull
    @Override
    public MonitoreoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monitoreo, parent, false);
        return new MonitoreoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitoreoViewHolder holder, int position) {
        MonitoreoItem item = monitoreoList.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {

        return monitoreoList.size();
    }

    // Método para actualizar la lista de datos en el adaptador
    public void setMonitoreos(List<MonitoreoItem> nuevosMonitoreos) {
        this.monitoreoList.clear();
        if (nuevosMonitoreos != null) {
            this.monitoreoList.addAll(nuevosMonitoreos);
        }
        notifyDataSetChanged();
    }

    // ViewHolder
    static class MonitoreoViewHolder extends RecyclerView.ViewHolder {
        TextView ganadoId, fecha, temperatura, deshidratacion;

        public MonitoreoViewHolder(@NonNull View itemView) {
            super(itemView);
            ganadoId = itemView.findViewById(R.id.textViewGanadoId);
            fecha = itemView.findViewById(R.id.textViewFecha);
            temperatura = itemView.findViewById(R.id.textViewTemperatura);
            deshidratacion = itemView.findViewById(R.id.textViewDeshidratacion);
        }

        public void bind(MonitoreoItem item) {
            ganadoId.setText("Clave Ganado: " + item.getIdGanado());
            // Simplificamos la fecha, puedes formatearla mejor si quieres
            fecha.setText("Fecha: " + item.getFechaHora().substring(0, 10));
            temperatura.setText("Temp: " + item.getTemperatura() + " °C");
            deshidratacion.setText("Nivel Deshidratación: " + item.getNivelDeshidratacion());
        }
    }
}
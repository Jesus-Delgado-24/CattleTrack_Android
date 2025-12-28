package com.example.cattletrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.models.HistorialReproduccion;

import java.util.List;

public class HistorialReproduccionAdapter extends RecyclerView.Adapter<HistorialReproduccionAdapter.ViewHolder> {

    private Context context;
    private List<HistorialReproduccion> list;

    public HistorialReproduccionAdapter(Context ctx, List<HistorialReproduccion> list) {
        this.context = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        HistorialReproduccion item = list.get(position);

        // Mostrar solo la parte de la fecha (aaaa-mm-dd)
        String fechaGestion = item.getFechaGestion().substring(0, 10);
        String fechaNacimiento = item.getFechaNacimiento().substring(0, 10);

        h.lblIdVaca.setText("ID Vaca: " + item.getIdVaca());
        h.lblIdToro.setText("ID Toro: " + item.getIdToro());
        h.lblFechaGestion.setText("Fecha Gestión: " + fechaGestion);
        h.lblFechaNacimiento.setText("Fecha Nacimiento: " + fechaNacimiento);
        h.lblCriasHembras.setText("Hembras: " + item.getCriasHembras());
        h.lblCriasMachos.setText("Machos: " + item.getCriasMacho());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblIdVaca, lblIdToro, lblFechaGestion, lblFechaNacimiento, lblCriasHembras, lblCriasMachos;

        public ViewHolder(View v) {
            super(v);
            lblIdVaca = v.findViewById(R.id.lblIdVaca);
            lblIdToro = v.findViewById(R.id.lblIdToro);
            lblFechaGestion = v.findViewById(R.id.lblFechaGestion);
            lblFechaNacimiento = v.findViewById(R.id.lblFechaNacimiento);
            lblCriasHembras = v.findViewById(R.id.lblCriasHembras);
            lblCriasMachos = v.findViewById(R.id.lblCriasMachos);
        }
    }
}

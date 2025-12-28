package com.example.cattletrack.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.models.Almacen;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AlmacenListAdapter extends RecyclerView.Adapter<AlmacenListAdapter.MyViewHolder> {
    private Context context;
    private List<Almacen> resultAlmacen;

    public AlmacenListAdapter(Context context, List<Almacen> resultAlmacenList) {
        this.context = context;
        this.resultAlmacen = resultAlmacenList;
    }
    @NonNull
    @Override
    public AlmacenListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_almacen, parent, false);
        return new MyViewHolder(view);
    }

    public void ordenarPorId(boolean ascendente) {
        if (this.resultAlmacen != null && !this.resultAlmacen.isEmpty()) {

            Collections.sort(this.resultAlmacen, new Comparator<Almacen>() {
                @Override
                public int compare(Almacen o1, Almacen o2) {
                    if (ascendente) {
                        return Integer.compare(o1.getId_Alimento(), o2.getId_Alimento());
                    } else {
                        return Integer.compare(o2.getId_Alimento(), o1.getId_Alimento());
                    }
                }
            });
            notifyDataSetChanged();
        }
    }

    @SuppressLint("RecycleView")
    @Override
    public void onBindViewHolder(@NonNull AlmacenListAdapter.MyViewHolder holder, int position) {
        holder.lblid_Alimento.setText("Clave: "+String.valueOf(this.resultAlmacen.get(position).getId_Alimento()));
        holder.lblNombre.setText(String.valueOf(this.resultAlmacen.get(position).getNombre()));
        holder.lblTipo.setText(String.valueOf(this.resultAlmacen.get(position).getTipo()));
        holder.lblCantidad.setText(String.valueOf(this.resultAlmacen.get(position).getCantidad()));
    }
    @Override
    public int getItemCount() {
        if(this.resultAlmacen!=null)
        return this.resultAlmacen.size();
        return 0;
    }
    public void setResultAlmacen(List<Almacen> resultAlmacen) {
        this.resultAlmacen = resultAlmacen;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lblid_Alimento;
        TextView lblNombre;
        TextView lblTipo;
        TextView lblCantidad;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lblid_Alimento = itemView.findViewById(R.id.lblId);
            lblNombre = itemView.findViewById(R.id.lblNombre);
            lblTipo = itemView.findViewById(R.id.lblTipo);
            lblCantidad = itemView.findViewById(R.id.lblCantidad);
        }
    }
}

package com.example.cattletrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.cattletrack.models.AbastecerResult;

    public class AbastecerListAdapter extends RecyclerView.Adapter<com.example.cattletrack.adapter.AbastecerListAdapter.AbastecerViewHolder> {

        // Listener
        public interface OnAbastecerClickListener {
            void onItemClick(AbastecerResult item);
        }

        private OnAbastecerClickListener listener;

        public void setOnAbastecerClickListener(OnAbastecerClickListener listener) {
            this.listener = listener;
        }

        private List<AbastecerResult> abastecerList = new ArrayList<>();


        private final Map<Integer, String> mapaSectores;


        public AbastecerListAdapter(Map<Integer, String> mapaSectores) {
            this.mapaSectores = mapaSectores;
        }

        public void setAbastecerList(List<AbastecerResult> list) {
            this.abastecerList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public com.example.cattletrack.adapter.AbastecerListAdapter.AbastecerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_abastecer, parent, false);
            return new AbastecerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AbastecerViewHolder holder, int position) {

            AbastecerResult item = abastecerList.get(position);


            String nombreSector = mapaSectores.get(item.id_Sector);

            holder.tvSector.setText("Sector: " + (nombreSector != null ? nombreSector : "Desconocido"));
            holder.tvAlimento.setText("Alimento ID: " + item.id_Alimento);
            holder.tvCantidad.setText("Cantidad: " + item.Cantidad);
            holder.tvFecha.setText("Fecha: " + item.Fecha_hora);

            holder.btnEditar.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(abastecerList.get(pos));
                }
            });
        }

        @Override
        public int getItemCount() {
            return abastecerList.size();
        }

        static class AbastecerViewHolder extends RecyclerView.ViewHolder {

            TextView tvSector, tvAlimento, tvCantidad, tvFecha;
            Button btnEditar;

            public AbastecerViewHolder(@NonNull View itemView) {
                super(itemView);

                tvSector = itemView.findViewById(R.id.txtSector);
                tvAlimento = itemView.findViewById(R.id.txtAlimento);
                tvCantidad = itemView.findViewById(R.id.txtCantidad);
                tvFecha = itemView.findViewById(R.id.txtFecha);
                btnEditar = itemView.findViewById(R.id.btnEditar);
            }
        }
    }



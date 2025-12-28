package com.example.cattletrack.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.models.Leche;
import com.example.cattletrack.models.Sector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class LecheListAdapter extends RecyclerView.Adapter<LecheListAdapter.MyViewHolder> {
    private int userId, userType;
    private Context context;
    private List<Leche> lecheList;
    private OnItemClickListener listener;
    private Map<Integer, String> sectorMap;

    public interface OnItemClickListener {
        void onEditClick(Leche leche);
    }

    public LecheListAdapter(Context context, List<Leche> lecheList, List<Sector> sectorList, OnItemClickListener listener) {
        this.context = context;
        this.lecheList = lecheList != null ? lecheList : new ArrayList<>();
        this.listener = listener;
        updateSectorMap(sectorList);
    }

    public void updateSectorMap(List<Sector> sectorList) {
        if (sectorList != null && !sectorList.isEmpty()) {
            try {
                this.sectorMap = sectorList.stream()
                        .collect(Collectors.toMap(Sector::getId_Sector, Sector::getNombre, (oldValue, newValue) -> oldValue));
            } catch (Exception e) {
                this.sectorMap = new HashMap<>();
                for (Sector s : sectorList) {
                    this.sectorMap.put(s.getId_Sector(), s.getNombre());
                }
            }
        } else {
            this.sectorMap = new HashMap<>();
        }
        notifyDataSetChanged();
    }

    public void updateData(List<Leche> nuevaListaLeche, List<Sector> nuevaListaSectores) {
        if (nuevaListaLeche == null) nuevaListaLeche = new ArrayList<>();
        this.lecheList.clear();
        this.lecheList.addAll(nuevaListaLeche);
        updateSectorMap(nuevaListaSectores);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produccion, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Leche leche = lecheList.get(position);
        SharedPreferences sharedPref = context.getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("userId", 0);
        userType = sharedPref.getInt("userType", 0);

        if(userType == 4 || userType == 1){
            holder.btnEditar.setVisibility(View.GONE);
        }

        int id_S = leche.getId_Sector();
        String nombreSector = sectorMap.getOrDefault(id_S, "ID: " + id_S);
        holder.tvIdSector.setText(nombreSector);

        holder.tvIdRegistro.setText("Clave: " + leche.getId_L());
        holder.tvCantidad.setText("Cantidad: " + leche.getCantidad() + " L");
        String fechaFormateada = formatearFecha(leche.getFecha());
        holder.tvFecha.setText("Fecha: " + fechaFormateada);

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(leche);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lecheList != null ? lecheList.size() : 0;
    }

    public void setData(List<Leche> nuevaLista) {
        if (nuevaLista == null) {
            this.lecheList = new ArrayList<>();
        } else {
            this.lecheList = new ArrayList<>(nuevaLista);
        }
        notifyDataSetChanged();
    }

    /**
     * Ordena la lista interna por ID de leche.
     * ascending == true -> ascendente (IDs pequeños primero),
     * ascending == false -> descendente (IDs grandes primero).
     */
    public void sortById(boolean ascending) {
        if (lecheList == null || lecheList.size() <= 1) return;

        Collections.sort(lecheList, new Comparator<Leche>() {
            @Override
            public int compare(Leche l1, Leche l2) {
                // Suponiendo getId_L() devuelve int. Protegemos contra nulls por si acaso.
                if (l1 == null && l2 == null) return 0;
                if (l1 == null) return ascending ? -1 : 1;
                if (l2 == null) return ascending ? 1 : -1;
                Integer id1 = null;
                Integer id2 = null;
                try { id1 = l1.getId_L(); } catch (Exception ignored) {}
                try { id2 = l2.getId_L(); } catch (Exception ignored) {}
                if (id1 == null && id2 == null) return 0;
                if (id1 == null) return ascending ? -1 : 1;
                if (id2 == null) return ascending ? 1 : -1;
                return ascending ? id1.compareTo(id2) : id2.compareTo(id1);
            }
        });
        notifyDataSetChanged();
    }

    // parseo de fecha y formateo (se mantiene)
    private Date parseDateFromIso(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return null;
        String[] patterns = new String[] {
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd"
        };
        for (String p : patterns) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat(p, Locale.getDefault());
                if (p.endsWith("'Z'") || p.contains("XXX")) {
                    parser.setTimeZone(TimeZone.getTimeZone("UTC"));
                }
                return parser.parse(fechaISO);
            } catch (ParseException ignored) { }
        }
        return null;
    }

    private String formatearFecha(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) {
            return "Fecha no disponible";
        }

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date fechaConvertida = parser.parse(fechaISO);
            return formatter.format(fechaConvertida);
        } catch (ParseException e) {
            Log.e("LecheListAdapter", "Error al parsear la fecha: " + fechaISO, e);
            return fechaISO.length() >= 10 ? fechaISO.substring(0, 10) : fechaISO;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdRegistro, tvIdSector, tvCantidad, tvFecha;
        ImageButton btnEditar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdRegistro = itemView.findViewById(R.id.lblId_LL);
            tvIdSector = itemView.findViewById(R.id.lblId_SL);
            tvCantidad = itemView.findViewById(R.id.lblCantidadL);
            tvFecha = itemView.findViewById(R.id.lblFechaL);
            btnEditar = itemView.findViewById(R.id.btnDelL);
        }
    }
}
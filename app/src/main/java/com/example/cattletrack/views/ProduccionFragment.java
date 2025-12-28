package com.example.cattletrack.views;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cattletrack.R;
import com.example.cattletrack.adapter.LecheListAdapter;
import com.example.cattletrack.models.Leche;
import com.example.cattletrack.models.ResultLeche;
import com.example.cattletrack.models.ResultSector;
import com.example.cattletrack.models.Sector;
import com.example.cattletrack.viewmodel.ListLecheViewModel;
import com.example.cattletrack.viewmodel.ListSectorViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ProduccionFragment extends Fragment implements LecheListAdapter.OnItemClickListener {

    private RecyclerView recyclerViewProduccion;
    private LecheListAdapter lecheListAdapter;
    private ListLecheViewModel viewModelL;
    private ListSectorViewModel viewModelS;
    private List<Sector> listaSectoresApi = new ArrayList<>();
    private int userId, userType;
    private List<Leche> currentLecheList = new ArrayList<>();
    private boolean sortAscending = true;

    public ProduccionFragment() { }

    public static ProduccionFragment newInstance() {
        return new ProduccionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelL = new ViewModelProvider(this).get(ListLecheViewModel.class);
        viewModelS = new ViewModelProvider(this).get(ListSectorViewModel.class);
        // Leer datos de sesión
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("userId", 0);
        userType = sharedPref.getInt("userType", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_produccion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(view);

        AppCompatButton btnOrdenFecha = view.findViewById(R.id.btnOrdenFecha);
        view.findViewById(R.id.btnAgregar_P).setOnClickListener(v -> showProduccionDialog(null));

        AppCompatButton Agregar = view.findViewById(R.id.btnAgregar_P);
        if(userType == 4 || userType == 1){
            Agregar.setVisibility(View.GONE);
        }

        // Toggle orden al pulsar el botón
        btnOrdenFecha.setText(sortAscending ? "Fecha ↑" : "Fecha ↓");
        btnOrdenFecha.setOnClickListener(v -> {
            sortAscending = !sortAscending;
            btnOrdenFecha.setText(sortAscending ? "Fecha ↑" : "Fecha ↓");
            lecheListAdapter.sortById(sortAscending);
            Log.e("ProduccionFragment", "user: " + userId +" type: " + userType);
        });

        // Observer sectores
        viewModelS.getSector().observe(getViewLifecycleOwner(), resultSector -> {
            if (resultSector != null && resultSector.getResult() != null) {
                listaSectoresApi.clear();
                listaSectoresApi.addAll(resultSector.getResult());
                lecheListAdapter.updateSectorMap(listaSectoresApi);
            }
        });

        // Observer leche: guardamos la lista y aplicamos orden
        viewModelL.getLeche().observe(getViewLifecycleOwner(), resultLeche -> {
            if (resultLeche != null && resultLeche.getData() != null) {
                currentLecheList = new ArrayList<>(resultLeche.getData());
                lecheListAdapter.updateData(currentLecheList, listaSectoresApi);
            }
        });

        // Central para respuestas de acción (POST/DELETE) — refresca la lista
        viewModelL.getActionResponse().observe(getViewLifecycleOwner(), responseLeche -> {
            if (responseLeche == null) return;
            // Mostrar mensaje si existe y refrescar
            try {
                if (responseLeche.getMensaje() != null) {
                    Toast.makeText(getContext(), responseLeche.getMensaje(), Toast.LENGTH_SHORT).show();
                } else if (responseLeche.getMessage() != null) {
                    Toast.makeText(getContext(), responseLeche.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ignored) {}
            viewModelL.CallServiceGetLeche(userId, userType);
        });

        viewModelS.CallServiceGetSector(userId, userType);
        viewModelL.CallServiceGetLeche(userId, userType);
    }

    private void setupRecyclerView(View view) {
        recyclerViewProduccion = view.findViewById(R.id.recyclerViewProduccion);
        recyclerViewProduccion.setLayoutManager(new LinearLayoutManager(getContext()));
        lecheListAdapter = new LecheListAdapter(getContext(), new ArrayList<>(), listaSectoresApi, this);
        recyclerViewProduccion.setAdapter(lecheListAdapter);
    }

    // Aplica el orden sobre currentLecheList y actualiza adapter con una copia
    private void applySortAndUpdateAdapter() {
        if (currentLecheList == null) return;
        List<Leche> copia = new ArrayList<>(currentLecheList);
        Collections.sort(copia, new Comparator<Leche>() {
            @Override
            public int compare(Leche l1, Leche l2) {
                Date d1 = parseDateFromIso(l1 != null ? l1.getFecha() : null);
                Date d2 = parseDateFromIso(l2 != null ? l2.getFecha() : null);
                if (d1 == null && d2 == null) return 0;
                if (d1 == null) return sortAscending ? -1 : 1; // nulls al final o inicio según orden
                if (d2 == null) return sortAscending ? 1 : -1;
                return sortAscending ? d1.compareTo(d2) : d2.compareTo(d1);
            }
        });
        lecheListAdapter.setData(copia); // setData debe notificar cambios
    }

    // Parseo robusto de fecha ISO -> Date
    private Date parseDateFromIso(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return null;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return parser.parse(fechaISO);
        } catch (ParseException e) {
            // Intentar con variantes si tu API devuelve otras precisiones
            try {
                SimpleDateFormat parser2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                parser2.setTimeZone(TimeZone.getTimeZone("UTC"));
                return parser2.parse(fechaISO);
            } catch (ParseException ex) {
                Log.e("ProduccionFragment", "No se pudo parsear fecha: " + fechaISO, ex);
                return null;
            }
        }
    }

    @Override
    public void onEditClick(Leche leche) {
        showProduccionDialog(leche);
    }

    private void showProduccionDialog(@Nullable Leche leche) {
        // 1. Inflar la vista del diálogo (esto está bien)
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_produccion, null);

        // 2. Encontrar las vistas que SÍ EXISTEN
        ImageButton btnCancelar = dialogView.findViewById(R.id.btn_cancelar_P);
        AppCompatButton btnConfirmar = dialogView.findViewById(R.id.btn_cancelar_P2);
        Spinner spinnerSector = dialogView.findViewById(R.id.spinner_idSector_P);
        TextInputEditText txtCantidad = dialogView.findViewById(R.id.txt_cantidad_P);
        TextView txtIdInvisible = dialogView.findViewById(R.id.lblAlertProduccion);
        TextInputLayout txtFechaVisible = dialogView.findViewById(R.id.txtFechaVisible);
        TextInputEditText txtFecha = dialogView.findViewById(R.id.txt_fecha_P);
        AppCompatButton btnEliminar = dialogView.findViewById(R.id.btn_Eliminar_P);

        // 3. Crear el AlertDialog
        final AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        // Preparar spinner con los nombres actuales (se usa la lista compartida listaSectoresApi)
        List<String> nombresDeSectores = new ArrayList<>();
        for (Sector sector : listaSectoresApi) {
            nombresDeSectores.add(sector.getNombre());
        }

        ArrayAdapter<String> adapterSectores = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_negro, nombresDeSectores);
        adapterSectores.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSector.setAdapter(adapterSectores);

        if (leche != null) {
            // Modo Editar
            txtIdInvisible.setVisibility(VISIBLE);
            txtFechaVisible.setVisibility(VISIBLE);
            btnEliminar.setVisibility(VISIBLE);
            btnConfirmar.setVisibility(VISIBLE);
            btnCancelar.setVisibility(GONE);
            spinnerSector.setEnabled(false);
            txtCantidad.setEnabled(false);
            txtFecha.setText(leche.getFecha());
            String fecha = formatearFecha(txtFecha.getText().toString());
            txtFecha.setText(fecha);
            txtIdInvisible.setText("¿Deseas eliminar este registro?");
            txtCantidad.setText(leche.getCantidad());

            int idSectorASeleccionar = leche.getId_Sector();
            int posicionDelSector = -1;

            for (int i = 0; i < listaSectoresApi.size(); i++) {
                if (listaSectoresApi.get(i).getId_Sector() == idSectorASeleccionar) {
                    posicionDelSector = i;
                    break;
                }
            }

            if (posicionDelSector != -1) {
                spinnerSector.setSelection(posicionDelSector);
            } else {
                Toast.makeText(getContext(), "El sector original no se encontró", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Modo Agregar
            txtIdInvisible.setVisibility(GONE);
            txtFechaVisible.setVisibility(GONE);
            btnEliminar.setVisibility(GONE);
            btnConfirmar.setVisibility(VISIBLE);
            spinnerSector.setEnabled(true);
            txtCantidad.setEnabled(true);
            btnConfirmar.setText("Agregar");
        }

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            String cantidadStr = txtCantidad.getText().toString().trim();

            if (leche == null) {
                if (cantidadStr.isEmpty()) {
                    Toast.makeText(getContext(), "El campo de cantidad no puede estar vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                double cantidadVal;
                try {
                    cantidadVal = Double.parseDouble(cantidadStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Cantidad inválida", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cantidadVal <= 0) {
                    Toast.makeText(getContext(), "La cantidad debe ser mayor que cero", Toast.LENGTH_SHORT).show();
                    return;
                }

                int posicionSeleccionada = spinnerSector.getSelectedItemPosition();

                if (posicionSeleccionada < 0 || posicionSeleccionada >= listaSectoresApi.size()) {
                    Toast.makeText(getContext(), "Error: sector no válido o lista no cargada", Toast.LENGTH_SHORT).show();
                    return;
                }
                Sector sectorSeleccionado = listaSectoresApi.get(posicionSeleccionada);
                int idDelSector = sectorSeleccionado.getId_Sector();

                // Llamamos al servicio; el observer centralizado en onViewCreated refrescará la lista cuando la acción complete.
                viewModelL.CallServicePostLeche(idDelSector, cantidadStr);
            }
            dialog.dismiss();
        });

        btnEliminar.setOnClickListener(v -> {
            if (leche != null) {
                String id_L = String.valueOf(leche.getId_L());
                // Llamamos al servicio de delete; el observer global refrescará la lista
                viewModelL.CallServiceDeleteLeche(id_L);
            }
            dialog.dismiss();
        });

        dialog.show();
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
}
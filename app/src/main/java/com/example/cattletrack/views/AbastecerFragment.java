package com.example.cattletrack.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.adapter.AbastecerListAdapter;
import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.models.Abastecer;
import com.example.cattletrack.models.AbastecerResponse;
import com.example.cattletrack.models.AbastecerResult;
import com.example.cattletrack.models.AlimentoResponse;
import com.example.cattletrack.models.AlimentoResult;
import com.example.cattletrack.models.SectorResponse;
import com.example.cattletrack.models.SectorResult;
import com.example.cattletrack.service.ServicesRetrofit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbastecerFragment extends Fragment {

    private Spinner spinnerSector, spinnerAlimento;
    private EditText etFecha, etCantidad;
    private Button btnAbastecer;
    private RecyclerView recyclerView;

    private AbastecerListAdapter adapter;
    private LocalNetworkAPI api;

    private String idEditando = null;
    private ArrayAdapter<SectorResult> adapterSector;
    private ArrayAdapter<AlimentoResult> adapterAlimento;
    private boolean spinnersListos = false;


    private final Map<Integer, String> mapaSectores = new HashMap<>();


    public AbastecerFragment() {}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_abastecer, container, false);

        spinnerSector = view.findViewById(R.id.spinnerSector);
        spinnerAlimento = view.findViewById(R.id.spinnerAlimento);
        etCantidad = view.findViewById(R.id.etCantidad);
        etFecha = view.findViewById(R.id.tvFecha);
        btnAbastecer = view.findViewById(R.id.btnAbastecer);
        recyclerView = view.findViewById(R.id.recyclerView);

        api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);

        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etFecha.setText(hoy);
        etFecha.setEnabled(false);

        configurarSpinners();
        configurarRecyclerView();
        cargarAbastecimientos();

        btnAbastecer.setOnClickListener(v -> {
            if (idEditando != null) {
                actualizarAbastecimiento();
            } else {
                crearAbastecimiento();
            }
        });

        return view;
    }

    // ===================== SPINNERS =====================
    private void configurarSpinners() {

        // ---------------- Spinner Sector ----------------
        adapterSector = new ArrayAdapter<>(requireContext(), R.layout.spinnersv_item, new ArrayList<>());
        adapterSector.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSector.setAdapter(adapterSector);
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        int userType = sharedPref.getInt("userType", -1);
        api.getSectores(userId,userType).enqueue(new Callback<SectorResponse>() {
            @Override
            public void onResponse(Call<SectorResponse> call, Response<SectorResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    adapterSector.clear();
                    adapterSector.addAll(response.body().data);
                    adapterSector.notifyDataSetChanged();
                    mapaSectores.clear();
                    for (SectorResult s : response.body().data) {
                        mapaSectores.put(s.id, s.Nombre);
                    }

                    spinnersListos = true;

                    Log.d("MAPA_SECTORES", "Mapa cargado: " + mapaSectores.toString());
                } else {
                    Log.e("MAPA_SECTORES", "Error: No llegaron sectores");
                }
            }

            @Override
            public void onFailure(Call<SectorResponse> call, Throwable t) {
                Log.e("MAPA_SECTORES", "Fallo API sectores: " + t.getMessage());
            }
        });


        // ---------------- Spinner Alimento ----------------
        adapterAlimento = new ArrayAdapter<>(requireContext(), R.layout.spinnersv_item, new ArrayList<>());
        adapterAlimento.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAlimento.setAdapter(adapterAlimento);

        api.getAlimentos().enqueue(new Callback<AlimentoResponse>() {
            @Override
            public void onResponse(Call<AlimentoResponse> call, Response<AlimentoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    adapterAlimento.clear();
                    adapterAlimento.addAll(response.body().data);
                    adapterAlimento.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<AlimentoResponse> call, Throwable t) {
                Log.e("API_ALIM_ERROR", t.getMessage());
            }
        });
    }

    // ===================== RECYCLER crea adapter a lista de abastecimientos =====================
    private void configurarRecyclerView() {

        adapter = new AbastecerListAdapter(mapaSectores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnAbastecerClickListener(item -> cargarDatosParaEditar(item));
    }

    // ===================== CARGAR DATOS PARA EDITAR =====================
    private void cargarDatosParaEditar(AbastecerResult item) {
        idEditando = item.idMongo;
        etCantidad.setText(String.valueOf(item.Cantidad));
        etFecha.setText(item.Fecha_hora);


        for (int i = 0; i < spinnerSector.getCount(); i++) {
            if (((SectorResult)spinnerSector.getItemAtPosition(i)).id == item.id_Sector) {
                spinnerSector.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < spinnerAlimento.getCount(); i++) {
            if (((AlimentoResult)spinnerAlimento.getItemAtPosition(i)).id == item.id_Alimento) {
                spinnerAlimento.setSelection(i);
                break;
            }
        }

        btnAbastecer.setText("Actualizar");
    }

    // ===================== CARGAR LISTA =====================
    private void cargarAbastecimientos() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        int userType = sharedPref.getInt("userType", -1);
        api.getAbastecer(userId,userType).enqueue(new Callback<AbastecerResponse>() {
            @Override
            public void onResponse(Call<AbastecerResponse> call, Response<AbastecerResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    adapter.setAbastecerList(response.body().data);
                }
            }
            @Override
            public void onFailure(Call<AbastecerResponse> call, Throwable t) {
                Log.e("API_ABASTECER_ERROR", t.getMessage(), t);
            }
        });
    }

    // ===================== CREAR =====================
    private void crearAbastecimiento() {
        if (!spinnersListos) {
            Toast.makeText(getContext(), "Espera a que carguen los sectores", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etCantidad.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        SectorResult sector = (SectorResult) spinnerSector.getSelectedItem();
        AlimentoResult alimento = (AlimentoResult) spinnerAlimento.getSelectedItem();
        double cantidad = Double.parseDouble(etCantidad.getText().toString().trim());
        String fecha = etFecha.getText().toString();

        Abastecer nuevo = new Abastecer(sector.id, alimento.id, cantidad, fecha);

        api.postAbastecer(nuevo).enqueue(new Callback<AbastecerResponse>() {
            @Override
            public void onResponse(Call<AbastecerResponse> call, Response<AbastecerResponse> response) {
                Toast.makeText(getContext(), "Registrado", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarAbastecimientos();
            }

            @Override
            public void onFailure(Call<AbastecerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al registrar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ===================== ACTUALIZAR =====================
    private void actualizarAbastecimiento() {
        if (etCantidad.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        SectorResult sector = (SectorResult) spinnerSector.getSelectedItem();
        AlimentoResult alimento = (AlimentoResult) spinnerAlimento.getSelectedItem();
        double cantidad = Double.parseDouble(etCantidad.getText().toString().trim());
        String fecha = etFecha.getText().toString();

        Abastecer actualizado = new Abastecer(sector.id, alimento.id, cantidad, fecha);

        api.putAbastecer(idEditando, actualizado).enqueue(new Callback<AbastecerResponse>() {
            @Override
            public void onResponse(Call<AbastecerResponse> call, Response<AbastecerResponse> response) {
                Toast.makeText(getContext(), "Registro actualizado", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarAbastecimientos();
            }

            @Override
            public void onFailure(Call<AbastecerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al actualizar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ===================== LIMPIAR =====================
    private void limpiarFormulario() {
        idEditando = null;
        etCantidad.setText("");
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etFecha.setText(hoy);
        spinnerSector.setSelection(0);
        spinnerAlimento.setSelection(0);
        btnAbastecer.setText("Abastecer");
    }
}

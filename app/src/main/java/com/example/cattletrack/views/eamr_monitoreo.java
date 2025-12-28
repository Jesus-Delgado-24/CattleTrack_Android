package com.example.cattletrack.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.models.Enfermedad;
import com.example.cattletrack.adapter.EnfermedadAdapter;
import com.example.cattletrack.models.MonitoringRequest;
import com.example.cattletrack.models.MonitoringRequest.EnfermedadItem;
import com.example.cattletrack.models.MonitoringResponse;
import com.example.cattletrack.models.GanadoResponse;
import com.example.cattletrack.models.GanadoResult;
import com.example.cattletrack.service.ServicesRetrofit;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.cattletrack.models.ModalAgregarEnfermedad;

public class eamr_monitoreo extends Fragment
        implements ModalAgregarEnfermedad.EnfermedadListener {

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String data);
        void onLoadFragment(Fragment fragment);
    }

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private EnfermedadAdapter recyclerAdapter;
    private List<EnfermedadItem> historial = new ArrayList<>();
    private List<Enfermedad> listaEnfermedades = new ArrayList<>();

    private Spinner spinnerGanado, spinnerDeshidratacion;
    private ArrayAdapter<Integer> adapterGanado;
    private List<Integer> listaGanado = new ArrayList<>();

    private MaterialCheckBox chkNutricion, chkEstadoFisico, chkHistorialClinico;
    private TextInputEditText edtTemperatura, edtFrecuencia;
    private AppCompatButton btnAgregarEnfermedad, btnGuardar, btnCancelar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eamr_monitoreo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.listView);
        recyclerAdapter = new EnfermedadAdapter(historial, listaEnfermedades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        // Spinner Ganado
        spinnerGanado = view.findViewById(R.id.spinner_ganado);
        adapterGanado = new ArrayAdapter<>(requireContext(),
                R.layout.spinnersv_item, listaGanado);
        adapterGanado.setDropDownViewResource(R.layout.spinnerr_dropdown_item);
        spinnerGanado.setAdapter(adapterGanado);
        cargarGanado();

        // Spinner Deshidratación
        spinnerDeshidratacion = view.findViewById(R.id.spinner_nivel_deshidratacion);
        String[] nivelesApi = {"Leve", "Moderado", "Severo"};
        ArrayAdapter<String> adapterDeshidratacion = new ArrayAdapter<>(getContext(),
                R.layout.spinnersv_item, nivelesApi);
        adapterDeshidratacion.setDropDownViewResource(R.layout.spinnerr_dropdown_item);
        spinnerDeshidratacion.setAdapter(adapterDeshidratacion);

        // Campos de texto
        edtTemperatura = view.findViewById(R.id.edt_temperatura);
        edtFrecuencia = view.findViewById(R.id.edt_frecuencia);

        // Checkboxes
        chkNutricion = view.findViewById(R.id.chk_nutricion);
        chkEstadoFisico = view.findViewById(R.id.chk_estado_fisico);
        chkHistorialClinico = view.findViewById(R.id.chk_historial_clinico);

        // Botones
        btnAgregarEnfermedad = view.findViewById(R.id.btn_agregar_enfermedad);
        btnGuardar = view.findViewById(R.id.btn_guardar);
        btnCancelar = view.findViewById(R.id.btn_cancelar);

        cargarListaEnfermedades();

        btnAgregarEnfermedad.setOnClickListener(v -> {
            ModalAgregarEnfermedad modal = new ModalAgregarEnfermedad();
            modal.setEnfermedadListener(this);
            modal.show(getParentFragmentManager(), "modal_enfermedad");
        });

        btnGuardar.setOnClickListener(v -> enviarMonitoreo());
        btnCancelar.setOnClickListener(v -> limpiarFormulario());
    }

    // ==================== CARGAR LISTA DE ENFERMEDADES ====================
    private void cargarListaEnfermedades() {
        LocalNetworkAPI api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
        api.getEnfermedades().enqueue(new Callback<com.example.cattletrack.models.EnfermedadResponse>() {
            @Override
            public void onResponse(Call<com.example.cattletrack.models.EnfermedadResponse> call,
                                   Response<com.example.cattletrack.models.EnfermedadResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    listaEnfermedades = response.body().data;
                    recyclerAdapter.setListaEnfermedades(listaEnfermedades);
                }
            }

            @Override
            public void onFailure(Call<com.example.cattletrack.models.EnfermedadResponse> call, Throwable t) {
                Log.e("MONITOREO_FRAGMENT", "Fallo conexión: " + t.getMessage());
            }
        });
    }

    // ==================== CARGAR SPINNER GANADO ====================
    private void cargarGanado() {
        // Obtener userId y userType desde SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        int userType = sharedPref.getInt("userType", -1);

        if (userId == -1 || userType == -1) {
            Toast.makeText(getContext(), "Error de sesión. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            return;
        }

        LocalNetworkAPI api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
        api.getGanados(userId, userType).enqueue(new Callback<GanadoResponse>() {
            @Override
            public void onResponse(Call<GanadoResponse> call, Response<GanadoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    listaGanado.clear();
                    for (GanadoResult g : response.body().data) {
                        listaGanado.add(g.id);
                    }
                    adapterGanado.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No se encontraron registros de ganado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GanadoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar ganado: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ==================== ENVIAR MONITOREO ====================
    private void enviarMonitoreo() {
        if (spinnerGanado.getSelectedItem() == null ||
                TextUtils.isEmpty(edtTemperatura.getText()) ||
                TextUtils.isEmpty(edtFrecuencia.getText())) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener userId y userType desde SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        int userType = sharedPref.getInt("userType", -1);

        if (userId == -1 || userType == -1) {
            Toast.makeText(getContext(), "Error de sesión. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            return;
        }

        int idGanadoSeleccionado = (Integer) spinnerGanado.getSelectedItem();
        double temperatura = Double.parseDouble(edtTemperatura.getText().toString().trim());
        int frecuenciaCardiaca = Integer.parseInt(edtFrecuencia.getText().toString().trim());
        String nivelDeshidratacion = spinnerDeshidratacion.getSelectedItem().toString();

        List<String> desgloseEnviar = new ArrayList<>();
        if (chkNutricion.isChecked()) desgloseEnviar.add("Chequeo nutricional");
        if (chkEstadoFisico.isChecked()) desgloseEnviar.add("Evaluación estado físico");
        if (chkHistorialClinico.isChecked()) desgloseEnviar.add("Revisión historial clínico");

        MonitoringRequest request = new MonitoringRequest(
                userId,  // ahora se usa SharedPreferences
                idGanadoSeleccionado,
                temperatura,
                frecuenciaCardiaca,
                0,
                nivelDeshidratacion,
                desgloseEnviar,
                historial
        );

        String json = new Gson().toJson(request);
        Log.d("MONITOREO_JSON_ENVIO", json);

        LocalNetworkAPI api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
        api.postMonitoring(request).enqueue(new Callback<MonitoringResponse>() {
            @Override
            public void onResponse(Call<MonitoringResponse> call, Response<MonitoringResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Monitoreo guardado", Toast.LENGTH_LONG).show();
                    limpiarFormulario();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("MONITOREO_API", "Error código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MonitoringResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error conexión ", Toast.LENGTH_LONG).show();
                Log.e("MONITOREO_API", t.getMessage());
            }
        });
    }

    private void limpiarFormulario() {
        historial.clear();
        recyclerAdapter.notifyDataSetChanged();
        spinnerGanado.setSelection(0);
        edtTemperatura.setText("");
        edtFrecuencia.setText("");
        spinnerDeshidratacion.setSelection(0);
        chkNutricion.setChecked(false);
        chkEstadoFisico.setChecked(false);
        chkHistorialClinico.setChecked(false);
    }

    @Override
    public void onEnfermedadAgregada(int idEnfermedad, String fecha) {
        historial.add(new EnfermedadItem(idEnfermedad, fecha));
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
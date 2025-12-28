package com.example.cattletrack.models;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.cattletrack.R;
import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.service.ServicesRetrofit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModalAgregarEnfermedad extends DialogFragment {

    public interface EnfermedadListener {
        void onEnfermedadAgregada(int idEnfermedad, String fecha);
    }

    private EnfermedadListener listener;

    private Spinner spinnerEnfermedades;
    private AppCompatButton btnAgregar;
    private TextView tvFecha;
    private Calendar calendar = Calendar.getInstance();
    private String fechaSeleccionada;

    private List<Enfermedad> listaEnfermedades = new ArrayList<>();

    public void setEnfermedadListener(EnfermedadListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_agregar_enfermedad, container, false);

        spinnerEnfermedades = view.findViewById(R.id.spinner_enfermedades);
        btnAgregar = view.findViewById(R.id.btn_agregar);
        tvFecha = view.findViewById(R.id.tv_fecha_seleccionada);

        cargarEnfermedades();

        tvFecha.setOnClickListener(v -> mostrarDatePicker());

        btnAgregar.setOnClickListener(v -> {
            int position = spinnerEnfermedades.getSelectedItemPosition();
            if (position < 0 || TextUtils.isEmpty(fechaSeleccionada)) {
                Toast.makeText(getContext(), "Selecciona enfermedad y fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            int idEnfermedad = listaEnfermedades.get(position).getId();

            if (listener != null) {
                listener.onEnfermedadAgregada(idEnfermedad, fechaSeleccionada);
            }

            dismiss();
        });

        return view;
    }

    private void mostrarDatePicker() {
        new DatePickerDialog(requireContext(),
                (datePicker, year, month, day) -> {
                    calendar.set(year, month, day);
                    fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(calendar.getTime());
                    tvFecha.setText(fechaSeleccionada);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void cargarEnfermedades() {
        LocalNetworkAPI api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);

        api.getEnfermedades().enqueue(new Callback<EnfermedadResponse>() {
            @Override
            public void onResponse(Call<EnfermedadResponse> call, Response<EnfermedadResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    listaEnfermedades = response.body().data;

                    List<String> nombres = new ArrayList<>();
                    for (Enfermedad e : listaEnfermedades) {
                        nombres.add(e.getNombre());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            R.layout.spinnersv_item, nombres);
                    adapter.setDropDownViewResource(R.layout.spinnerr_dropdown_item);
                    spinnerEnfermedades.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<EnfermedadResponse> call, Throwable t) { }
        });
    }
}

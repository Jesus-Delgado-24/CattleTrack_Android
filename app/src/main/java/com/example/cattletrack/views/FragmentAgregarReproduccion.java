package com.example.cattletrack.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cattletrack.R;
import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.models.Ganado;
import com.example.cattletrack.models.HistorialReproduccion;
import com.example.cattletrack.models.ResultGanado;
import com.example.cattletrack.service.ServicesRetrofit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAgregarReproduccion extends Fragment {

    private Spinner spinnerIdToro, spinnerIdVaca;
    private EditText editCriasMachos, editCriasHembras;
    private EditText editFechaGestion, editFechaNacimiento;
    private Button btnAgregarReproduccion, btnCerrar;

    private LocalNetworkAPI api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_reproduccion, container, false);

        spinnerIdToro = view.findViewById(R.id.spinnerIdToro);
        spinnerIdVaca = view.findViewById(R.id.spinnerIdVaca);
        editFechaGestion = view.findViewById(R.id.editFechaGestion);
        editFechaNacimiento = view.findViewById(R.id.editFechaNacimiento);
        editCriasMachos = view.findViewById(R.id.editCriasMachos);
        editCriasHembras = view.findViewById(R.id.editCriasHembras);
        btnAgregarReproduccion = view.findViewById(R.id.btnAgregarReproduccion);
        btnCerrar = view.findViewById(R.id.btnCancelarH);

        api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);

        // Cargar datos reales
        cargarGanado();

        // Configurar fechas
        editFechaGestion.setFocusable(false);
        editFechaNacimiento.setFocusable(false);

        editFechaGestion.setOnClickListener(v -> showDatePicker(editFechaGestion));
        editFechaNacimiento.setOnClickListener(v -> showDatePicker(editFechaNacimiento));

        btnCerrar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnAgregarReproduccion.setOnClickListener(v -> agregarReproduccion());

        return view;
    }

    private void cargarGanado() {

        SharedPreferences sharedPref = requireActivity()
                .getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);

        int userId = sharedPref.getInt("userId", 0);
        int userType = sharedPref.getInt("userType", 0);

        Call<ResultGanado> call = api.getGanado(userId, userType);

        call.enqueue(new Callback<ResultGanado>() {
            @Override
            public void onResponse(Call<ResultGanado> call, Response<ResultGanado> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Error al cargar ganado", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Ganado> lista = response.body().getData();
                if (lista == null) lista = new ArrayList<>();

                // Filtrar por género
                List<String> toros = new ArrayList<>();
                List<String> vacas = new ArrayList<>();

                for (Ganado g : lista) {
                    if (g.getGenero().equalsIgnoreCase("M")) {
                        toros.add(String.valueOf(g.getIdGanado()));
                    } else if (g.getGenero().equalsIgnoreCase("H")) {
                        vacas.add(String.valueOf(g.getIdGanado()));
                    }
                }

                cargarSpinners(toros, vacas);
            }

            @Override
            public void onFailure(Call<ResultGanado> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarSpinners(List<String> toros, List<String> vacas) {

        if (toros.isEmpty()) toros.add("Sin toros");
        if (vacas.isEmpty()) vacas.add("Sin vacas");

        ArrayAdapter<String> adapterToros =
                new ArrayAdapter<>(requireContext(), R.layout.spinner_item_negro, toros);

        adapterToros.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerIdToro.setAdapter(adapterToros);

        ArrayAdapter<String> adapterVacas =
                new ArrayAdapter<>(requireContext(), R.layout.spinner_item_negro, vacas);

        adapterVacas.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerIdVaca.setAdapter(adapterVacas);
    }

    private void agregarReproduccion() {

        if (TextUtils.isEmpty(editFechaGestion.getText()) ||
                TextUtils.isEmpty(editFechaNacimiento.getText()) ||
                TextUtils.isEmpty(editCriasMachos.getText()) ||
                TextUtils.isEmpty(editCriasHembras.getText())) {

            Toast.makeText(getContext(), "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // IDs seleccionados
        int idVaca = Integer.parseInt(spinnerIdVaca.getSelectedItem().toString());
        int idToro = Integer.parseInt(spinnerIdToro.getSelectedItem().toString());

        HistorialReproduccion reproduccion = new HistorialReproduccion(
                idVaca,
                idToro,
                editFechaGestion.getText().toString(),
                editFechaNacimiento.getText().toString(),
                Integer.parseInt(editCriasHembras.getText().toString()),
                Integer.parseInt(editCriasMachos.getText().toString())
        );

        Call<Void> call = api.setReproduccion(reproduccion);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Reproducción agregada correctamente", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al agregar reproducción", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            String formattedDate = String.format("%04d-%02d-%02d", y, m + 1, d);
            editText.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}

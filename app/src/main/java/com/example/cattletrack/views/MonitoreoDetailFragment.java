package com.example.cattletrack.views;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cattletrack.R;
import com.example.cattletrack.models.HistorialEnfermedadItem;
import com.example.cattletrack.models.MonitoreoItem;
import com.example.cattletrack.models.MonitoreoUpdateRequest;
import com.example.cattletrack.viewmodel.MonitoreoViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MonitoreoDetailFragment extends Fragment {

    private static final String TAG = "MonitoreoDetailFragment";

    // ViewModel y datos
    private MonitoreoViewModel monitoreoViewModel;
    private MonitoreoItem monitoreoItemActual;

    // Vistas de UI
    private TextView textViewDetailGanado, textViewDetailFecha;
    private TextInputEditText editTextTemperatura, editTextFreqCardiaca, editTextFreqRespiratoria, editTextDeshidratacion, editTextDesglose;
    private TextView textViewHistorialEnfermedades;
    private Button buttonGuardar;

    // Interfaz de comunicación
    private MonitoreoFragment.OnFragmentInteractionListener mListener;

    public MonitoreoDetailFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MonitoreoFragment.OnFragmentInteractionListener) {
            mListener = (MonitoreoFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitoreo_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicializar ViewModel
        monitoreoViewModel = new ViewModelProvider(this).get(MonitoreoViewModel.class);

        // 2. Vincular Vistas
        vincularVistas(view);

        // 3. Obtener el item del Bundle
        Bundle args = getArguments();
        if (args != null) {
            monitoreoItemActual = (MonitoreoItem) args.getSerializable("monitoreoItem");
            if (monitoreoItemActual != null) {
                Log.d(TAG, "Item recibido: " + monitoreoItemActual.getId());
                poblarDatosUI(monitoreoItemActual);
            } else {
                Log.e(TAG, "El item de monitoreo es nulo.");
                Toast.makeText(getContext(), "Error: No se pudo cargar el item.", Toast.LENGTH_SHORT).show();
            }
        }

        // 4. Configurar Listeners
        buttonGuardar.setOnClickListener(v -> guardarCambios());

        // 5. Configurar Observadores
        setupObservers();
    }

    private void vincularVistas(View view) {
        textViewDetailGanado = view.findViewById(R.id.textViewDetailGanado);
        textViewDetailFecha = view.findViewById(R.id.textViewDetailFecha);
        editTextTemperatura = view.findViewById(R.id.editTextTemperatura);
        editTextFreqCardiaca = view.findViewById(R.id.editTextFreqCardiaca);
        editTextFreqRespiratoria = view.findViewById(R.id.editTextFreqRespiratoria);
        editTextDeshidratacion = view.findViewById(R.id.editTextDeshidratacion);
        editTextDesglose = view.findViewById(R.id.editTextDesglose);
        buttonGuardar = view.findViewById(R.id.buttonGuardar);
        textViewHistorialEnfermedades = view.findViewById(R.id.textViewHistorialEnfermedades);
    }

    private void poblarDatosUI(MonitoreoItem item) {
        textViewDetailGanado.setText(String.valueOf(item.getIdGanado()));
        textViewDetailFecha.setText(item.getFechaHora());

        // Campos editables
        editTextTemperatura.setText(String.valueOf(item.getTemperatura()));
        editTextFreqCardiaca.setText(String.valueOf(item.getFrecuenciaCardiaca()));
        editTextFreqRespiratoria.setText(String.valueOf(item.getFrecuenciaRespiratoria()));
        editTextDeshidratacion.setText(item.getNivelDeshidratacion());

        // Manejar la lista de Desglose
        if (item.getDesglose() != null && !item.getDesglose().isEmpty()) {
            // Unimos la lista con saltos de línea para mostrarla en el EditText
            String desgloseTexto = String.join("\n", item.getDesglose());
            editTextDesglose.setText(desgloseTexto);
        } else {
            editTextDesglose.setText("");
        }

        if (item.getHistorialEnfermedades() != null && !item.getHistorialEnfermedades().isEmpty()) {
            // Construimos un string para mostrar la lista
            StringBuilder historialBuilder = new StringBuilder();
            for (HistorialEnfermedadItem historia : item.getHistorialEnfermedades()) {
                historialBuilder.append("ID Enfermedad: ")
                        .append(historia.getIdEnfermedad())
                        .append(", Fecha: ")
                        .append(historia.getFechaHora())
                        .append("\n"); // Salto de línea para el siguiente item
            }
            textViewHistorialEnfermedades.setText(historialBuilder.toString());
        } else {
            textViewHistorialEnfermedades.setText("No hay historial de enfermedades.");
        }
    }

    private void setupObservers() {
        monitoreoViewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), itemActualizado -> {
            Toast.makeText(getContext(), "¡Actualizado con éxito!", Toast.LENGTH_SHORT).show();
            // Opcional: navegar hacia atrás al fragmento anterior
            if (mListener != null && getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        monitoreoViewModel.getUpdateError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), "Error al actualizar: " + error, Toast.LENGTH_LONG).show();
        });

        monitoreoViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Aquí puedes mostrar/ocultar un ProgressBar
            // progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void guardarCambios() {
        if (monitoreoItemActual == null) {
            Toast.makeText(getContext(), "No hay item para actualizar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Crear el objeto "request" vacío
        MonitoreoUpdateRequest request = new MonitoreoUpdateRequest();

        // 2. Rellenar el objeto "request" con los datos de los EditTexts
        try {
            request.setTemperatura(Double.parseDouble(editTextTemperatura.getText().toString()));
            request.setFrecuenciaCardiaca(Integer.parseInt(editTextFreqCardiaca.getText().toString()));
            request.setFrecuenciaRespiratoria(Integer.parseInt(editTextFreqRespiratoria.getText().toString()));
            request.setNivelDeshidratacion(editTextDeshidratacion.getText().toString().trim());

            // Convertir el texto multilínea de Desglose de nuevo a una lista
            String desgloseTexto = editTextDesglose.getText().toString();
            List<String> desgloseLista = Arrays.stream(desgloseTexto.split("\n"))
                    .filter(linea -> !linea.trim().isEmpty())
                    .collect(Collectors.toList());
            request.setDesglose(desgloseLista);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor, ingresa números válidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Llama al ViewModel con el ID y el objeto "request" que acabas de crear
        Log.d(TAG, "Enviando actualización para item: " + monitoreoItemActual.getId());
        monitoreoViewModel.updateMonitoreo(monitoreoItemActual.getId(), request);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
package com.example.cattletrack.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;
import com.example.cattletrack.adapter.HistorialReproduccionAdapter;
import com.example.cattletrack.models.ResultHistorialReproduccion;
import com.example.cattletrack.viewmodel.HistorialReproduccionViewModel;

import java.util.ArrayList;

public class FragmentHistorialReproduccion extends Fragment {

    public interface OnFragmentInteractionH {
        void onLoadFragment(Fragment fragment);
    }

    private MonitoreoFragment.OnFragmentInteractionListener mListener;

    private EditText editIdGanado;
    private FrameLayout contenedorHistorial;
    private Button btnAgregarHistorial;

    private RecyclerView recyclerHistorial;
    private HistorialReproduccionViewModel historialViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MonitoreoFragment.OnFragmentInteractionListener) {
            mListener = (MonitoreoFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial_reproduccion, container, false);

        // Views

        contenedorHistorial = view.findViewById(R.id.contenedorHistorial);
        btnAgregarHistorial = view.findViewById(R.id.btnAgregarHistorial);

        // Botón para agregar registro
        btnAgregarHistorial.setOnClickListener(v -> {
            if (mListener != null) {
                try {
                    Fragment nuevoFragment = new FragmentAgregarReproduccion();
                    mListener.onLoadFragment(nuevoFragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar RecyclerView dentro del FrameLayout
        recyclerHistorial = new RecyclerView(requireContext());
        recyclerHistorial.setLayoutParams(
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        contenedorHistorial.addView(recyclerHistorial);
        recyclerHistorial.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializar ViewModel
        historialViewModel = new ViewModelProvider(this).get(HistorialReproduccionViewModel.class);

        // Observar datos del historial
        historialViewModel.getHistorial().observe(getViewLifecycleOwner(), result -> {
            if (result != null && result.isSuccess() && result.getData() != null) {
                HistorialReproduccionAdapter adapter =
                        new HistorialReproduccionAdapter(requireContext(), result.getData());
                recyclerHistorial.setAdapter(adapter);
            } else {
                recyclerHistorial.setAdapter(new HistorialReproduccionAdapter(requireContext(), new ArrayList<>()));
                Toast.makeText(requireContext(), "No hay historial disponible", Toast.LENGTH_SHORT).show();
            }
        });

        // Llamar la API
        historialViewModel.loadHistorial();

        return view;
    }
}

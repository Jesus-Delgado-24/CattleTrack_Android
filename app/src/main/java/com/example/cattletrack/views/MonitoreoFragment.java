package com.example.cattletrack.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log; // <-- IMPORTANTE: AÑADE ESTO
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cattletrack.R;
import com.example.cattletrack.adapter.MonitoreoAdapter;
import com.example.cattletrack.viewmodel.MonitoreoViewModel;

import java.io.Serializable;
import java.util.ArrayList;

public class MonitoreoFragment extends Fragment {

    // --- Variables para MVVM ---
    private MonitoreoViewModel monitoreoViewModel;
    private RecyclerView recyclerViewMonitoreo;
    private MonitoreoAdapter monitoreoAdapter;

    private static final String TAG = "MonitoreoFragment"; // <-- Tag para los logs

    public interface OnFragmentInteractionListener {
        void onLoadFragment(Fragment fragment);
    }

    private OnFragmentInteractionListener mListener;

    public MonitoreoFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitoreo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Configuración del Botón (tu código original) ---
        Button addButton = view.findViewById(R.id.buttonAgregar);
        addButton.setOnClickListener(v -> {
            if (mListener != null) {
                try {
                    Fragment nuevoFragment = new eamr_monitoreo(); // Asegúrate que esta clase exista
                    mListener.onLoadFragment(nuevoFragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // --- Configuración de MVVM y RecyclerView ---
        Log.d(TAG, "onViewCreated: Inicializando ViewModel y RecyclerView...");

        // 1. Inicializar ViewModel
        monitoreoViewModel = new ViewModelProvider(this).get(MonitoreoViewModel.class);

        // 2. Configurar RecyclerView
        recyclerViewMonitoreo = view.findViewById(R.id.recyclerViewMonitoreo);
        recyclerViewMonitoreo.setLayoutManager(new LinearLayoutManager(getContext()));
        monitoreoAdapter = new MonitoreoAdapter(new ArrayList<>());
        recyclerViewMonitoreo.setAdapter(monitoreoAdapter);

        monitoreoAdapter.setOnItemClickListener(item -> {
            // Cuando se hace clic, abrimos el fragmento de detalle
            Log.d("MonitoreoFragment", "Clic en item: " + item.getId());

            // 1. Crea el nuevo fragmento
            MonitoreoDetailFragment detailFragment = new MonitoreoDetailFragment();

            // 2. Prepara un "paquete" de datos
            Bundle args = new Bundle();
            args.putSerializable("monitoreoItem", item); // "monitoreoItem" es la clave
            detailFragment.setArguments(args);

            // 3. Usa la interfaz para cargar el nuevo fragmento
            if (mListener != null) {
                mListener.onLoadFragment(detailFragment);
            }
        });

        // 3. Observar cambios en el ViewModel
        setupObservers();

        // 4. Cargar los datos
        loadMonitoreosFromApi();
    }

    private void setupObservers() {
        Log.d(TAG, "setupObservers: Configurando observadores...");
        // Observador para la lista de monitoreos
        monitoreoViewModel.getMonitoreoList().observe(getViewLifecycleOwner(), monitoreos -> {
            if (monitoreos != null) {
                Log.d(TAG, "Observador getMonitoreoList: Datos recibidos. Cantidad: " + monitoreos.size());
                monitoreoAdapter.setMonitoreos(monitoreos);
            }
        });

        // Observador para errores
        monitoreoViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e(TAG, "Observador getError: " + error); // <-- Error va a Logcat
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Observador para el estado de carga (puedes añadir un ProgressBar si quieres)
        monitoreoViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d(TAG, "Observador getIsLoading: " + isLoading);
        });
    }

    private void loadMonitoreosFromApi() {
        Log.d(TAG, "loadMonitoreosFromApi: Intentando cargar datos...");

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        int userType = sharedPref.getInt("userType", -1);

        Log.d(TAG, "Datos de SharedPreferences: userId=" + userId + ", userType=" + userType);

        if (userId == -1 || userType == -1) {
            Log.e(TAG, "loadMonitoreosFromApi: ¡Error de sesión! userId o userType es -1.");
            Toast.makeText(getContext(), "Error de sesión. Intenta iniciar sesión de nuevo.", Toast.LENGTH_LONG).show();
            return;
        }

        // Llamamos al ViewModel para que cargue los datos
        Log.d(TAG, "loadMonitoreosFromApi: Llamando a monitoreoViewModel.fetchMonitoreos...");
        monitoreoViewModel.fetchMonitoreos(userId, userType);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
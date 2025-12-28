package com.example.cattletrack.views;

import android.os.Bundle;
import android.util.Log; // <--- 1. IMPORTA EL LOG
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import com.example.cattletrack.R;
import com.example.cattletrack.adapter.AlmacenListAdapter;
import com.example.cattletrack.models.ResultAlmacen;
import com.example.cattletrack.viewmodel.ListAlmacenViewModel;

import java.util.ArrayList;

public class AlmacenFragment extends Fragment {

    // --- 2. AÑADE UN TAG PARA FILTRAR ---
    private static final String TAG = "AlmacenFragment";

    // Declarar las variables
    private ListAlmacenViewModel listAlmacenViewModel;
    private RecyclerView recyclerView;
    private AlmacenListAdapter almacenListAdapter;
    private boolean sortAscending = true;

    public AlmacenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: Fragmento creado."); // <-- LOG

        // 1. Inicializa el ViewModel aquí.
        listAlmacenViewModel = new ViewModelProvider(this).get(ListAlmacenViewModel.class);

        if (listAlmacenViewModel != null) {
            Log.d(TAG, "onCreate: ViewModel inicializado exitosamente."); // <-- LOG
        } else {
            Log.e(TAG, "onCreate: ERROR! ViewModel es nulo."); // <-- LOG
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Inflando layout..."); // <-- LOG
        // 2. Infla el layout y devuelve la vista.
        return inflater.inflate(R.layout.fragment_almacen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Vista creada. Configurando UI..."); // <-- LOG

        // 3. Configura todas tus vistas aquí.

        // Encontrar el RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAlmacen);
        if (recyclerView == null) {
            Log.e(TAG, "onViewCreated: ERROR! recyclerViewAlmacen no se encontró en el layout."); // <-- LOG
            return; // Salir si no podemos encontrar el RV
        } else {
            Log.d(TAG, "onViewCreated: RecyclerView encontrado."); // <-- LOG
        }

        // Configurar el LayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        // Configurar el Adaptador
        almacenListAdapter = new AlmacenListAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(almacenListAdapter);
        Log.d(TAG, "onViewCreated: LayoutManager y Adapter configurados."); // <-- LOG

        Button btnOrdenar = view.findViewById(R.id.btnorganizar);

        btnOrdenar.setText(sortAscending ? "Clave ↑" : "Clave ↓");

        btnOrdenar.setOnClickListener(v -> {
            sortAscending = !sortAscending;

            btnOrdenar.setText(sortAscending ? "Clave ↑" : "Clave ↓");

            if (almacenListAdapter != null) {
                almacenListAdapter.ordenarPorId(sortAscending);
            }
        });


        // 4. Crear el Observador
        Log.d(TAG, "onViewCreated: Creando el Observer para LiveData..."); // <-- LOG
        final Observer<ResultAlmacen> observer = new Observer<ResultAlmacen>() {
            @Override
            public void onChanged(ResultAlmacen result) {
                // ESTE ES EL LOG MÁS IMPORTANTE
                Log.d(TAG, "Observer onChanged: ¡Datos recibidos del ViewModel!"); // <-- LOG

                // Actualiza el adaptador cuando lleguen los datos
                if (result == null) {
                    Log.w(TAG, "Observer onChanged: El 'result' (ResultAlmacen) es NULO."); // <-- LOG
                    return;
                }

                if (result.getResults() != null) {
                    int numItems = result.getResults().size();
                    Log.i(TAG, "Observer onChanged: Actualizando adaptor con " + numItems + " elementos."); // <-- LOG
                    almacenListAdapter.setResultAlmacen(result.getResults());
                } else {
                    Log.w(TAG, "Observer onChanged: 'result.getResults()' (la lista de items) es NULA."); // <-- LOG
                }
            }
        };

        // 5. Observar el LiveData
        listAlmacenViewModel.getAlmacenes().observe(getViewLifecycleOwner(), observer);
        Log.d(TAG, "onViewCreated: El observador está activo y escuchando."); // <-- LOG

        // 6. Llamar al servicio para cargar los datos
        Log.i(TAG, "onViewCreated: Llamando a CallServiceGetAlmacen(1)..."); // <-- LOG
        listAlmacenViewModel.CallServiceGetAlmacen(1);
    }
}

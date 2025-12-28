package com.example.cattletrack.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattletrack.R;

import java.util.Arrays;
import java.util.List;

public class Abastecer extends Fragment {
    private TextView etFecha;
    private Spinner spinnerSector, spinnerAlimento;
    private EditText etCantidad;
    private Button btnAbastecer;
    RecyclerView recyclerView;

    public Abastecer() {

    }

    public static Abastecer newInstance() {
        return new Abastecer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_abastecer, container, false);

        spinnerSector = view.findViewById(R.id.spinnerSector);
        spinnerAlimento = view.findViewById(R.id.spinnerAlimento);
        etFecha = view.findViewById(R.id.tvFecha);
        etCantidad = view.findViewById(R.id.etCantidad);
        btnAbastecer = view.findViewById(R.id.btnAbastecer);
        recyclerView = view.findViewById(R.id.recyclerView);


        configurarSpinners();


        inicializarEventos();

        return view;
    }

    private void configurarSpinners() {

        List<String> sectores = Arrays.asList(
                "Sector Norte",
                "Sector Sur",
                "Sector Este",
                "Sector Oeste"
        );


        List<String> alimentos = Arrays.asList(
                "Pasto seco",
                "Concentrado",
                "Maíz molido",
                "Heno",
                "Sales minerales"
        );


        ArrayAdapter<String> adapterSectores = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item_negro,
                sectores
        );
        adapterSectores.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSector.setAdapter(adapterSectores);

        ArrayAdapter<String> adapterAlimentos = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item_negro,
                alimentos
        );
        adapterAlimentos.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerAlimento.setAdapter(adapterAlimentos);
    }

    private void inicializarEventos() {

        btnAbastecer.setOnClickListener(v -> {
            String fecha = etFecha.getText().toString().trim();
            String cantidad = etCantidad.getText().toString().trim();
            String sector = spinnerSector.getSelectedItem() != null ? spinnerSector.getSelectedItem().toString() : "";
            String alimento = spinnerAlimento.getSelectedItem() != null ? spinnerAlimento.getSelectedItem().toString() : "";

            if (fecha.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }


            Toast.makeText(getContext(),
                    "✅ Abastecido " + cantidad + "kg de " + alimento +
                            " en " + sector + " (" + fecha + ")",
                    Toast.LENGTH_LONG).show();
        });
    }
}

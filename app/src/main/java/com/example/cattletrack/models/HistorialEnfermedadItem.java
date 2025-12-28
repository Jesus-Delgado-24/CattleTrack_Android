package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class HistorialEnfermedadItem implements Serializable {
    @SerializedName("id_Enfermedad")
    private int idEnfermedad;

    @SerializedName("Fecha_Hora")
    private String fechaHora;

    // Getters
    public int getIdEnfermedad() {
        return idEnfermedad;
    }

    public String getFechaHora() {
        return fechaHora;
    }
}
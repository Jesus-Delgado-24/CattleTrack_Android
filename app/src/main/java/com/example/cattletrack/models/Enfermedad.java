package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class Enfermedad {
    @SerializedName("id_Enfermedad")
    private int id;

    @SerializedName("Nombre")
    private String nombre;

    public int getId() { return id; }
    public String getNombre() { return nombre; }
}

package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class AlimentoResult {
    @SerializedName("id_Alimento")
    public int id;

    @SerializedName("Nombre")
    public String Nombre;

    @Override
    public String toString() {
        return Nombre;
    }
}

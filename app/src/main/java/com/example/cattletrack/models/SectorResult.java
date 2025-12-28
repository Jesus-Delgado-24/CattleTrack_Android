package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class SectorResult {
    @SerializedName("id_Sector")
    public int id;

    @SerializedName("Nombre")
    public String Nombre;

    @Override
    public String toString() {
        return Nombre;
    }
}

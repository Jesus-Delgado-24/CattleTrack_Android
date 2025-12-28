package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbastecerResult {
    @SerializedName("id_Sector")
    @Expose
    public int id_Sector;

    @SerializedName("id_Alimento")
    @Expose
    public int id_Alimento;

    @SerializedName("Cantidad")
    @Expose
    public double Cantidad;

    @SerializedName("Fecha_hora")
    @Expose
    public String Fecha_hora;

    @SerializedName("_id")
    @Expose
    public String idMongo;

    @SerializedName("Nombre_sector")
    @Expose
    public String Nombre_Sector;

}

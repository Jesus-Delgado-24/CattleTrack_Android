package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class Ganado {

    @SerializedName("id_Ganado")
    private int idGanado;

    @SerializedName("id_Sector")
    private int idSector;

    @SerializedName("Genero")
    private String genero;

    @SerializedName("Peso")
    private String peso;

    @SerializedName("Fecha_Nacimiento")
    private String fechaNacimiento;

    @SerializedName("ObjectId")
    private String objectId;

    public int getIdGanado() {
        return idGanado;
    }

    public int getIdSector() {
        return idSector;
    }

    public String getGenero() {
        return genero;
    }

    public String getPeso() {
        return peso;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getObjectId() {
        return objectId;
    }
}

package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sector {
    @SerializedName("id_Sector")
    @Expose
    private int id_Sector;
    @SerializedName("Nombre")
    @Expose
    private String Nombre;
    @SerializedName("Ubicacion")
    @Expose
    private String Ubicacion;
    @SerializedName("Capacidad")
    @Expose
    private String Capacidad;

    public int getId_Sector() {
        return id_Sector;
    }

    public void setId_Sector(int id_Sector) {
        this.id_Sector = id_Sector;
    }

    public String getCapacidad() {
        return Capacidad;
    }

    public void setCapacidad(String capacidad) {
        Capacidad = capacidad;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}

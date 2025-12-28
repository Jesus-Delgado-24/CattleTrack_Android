package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leche {
    @SerializedName("id_L")
    @Expose
    private int id_L;
    @SerializedName(value = "id_sector", alternate = {"id_Sector"})
    @Expose
    private int id_Sector;
    @SerializedName(value = "cantidad", alternate = {"Cantidad"})
    @Expose
    private String Cantidad;
    @SerializedName("Fecha")
    @Expose
    private String Fecha;

    public int getId_L() {
        return id_L;
    }

    public void setId_L(int id_L) {
        this.id_L = id_L;
    }

    public int getId_Sector() {
        return id_Sector;
    }

    public void setId_Sector(int id_Sector) {
        this.id_Sector = id_Sector;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}

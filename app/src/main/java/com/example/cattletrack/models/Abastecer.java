package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class Abastecer {
    @SerializedName("id_Sector")
    private int id_Sector;

    @SerializedName("id_Alimento")
    private int id_Alimento;

    @SerializedName("Cantidad")
    private double Cantidad;

    // Constructor
    public Abastecer(int id_Sector, int id_Alimento, double Cantidad, String fecha) {
        this.id_Sector = id_Sector;
        this.id_Alimento = id_Alimento;
        this.Cantidad = Cantidad;
    }

    // Getters y Setters
    public int getId_Sector() { return id_Sector; }
    public void setId_Sector(int id_Sector) { this.id_Sector = id_Sector; }

    public int getId_Alimento() { return id_Alimento; }
    public void setId_Alimento(int id_Alimento) { this.id_Alimento = id_Alimento; }

    public double getCantidad() { return Cantidad; }
    public void setCantidad(double cantidad) { Cantidad = cantidad; }
}

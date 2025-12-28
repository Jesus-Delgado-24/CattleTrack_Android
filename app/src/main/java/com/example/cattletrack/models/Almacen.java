package com.example.cattletrack.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Almacen {
    @SerializedName("id_Alimento")
    @Expose
    private int id_Alimento;
    @SerializedName("Nombre")
    @Expose
    private String Nombre;
    @SerializedName("Tipo")
    @Expose
    private String Tipo;
    @SerializedName("Cantidad")
    @Expose
    private double Cantidad;

    public int getId_Alimento() {
        return id_Alimento;
    }

    public void setId_Alimento(int id_Alimento) {
        this.id_Alimento = id_Alimento;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }
}

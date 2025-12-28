package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonitoringResult {
    @SerializedName("_id")
    private String id;

    @SerializedName("id_Veterinario")
    private int idVeterinario;

    @SerializedName("id_Ganado")
    private int idGanado;

    @SerializedName("Fecha_Hora")
    private String fechaHora;

    @SerializedName("Temperatura")
    private double temperatura;

    @SerializedName("Frecuencia_Cardiaca")
    private int frecuenciaCardiaca;

    @SerializedName("Frecuencia_Respiratoria")
    private int frecuenciaRespiratoria;

    @SerializedName("Nivel_Deshidratacion")
    private String nivelDeshidratacion;

    @SerializedName("Desglose")
    private List<String> desglose;

    @SerializedName("Historial_Enfermedades")
    private List<MonitoringRequest.EnfermedadItem> historialEnfermedades;

    // Getters
    public String getId() { return id; }
    public int getIdVeterinario() { return idVeterinario; }
    public int getIdGanado() { return idGanado; }
    public String getFechaHora() { return fechaHora; }
    public double getTemperatura() { return temperatura; }
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public int getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public String getNivelDeshidratacion() { return nivelDeshidratacion; }
    public List<String> getDesglose() { return desglose; }
    public List<MonitoringRequest.EnfermedadItem> getHistorialEnfermedades() { return historialEnfermedades; }
}


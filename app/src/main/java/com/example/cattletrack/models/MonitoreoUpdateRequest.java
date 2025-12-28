package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Modelo para el cuerpo de la petición PUT /api/a_monitoring/:id
public class MonitoreoUpdateRequest {
    @SerializedName("id_Veterinario")
    private Integer idVeterinario;
    @SerializedName("id_Ganado")
    private Integer idGanado;

    @SerializedName("Temperatura")
    private Double temperatura;

    @SerializedName("Frecuencia_Cardiaca")
    private Integer frecuenciaCardiaca;

    @SerializedName("Frecuencia_Respiratoria")
    private Integer frecuenciaRespiratoria;

    @SerializedName("Nivel_Deshidratacion")
    private String nivelDeshidratacion;

    @SerializedName("Desglose")
    private List<String> desglose;

    @SerializedName("Historial_Enfermedades")
    private List<HistorialEnfermedadItem> historialEnfermedades;

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public void setNivelDeshidratacion(String nivelDeshidratacion) {
        this.nivelDeshidratacion = nivelDeshidratacion;
    }

    public void setDesglose(List<String> desglose) {
        this.desglose = desglose;
    }
}
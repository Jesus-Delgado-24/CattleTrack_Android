package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MonitoringRequest {

    @SerializedName("id_Veterinario")
    private int idVeterinario;

    @SerializedName("id_Ganado")
    private int idGanado;

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
    private List<EnfermedadItem> historialEnfermedades;

    public MonitoringRequest(int idVeterinario, int idGanado, double temperatura,
                             int frecuenciaCardiaca, int frecuenciaRespiratoria,
                             String nivelDeshidratacion, List<String> desglose,
                             List<EnfermedadItem> historialEnfermedades) {
        this.idVeterinario = idVeterinario;
        this.idGanado = idGanado;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
        this.nivelDeshidratacion = nivelDeshidratacion;
        this.desglose = desglose;
        this.historialEnfermedades = historialEnfermedades;
    }

    public static class EnfermedadItem {
        @SerializedName("id_Enfermedad")
        private int idEnfermedad;

        @SerializedName("Fecha_Hora")
        private String fechaHora;

        public EnfermedadItem(int idEnfermedad, String fechaHora) {
            this.idEnfermedad = idEnfermedad;
            this.fechaHora = fechaHora;
        }

        public int getIdEnfermedad() { return idEnfermedad; }
        public String getFechaHora() { return fechaHora; }
    }
}

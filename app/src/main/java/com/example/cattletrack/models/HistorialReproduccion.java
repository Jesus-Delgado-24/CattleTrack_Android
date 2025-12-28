package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class HistorialReproduccion {

    @SerializedName("_id")
    private String id;

    @SerializedName("id_Vaca")
    private int idVaca;

    @SerializedName("id_Toro")
    private int idToro;

    @SerializedName("Fecha_Gestion")
    private String fechaGestion;

    @SerializedName("Fecha_Nacimiento")
    private String fechaNacimiento;

    @SerializedName("Crias_Hembras")
    private int criasHembras;

    @SerializedName("Crias_Macho")
    private int criasMacho;

    // Constructor vacío (necesario para Retrofit/Gson)
    public HistorialReproduccion() {}

    // Constructor completo
    public HistorialReproduccion(int idVaca, int idToro, String fechaGestion, String fechaNacimiento, int criasHembras, int criasMacho) {
        this.idVaca = idVaca;
        this.idToro = idToro;
        this.fechaGestion = fechaGestion;
        this.fechaNacimiento = fechaNacimiento;
        this.criasHembras = criasHembras;
        this.criasMacho = criasMacho;
    }

    // Getters
    public String getId() { return id; }
    public int getIdVaca() { return idVaca; }
    public int getIdToro() { return idToro; }
    public String getFechaGestion() { return fechaGestion; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public int getCriasHembras() { return criasHembras; }
    public int getCriasMacho() { return criasMacho; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setIdVaca(int idVaca) { this.idVaca = idVaca; }
    public void setIdToro(int idToro) { this.idToro = idToro; }
    public void setFechaGestion(String fechaGestion) { this.fechaGestion = fechaGestion; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setCriasHembras(int criasHembras) { this.criasHembras = criasHembras; }
    public void setCriasMacho(int criasMacho) { this.criasMacho = criasMacho; }
}

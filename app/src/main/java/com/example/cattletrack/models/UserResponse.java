package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserResponse {
    @SerializedName("id_Usuario")
    private int idUsuario;

    @SerializedName("Nombre")
    private String nombre;

    @SerializedName("Apellido_P")
    private String apellidoP;

    @SerializedName("Apellido_M")
    private String apellidoM;

    @SerializedName("Email")
    private String email;

    @SerializedName("Tipo_U")
    private int tipoU;

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellidoP() { return apellidoP; }
    public String getApellidoM() { return apellidoM; }
    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " +
                (apellidoP != null ? apellidoP : "") + " " +
                (apellidoM != null ? apellidoM : "");
    }
    public String getEmail() { return email; }
    public int getTipoU() { return tipoU; }
}
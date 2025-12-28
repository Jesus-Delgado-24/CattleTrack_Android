package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String message;
    @SerializedName("idUsuario")
    private int idUsuario;
    @SerializedName("tipoUsuario")
    private int tipoUsuario;

    public String getMessage() {
        return message;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }
}
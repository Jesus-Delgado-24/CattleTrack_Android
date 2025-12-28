package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLeche {
    @SerializedName("leche")
    @Expose
    private Leche leche;
    @SerializedName("status")
    @Expose
    private int status_server;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    public Leche getLeche() {
        return leche;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setLeche(Leche leche) {
        this.leche = leche;
    }

    public int getStatus_server() {
        return status_server;
    }

    public void setStatus_server(int status_server) {
        this.status_server = status_server;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

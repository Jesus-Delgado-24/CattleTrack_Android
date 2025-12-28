package com.example.cattletrack.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ResponseAlmacen {
    @SerializedName("almacen")
    @Expose
    private Almacen almacen;
    @SerializedName("status")
    @Expose
    private int status_server;
    @SerializedName("msj")
    @Expose
    private String mensaje;
}

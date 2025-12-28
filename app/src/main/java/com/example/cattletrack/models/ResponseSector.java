package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSector {
    @SerializedName("sector")
    @Expose
    private Sector sector;
    @SerializedName("status")
    @Expose
    private int status_server;
    @SerializedName("message")
    @Expose
    private String message;


    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
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

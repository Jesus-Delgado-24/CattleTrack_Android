package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultSector {
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("data")
    @Expose
    private List<Sector> result;

    public List<Sector> getResult() {
        return result;
    }

    public void setResult(List<Sector> result) {
        this.result = result;
    }
}

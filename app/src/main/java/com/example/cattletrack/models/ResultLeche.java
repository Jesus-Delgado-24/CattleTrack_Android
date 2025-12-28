package com.example.cattletrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultLeche {
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("data")
    @Expose
    private List<Leche> result;

    public List<Leche> getData() {
        return result;
    }

    public void setResult(List<Leche> result) {
        this.result = result;
    }
}

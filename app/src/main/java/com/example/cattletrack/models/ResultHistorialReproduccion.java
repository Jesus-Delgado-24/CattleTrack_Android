package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResultHistorialReproduccion {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<HistorialReproduccion> data;

    public boolean isSuccess() { return success; }
    public List<HistorialReproduccion> getData() { return data; }
}

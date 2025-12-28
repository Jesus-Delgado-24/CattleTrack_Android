package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Esta clase envuelve la respuesta completa de la API
public class MonitoreoResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    private List<MonitoreoItem> data;
    // Getters
    public boolean isSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

    public List<MonitoreoItem> getData() {
        return data;
    }
}
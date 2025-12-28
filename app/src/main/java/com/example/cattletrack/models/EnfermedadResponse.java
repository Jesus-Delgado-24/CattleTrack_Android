package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnfermedadResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public List<Enfermedad> data;
}

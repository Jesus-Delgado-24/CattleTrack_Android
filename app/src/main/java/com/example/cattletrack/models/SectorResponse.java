package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectorResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public List<SectorResult> data;
}

package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

public class MonitoringResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private MonitoringResult data;

    @SerializedName("error")
    private String error;

    public boolean isSuccess() { return success; }
    public MonitoringResult getData() { return data; }
    public String getError() { return error; }
}

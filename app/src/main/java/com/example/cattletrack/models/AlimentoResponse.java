package com.example.cattletrack.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlimentoResponse {
        @SerializedName("success")
        public boolean success;

        @SerializedName("data")
        public List<AlimentoResult> data;

}

package com.example.cattletrack.models;

import java.util.List;

public class ResultGanado {
    private boolean success;
    private List<Ganado> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Ganado> getData() {
        return data;
    }
}

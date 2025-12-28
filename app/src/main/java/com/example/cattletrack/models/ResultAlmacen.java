package com.example.cattletrack.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class ResultAlmacen {
    @SerializedName("data")
    @Expose
    private List<Almacen>results;

    public List<Almacen> getResults() {
        return results;
    }

    public void setResults(List<Almacen> results) {
        this.results = results;
    }
}
package com.example.cattletrack.models;

public class MapAbastecer {
    private int id;
    private String nombre;

    public MapAbastecer(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
    }
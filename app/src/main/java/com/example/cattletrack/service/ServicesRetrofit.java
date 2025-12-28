package com.example.cattletrack.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServicesRetrofit {
    public static String BASE_URL = "http://192.168.137.1:5000/api/";
    private static Retrofit INSTANCE=null;

    public static Retrofit getClient(){
        if (INSTANCE==null){
            INSTANCE= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return INSTANCE;
    }
}
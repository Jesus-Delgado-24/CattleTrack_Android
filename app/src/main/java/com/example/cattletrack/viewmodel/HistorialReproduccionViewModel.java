package com.example.cattletrack.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.service.ServicesRetrofit;
import com.example.cattletrack.models.ResultHistorialReproduccion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialReproduccionViewModel extends ViewModel {

    private MutableLiveData<ResultHistorialReproduccion> historial = new MutableLiveData<>();
    private LocalNetworkAPI api;

    public LiveData<ResultHistorialReproduccion> getHistorial() {
        return historial;
    }

    public void loadHistorial() {
        api = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);

        api.getHistorialReproduccion().enqueue(new Callback<ResultHistorialReproduccion>() {
            @Override
            public void onResponse(Call<ResultHistorialReproduccion> call, Response<ResultHistorialReproduccion> response) {
                if (response.isSuccessful()) {
                    historial.postValue(response.body());
                } else {
                    historial.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResultHistorialReproduccion> call, Throwable t) {
                historial.postValue(null);
            }
        });
    }
}

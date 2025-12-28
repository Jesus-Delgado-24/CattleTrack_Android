package com.example.cattletrack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.service.ServicesRetrofit;
import com.example.cattletrack.models.MonitoreoItem;
import com.example.cattletrack.models.MonitoreoResponse;
import com.example.cattletrack.models.MonitoreoUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoreoViewModel extends ViewModel {

    private final LocalNetworkAPI apiService;
    private final MutableLiveData<List<MonitoreoItem>> monitoreoList = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<MonitoreoItem> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> updateError = new MutableLiveData<>();

    public MonitoreoViewModel() {
        apiService = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
    }

    public LiveData<MonitoreoItem> getUpdateSuccess() {
        return updateSuccess;
    }
    public LiveData<String> getUpdateError() {
        return updateError;
    }

    // Getters para que el Fragment los observe
    public LiveData<List<MonitoreoItem>> getMonitoreoList() {
        return monitoreoList;
    }
    public LiveData<String> getError() {
        return error;
    }
    public LiveData<Boolean> getIsLoading() {

        return isLoading;
    }

    // Método para cargar los monitoreos
    public void fetchMonitoreos(int idUsuario, int tipoUsuario) {
        isLoading.setValue(true);
        apiService.getMonitoreos(idUsuario, tipoUsuario).enqueue(new Callback<MonitoreoResponse>() {
            @Override
            public void onResponse(Call<MonitoreoResponse> call, Response<MonitoreoResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    monitoreoList.setValue(response.body().getData());
                } else {
                    error.setValue("No se pudieron cargar los datos.");
                }
            }

            @Override
            public void onFailure(Call<MonitoreoResponse> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Error de red: " + t.getMessage());
            }
        });
    }
    public void updateMonitoreo(String monitoreoId, MonitoreoUpdateRequest request) {
        isLoading.setValue(true);
        apiService.updateMonitoreo(monitoreoId, request).enqueue(new Callback<MonitoreoItem>() {
            @Override
            public void onResponse(Call<MonitoreoItem> call, Response<MonitoreoItem> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    updateSuccess.setValue(response.body());
                } else {
                    updateError.setValue("Error al actualizar. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MonitoreoItem> call, Throwable t) {
                isLoading.setValue(false);
                updateError.setValue("Error de red: " + t.getMessage());
            }
        });
    }

}
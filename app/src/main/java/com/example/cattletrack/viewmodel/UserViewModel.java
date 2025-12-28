package com.example.cattletrack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.service.ServicesRetrofit;
import com.example.cattletrack.models.UserResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private final LocalNetworkAPI apiService;
    private final MutableLiveData<UserResponse> user = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public UserViewModel() {
        apiService = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
    }

    public LiveData<UserResponse> getUser() { return user; }
    public LiveData<String> getError() { return error; }

    public void fetchUserDetails(int userId) {
        // Llamamos a la API que devuelve una LISTA
        apiService.getUserById(userId).enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                // Verificamos si la lista no es nula y tiene al menos un usuario
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Tomamos el primer usuario de la lista (índice 0)
                    user.setValue(response.body().get(0));
                } else {
                    error.setValue("Usuario no encontrado.");
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                error.setValue("Error de red: " + t.getMessage());
            }
        });
    }
}
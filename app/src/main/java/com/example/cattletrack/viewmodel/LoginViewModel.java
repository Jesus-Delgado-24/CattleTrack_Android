package com.example.cattletrack.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.service.ServicesRetrofit;
import com.example.cattletrack.models.LoginRequest;
import com.example.cattletrack.models.LoginResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();
    private final LocalNetworkAPI apiService;

    public LoginViewModel() {
        // Obtenemos la instancia de ApiService desde nuestro RetrofitClient
        apiService = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
    }

    // Métodos para que la Vista (MainActivity) observe los cambios
    public LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    // La lógica para realizar el login
    public void performLogin(String email, String password) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            loginError.postValue("Usuario y contraseña no pueden estar vacíos.");
            return;
        }

        LoginRequest request = new LoginRequest(email, password);
        Call<LoginResponse> call = apiService.loginUser(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    loginResult.postValue(response.body());
                } else {
                    loginError.postValue("Credenciales incorrectas. Inténtalo de nuevo.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginError.postValue("Error de red: " + t.getMessage());
            }
        });
    }
}
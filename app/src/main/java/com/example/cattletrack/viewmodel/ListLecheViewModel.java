package com.example.cattletrack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.models.Leche;
import com.example.cattletrack.models.ResponseLeche;
import com.example.cattletrack.models.ResultLeche;
import com.example.cattletrack.service.ServicesRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLecheViewModel extends ViewModel {
    private MutableLiveData<ResultLeche> list;
    private LocalNetworkAPI localNetworkAPI;
    private MutableLiveData<ResponseLeche> actionResponse = new MutableLiveData<>();

    public ListLecheViewModel() {
        this.list = new MutableLiveData<ResultLeche>();
        localNetworkAPI = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
    }

    public LiveData<ResultLeche> getLeche(){
        return list;
    }
    public LiveData<ResponseLeche> getActionResponse() {
        return actionResponse;
    }

    public void CallServiceGetLeche(int id, int tipo){
        Call<ResultLeche> call = localNetworkAPI.getLeche(id, tipo);

        call.enqueue(new Callback<ResultLeche>() {
            @Override
            public void onResponse(Call<ResultLeche> call, Response<ResultLeche> response) {
                if (response.isSuccessful()) {
                    list.postValue(response.body());
                } else {
                    list.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResultLeche> call, Throwable t) {
                list.postValue(null);
            }
        });
    }

    public void CallServicePostLeche(int id_S, String cantidad){
        Leche sLeche = new Leche();
        sLeche.setId_Sector(id_S);
        sLeche.setCantidad(cantidad);
        Call<ResponseLeche> call = localNetworkAPI.setLeche(sLeche);

        call.enqueue(new Callback<ResponseLeche>() {
            @Override
            public void onResponse(Call<ResponseLeche> call, Response<ResponseLeche> response) {
                if (response.isSuccessful()) {
                    actionResponse.postValue(response.body());
                } else {
                    actionResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseLeche> call, Throwable t) {
                actionResponse.postValue(null);
            }
        });
    }

    public void CallServiceDeleteLeche(String id) {
        Call<ResponseLeche> call = localNetworkAPI.delLeche(id);

        call.enqueue(new Callback<ResponseLeche>() {
            @Override
            public void onResponse(Call<ResponseLeche> call, Response<ResponseLeche> response) {
                if (response.isSuccessful()) {
                    actionResponse.postValue(response.body());
                } else {
                    actionResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseLeche> call, Throwable t) {
                actionResponse.postValue(null);
            }
        });
    }
}
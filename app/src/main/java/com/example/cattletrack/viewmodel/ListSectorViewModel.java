package com.example.cattletrack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.models.ResponseLeche;
import com.example.cattletrack.models.ResultLeche;
import com.example.cattletrack.models.ResultSector;
import com.example.cattletrack.models.Sector;
import com.example.cattletrack.service.ServicesRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListSectorViewModel extends ViewModel {
    private MutableLiveData<ResultSector> list;
    private LocalNetworkAPI localNetworkAPI;
    private MutableLiveData<ResponseLeche> actionResponse = new MutableLiveData<>();

    public ListSectorViewModel() {
        this.list = new MutableLiveData<ResultSector>();
        localNetworkAPI = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
    }

    public LiveData<ResultSector> getSector(){
        return list;
    }
    public LiveData<ResponseLeche> getActionResponse() {
        return actionResponse;
    }

    public void CallServiceGetSector(int id, int tipo){
        Call<ResultSector> call = localNetworkAPI.getSector(id, tipo);

        call.enqueue(new Callback<ResultSector>() {
            @Override
            public void onResponse(Call<ResultSector> call, Response<ResultSector> response) {
                if(response.isSuccessful()){
                    list.postValue(response.body());
                } else {
                    list.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResultSector> call, Throwable t) {
                list.postValue(null);
            }
        });
    }
}

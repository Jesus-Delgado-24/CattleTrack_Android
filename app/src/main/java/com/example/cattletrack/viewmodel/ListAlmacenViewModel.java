package com.example.cattletrack.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cattletrack.api.LocalNetworkAPI;
import com.example.cattletrack.models.ResultAlmacen;
import com.example.cattletrack.service.ServicesRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListAlmacenViewModel extends ViewModel {
    private MutableLiveData<ResultAlmacen> list;
    private LocalNetworkAPI localNetworkAPI;

    public ListAlmacenViewModel() {
        this.list = new MutableLiveData<ResultAlmacen>();
    }
    public LiveData<ResultAlmacen> getAlmacenes() {return list;}
    public void setPersona(ResultAlmacen list) {this.list.setValue(list);}

    public void CallServiceGetAlmacen(int page) {
        localNetworkAPI = ServicesRetrofit.getClient().create(LocalNetworkAPI.class);
        Call<ResultAlmacen> call = localNetworkAPI.getListAlmacen();
        call.enqueue(new Callback<ResultAlmacen>() {
            @Override
            public void onResponse(Call<ResultAlmacen> call, Response<ResultAlmacen> response) {
                if (response.isSuccessful()) {
                    list.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<ResultAlmacen> call, Throwable t) {list.postValue(null);}

        });
    }
}

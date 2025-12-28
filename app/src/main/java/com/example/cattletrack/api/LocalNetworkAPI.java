package com.example.cattletrack.api;
import com.example.cattletrack.models.Abastecer;
import com.example.cattletrack.models.AbastecerResponse;
import com.example.cattletrack.models.AlimentoResponse;
import com.example.cattletrack.models.EnfermedadResponse;
import com.example.cattletrack.models.GanadoResponse;
import com.example.cattletrack.models.HistorialReproduccion;
import com.example.cattletrack.models.Leche;
import com.example.cattletrack.models.LoginRequest;
import com.example.cattletrack.models.LoginResponse;
import com.example.cattletrack.models.MonitoreoItem;
import com.example.cattletrack.models.MonitoreoResponse;
import com.example.cattletrack.models.MonitoreoUpdateRequest;
import com.example.cattletrack.models.MonitoringRequest;
import com.example.cattletrack.models.MonitoringResponse;
import com.example.cattletrack.models.ResponseLeche;
import com.example.cattletrack.models.ResultAlmacen;
import com.example.cattletrack.models.ResultGanado;
import com.example.cattletrack.models.ResultHistorialReproduccion;
import com.example.cattletrack.models.ResultLeche;
import com.example.cattletrack.models.ResultSector;
import com.example.cattletrack.models.SectorResponse;
import com.example.cattletrack.models.UserResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LocalNetworkAPI {
    @GET("g_proleche")
    Call<ResultLeche> getLeche(@Query("id") int id, @Query("tipo") int tipo);
    @GET("g_almacenes")
    Call<ResultAlmacen> getListAlmacen();
    @Headers({"Content-Type: application/json"})
    @DELETE("e_p_leche/{id}")
    Call<ResponseLeche> delLeche(@Path("id") String id);
    @Headers({"Content-Type: application/json"})
    @POST("r_produccion")
    Call<ResponseLeche> setLeche(@Body Leche leche);
    @GET("g_sectores")
    Call<ResultSector> getSector(@Query("id") int id, @Query("tipo") int tipo);
    @GET("b_sector/{id}")
    Call<ResultSector> getBSector(@Path("id") String id);
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    @GET("g_monitoreo")
    Call<MonitoreoResponse> getMonitoreos(
            @Query("id") int idUsuario,
            @Query("tipo") int tipoUsuario
    );
    @PUT("a_monitoring/{id}")
    Call<MonitoreoItem> updateMonitoreo(
            @Path("id") String monitoreoId, // El ID del monitoreo a actualizar
            @Body MonitoreoUpdateRequest requestBody // Los datos a cambiar
    );
    @GET("b_usuario/{id}")
    Call<List<UserResponse>> getUserById(@Path("id") int userId);

    @GET("g_historialpro")
    Call<ResultHistorialReproduccion> getHistorialReproduccion();

    @Headers({"Content-Type: application/json"})
    @POST("r_reproduccion")
    Call<Void> setReproduccion(@Body HistorialReproduccion reproduccion);

    @GET("g_ganado")
    Call<ResultGanado> getGanado(@Query("id") int id, @Query("tipo") int tipo);


    @POST("r_monitoreo")
    Call<MonitoringResponse> postMonitoring(@Body MonitoringRequest monitoringRequest);


    @GET("g_sectores")
    Call<SectorResponse> getSectores(
            @Query("id") int id,
            @Query("tipo") int tipo
    );

    @GET("g_almacenes")
    Call<AlimentoResponse> getAlimentos();

    @GET("/api/g_abastecer")
    Call<AbastecerResponse> getAbastecer(
            @Query("id") int id,
            @Query("tipo") int tipo
    );

    @POST("r_abastecimiento")
    Call<AbastecerResponse> postAbastecer(@Body Abastecer abastecer);

    @PUT("a_food/supply/{id}")
    Call<AbastecerResponse> putAbastecer(
            @Path("id") String id,
            @Body Abastecer abastecer
    );
    @GET("g_enfermedades")
    Call<EnfermedadResponse> getEnfermedades();

    @GET("g_ganado")
    Call<GanadoResponse> getGanados(@Query("id") int id, @Query("tipo") int tipo);



}

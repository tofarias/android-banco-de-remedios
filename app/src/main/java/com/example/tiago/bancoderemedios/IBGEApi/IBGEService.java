package com.example.tiago.bancoderemedios.IBGEApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IBGEService {

    @GET("api/v1/localidades/estados")
    Call<List<Estado>> listStates();

    @GET("api/v1/localidades/estados/{id}/municipios")
    Call<List<Municipio>> listCountiesByState(@Path("id") String Id);
}

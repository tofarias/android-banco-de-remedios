package com.example.tiago.roupas.IBGEApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IBGEService {

    @GET("api/v1/localidades/estados")
    Call<List<Estado>> listEstados();

    @GET("api/v1/localidades/estados/{id}/municipios")
    Call<List<Municipio>> listMunicipiosPorEstado(@Path("id") String Id);
}

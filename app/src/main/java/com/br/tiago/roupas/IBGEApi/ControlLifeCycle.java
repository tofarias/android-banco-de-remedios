package com.br.tiago.roupas.IBGEApi;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControlLifeCycle extends Application {

    public static IBGEService service;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://servicodados.ibge.gov.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(IBGEService.class);
    }
}

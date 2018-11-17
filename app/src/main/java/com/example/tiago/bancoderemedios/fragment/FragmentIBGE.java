package com.example.tiago.bancoderemedios.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tiago.bancoderemedios.IBGEApi.ControlLifeCycle;
import com.example.tiago.bancoderemedios.IBGEApi.Estado;
import com.example.tiago.bancoderemedios.IBGEApi.Municipio;
import com.example.tiago.bancoderemedios.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentIBGE extends Fragment {

    private Spinner spinnerEstado, spinnerMunicipio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_ibge, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.spinnerEstado = (Spinner) getActivity().findViewById(R.id.spinnerEstado);
        this.spinnerEstado.setOnItemSelectedListener( spinnerEstadoOnItemSelectedListener );

        this.spinnerMunicipio = (Spinner) getActivity().findViewById(R.id.spinnerMunicipio);
        //this.spinnerMunicipio.setOnItemSelectedListener();

        try {

            Call<List<Estado>> call = ControlLifeCycle.service.listEstados();
            call.enqueue( getListEstados() );

            Toast.makeText(getContext(), call.request().url().toString(), Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Log.e("Call<Estado>",e.getMessage().toString());
        }
    }

    private Callback<List<Estado>> getListEstados(){

        return new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {

                if( response != null) {

                    if (response.isSuccessful()) {

                        List<Estado> listEstados = new ArrayList<>();

                        for (Estado res : response.body()) {

                            listEstados.add( new Estado(res.id, res.nome, res.sigla) );
                        }

                        ArrayAdapter userAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, listEstados.toArray());
                        spinnerEstado.setAdapter(userAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Log.e("onFailure", t.getMessage().toString());
            }
        };
    };

    private Callback<List<Municipio>> getListMunicipios(){

        return new Callback<List<Municipio>>() {
            @Override
            public void onResponse(Call<List<Municipio>> call, Response<List<Municipio>> response) {

                if( response != null) {

                    if (response.isSuccessful()) {

                        List<Municipio> listMunicipios = new ArrayList<>();

                        for (Municipio res : response.body()) {

                            listMunicipios.add( new Municipio(res.id, res.nome) );
                        }

                        ArrayAdapter municipioAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, listMunicipios.toArray());
                        spinnerMunicipio.setAdapter(municipioAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Municipio>> call, Throwable t) {
                Log.e("onFailure", t.getMessage().toString());
            }
        };
    };

    private AdapterView.OnItemSelectedListener spinnerEstadoOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Estado estado = (Estado) parent.getSelectedItem();
            Call<List<Municipio>> call = null;

            try {

                call = ControlLifeCycle.service.listMunicipiosPorEstado(estado.id);
                call.enqueue( getListMunicipios() );

                Toast.makeText(getContext(), call.request().url().toString(), Toast.LENGTH_LONG).show();

            }catch (Exception e){
                Log.e("Call<Municipio>",e.getMessage().toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}

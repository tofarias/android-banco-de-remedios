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
import com.example.tiago.bancoderemedios.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentIBGE extends Fragment {

    private Spinner spinnerEstado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_ibge, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Call<List<Estado>> call = null;
        this.spinnerEstado = (Spinner) getActivity().findViewById(R.id.spinnerEstado);
        this.spinnerEstado.setOnItemSelectedListener( spinnerEstadoOnItemSelectedListener );

        try {
            call = ControlLifeCycle.service.detailTitle();
        }catch (Exception e){
            Log.e("Call<Estado>",e.getMessage().toString());
        }


        call.enqueue(new Callback<List<Estado>>() {

            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {

                if( response != null) {

                    if (response.isSuccessful()) {
                        List<String> listEstados = new ArrayList<>();

                        for (Estado res : response.body()) {

                            listEstados.add(res.sigla + " - " + res.nome);
                        }
                        Collections.sort(listEstados);
                        listEstados.add(0, "Selecione");

                        ArrayAdapter userAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, listEstados.toArray());
                        spinnerEstado.setAdapter(userAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Log.e("onFailure", t.getMessage().toString());
            }
        });
    }

    private AdapterView.OnItemSelectedListener spinnerEstadoOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if( parent.getSelectedItemPosition() > 0 ){
                Estado estado = (Estado) parent.getSelectedItem();
                Toast.makeText(getContext(), "id: "+estado.id+",  nome : "+estado.nome+", sigla: "+estado.sigla, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /*spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Country country = (Country) parent.getSelectedItem();
            Toast.makeText(context, "Country ID: "+country.getId()+",  Country Name : "+country.getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });*/
}

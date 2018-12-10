package com.br.tiago.roupas.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.br.tiago.roupas.IBGEApi.ControlLifeCycle;
import com.br.tiago.roupas.IBGEApi.Estado;
import com.br.tiago.roupas.IBGEApi.Municipio;
import com.br.tiago.roupas.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUsuario extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private GoogleSignInAccount account;

    private TextView textViewNome, textViewEmail;
    private Spinner spinnerEstado, spinnerMunicipio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuario, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nav_header_user);

        this.setFirebaseInstance();
        this.setDatabaseReference();

        this.setAccount();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        this.textViewNome = (TextView) getActivity().findViewById(R.id.textViewNomeLabel);
        this.textViewNome.setText( currentUser.getDisplayName() );

        this.textViewEmail = (TextView) getActivity().findViewById(R.id.textViewEmail);
        this.textViewEmail.setText( currentUser.getEmail() );

        this.spinnerEstado = (Spinner) getActivity().findViewById(R.id.spinnerEstado);
        this.spinnerEstado.setOnItemSelectedListener( spinnerEstadoOnItemSelectedListener );

        this.spinnerMunicipio = (Spinner) getActivity().findViewById(R.id.spinnerMunicipio);

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
                        listEstados.add(0,new Estado("0","Selecione",""));

                        for (Estado res : response.body()) {

                            listEstados.add( new Estado(res.id, res.nome, res.sigla) );
                        }
                        //listEstados.
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

    private void setAccount() {
        this.account = GoogleSignIn.getLastSignedInAccount(getContext());
    }

    private void setDatabaseReference() {
        //this.mDatabaseReference = this.mFirebaseDatabase.getReference("necessidades");
        this.mDatabaseReference = this.mFirebaseDatabase.getReference("usuarios");
    }

    private void setFirebaseInstance() {

        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
    }
}

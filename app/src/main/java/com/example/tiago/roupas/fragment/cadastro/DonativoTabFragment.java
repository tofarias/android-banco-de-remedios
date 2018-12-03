package com.example.tiago.roupas.fragment.cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tiago.roupas.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonativoTabFragment extends Fragment {

    Spinner spinnerTipoRoupas;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private GoogleSignInAccount account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_donativo_tab, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(R.string.nav_header_user);

        this.setAccount();

        this.setFirebaseInstance();
        this.setDatabaseReference();

        //this.setEditTexts();
        //this.setTextViews();
        this.setSpinnerTipoRoupas();

        //this.setBtnSalvar();

        this.spinnerTipoRoupas.requestFocus();

        //this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );
    }

    private void setAccount() {
        this.account = GoogleSignIn.getLastSignedInAccount(getContext());
    }

    private void setDatabaseReference() {
        this.mDatabaseReference = this.mFirebaseDatabase.getReference("usuarios");
    }

    private void setFirebaseInstance() {

        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    /*private void setBtnSalvar() {
        this.btnSalvar = (Button) getActivity().findViewById(R.id.buttonSalvar);
    }*/

    private void setSpinnerTipoRoupas(){

        this.spinnerTipoRoupas = (Spinner) getActivity().findViewById(R.id.spinnerTipoRoupasDonativo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.tipo_roupas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTipoRoupas.setAdapter(adapter);
    }
}

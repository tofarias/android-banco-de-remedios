package com.example.tiago.bancoderemedios.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiago.bancoderemedios.R;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class FragmentMain extends Fragment {

    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.mFirebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Tela Principal");

        TextView textViewBemVindo = (TextView) getActivity().findViewById(R.id.textViewBemVindo);

        if( this.mFirebaseAuth.getCurrentUser() != null ){
            textViewBemVindo.setText("Bem Vindo "+this.mFirebaseAuth.getCurrentUser().getDisplayName()+"!");
        }else{
            textViewBemVindo.setText("NÃO AUTENTICADO !!!");
        }
    }
}

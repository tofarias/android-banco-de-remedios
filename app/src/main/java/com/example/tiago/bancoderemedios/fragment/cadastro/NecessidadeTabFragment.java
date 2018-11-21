package com.example.tiago.bancoderemedios.fragment.cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tiago.bancoderemedios.R;

public class NecessidadeTabFragment extends Fragment {

    EditText editTextTitulo;
    Button btnSalvar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_necessidade_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(R.string.nav_header_user);

        this.editTextTitulo = (EditText) getActivity().findViewById(R.id.editTextTitulo);
        this.editTextTitulo.requestFocus();

        this.btnSalvar = (Button) getActivity().findViewById(R.id.buttonSalvar);
        this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );
    }

    private View.OnClickListener btnSalvarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(getContext(), "Não Implementado", Toast.LENGTH_LONG).show();
        }
    };
}

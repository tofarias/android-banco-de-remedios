package com.example.tiago.roupas.fragment.cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tiago.roupas.R;

public class DonativoTabFragment extends Fragment {

    CheckBox checkBoxCamisa;
    EditText editTextQCamiseta;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_donativo_tab, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(R.string.nav_header_user);

        this.checkBoxCamisa = (CheckBox) getActivity().findViewById(R.id.checkBoxCamisa);
        this.checkBoxCamisa.setOnCheckedChangeListener(checkBoxCamisaOnCheckedChangeListener);

        this.editTextQCamiseta = (EditText) getActivity().findViewById(R.id.editTextQCamiseta);
    }

    private CompoundButton.OnCheckedChangeListener checkBoxCamisaOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if( isChecked ){
                editTextQCamiseta.setEnabled( true );
                editTextQCamiseta.requestFocus();
            }else{
                editTextQCamiseta.setEnabled( false );
                editTextQCamiseta.setText("");
            }
        }
    };
}

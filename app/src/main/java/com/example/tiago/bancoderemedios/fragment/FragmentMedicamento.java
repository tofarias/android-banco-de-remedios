package com.example.tiago.bancoderemedios.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tiago.bancoderemedios.R;
import com.example.tiago.bancoderemedios.model.Medicamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentMedicamento extends Fragment {

    Calendar calendarDtValidade;
    EditText editTextDtValidade, editTextNome, editTextPrincipioAtivo, editTextLaboratorio;
    Spinner spinnerTipoMedicamento;
    Button btnSalvar;

    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseMedicamentos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicamento, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mFirebaseAuth     = FirebaseAuth.getInstance();
        this.mFirebaseDatabase = FirebaseDatabase.getInstance();

        this.mDatabaseMedicamentos = this.mFirebaseDatabase.getReference("medicamentos");

        //

        getActivity().setTitle(R.string.nav_header_medicine);

        getActivity().findViewById(R.id.editTextNome).requestFocus();

        this.editTextNome           = (EditText) getActivity().findViewById(R.id.editTextNome);
        this.editTextPrincipioAtivo = (EditText) getActivity().findViewById(R.id.editTextPrincipioAtivo);
        this.editTextLaboratorio    = (EditText) getActivity().findViewById(R.id.editTextLaboratorio);

        this.editTextDtValidade = (EditText) getActivity().findViewById( R.id.editTextDtValidade );
        this.editTextDtValidade.setOnFocusChangeListener( this.editTextDtValidadeOnFocusChangeListener );

        this.spinnerTipoMedicamento = (Spinner) getActivity().findViewById( R.id.spinnerTipoMedicamento);
        this.populateSpinnerTipoMedicamento();

        this.calendarDtValidade = Calendar.getInstance();

        this.btnSalvar = (Button) getActivity().findViewById(R.id.btnSalvar) ;
        this.btnSalvar.setOnClickListener( btnSalvarOnClickListener );
    }

    private View.OnClickListener btnSalvarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Medicamento medicamento = new Medicamento(
                    editTextNome.getText().toString().trim(),
                    editTextLaboratorio.getText().toString().trim(),
                    editTextPrincipioAtivo.getText().toString().trim()
            );

            try {
                String uui = mDatabaseMedicamentos.push().getKey();
                mDatabaseMedicamentos.child( uui ).setValue(medicamento);

                Toast.makeText(getContext(), "Salvando Dados", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void populateSpinnerTipoMedicamento(){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTipoMedicamento.setAdapter(adapter);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear,int dayOfMonth) {

            calendarDtValidade.set(Calendar.YEAR, year);
            calendarDtValidade.set(Calendar.MONTH, monthOfYear);
            calendarDtValidade.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateEditTextDtValidade();
        }

    };

    private View.OnFocusChangeListener editTextDtValidadeOnFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus) {
                new DatePickerDialog(getContext(),date, calendarDtValidade
                        .get(Calendar.YEAR), calendarDtValidade.get(Calendar.MONTH),
                        calendarDtValidade.get(Calendar.DAY_OF_MONTH)).show();
            }
        }
    };

    private void updateEditTextDtValidade() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        this.editTextDtValidade.setText(sdf.format(calendarDtValidade.getTime()));
    }
}

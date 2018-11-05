package com.example.tiago.bancoderemedios;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentMedicine extends Fragment {

    Calendar calendarDtValidade;
    EditText editTextDtValidade;
    Spinner spinnerTipoMedicamento;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medicine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.nav_header_medicine);

        this.editTextDtValidade = (EditText) getActivity().findViewById( R.id.editTextDtValidade );
        this.editTextDtValidade.setOnFocusChangeListener( this.editTextDtValidadeOnFocusChangeListener );

        this.spinnerTipoMedicamento = (Spinner) getActivity().findViewById( R.id.spinnerTipoMedicamento);
        this.populateSpinnerTipoMedicamento();

        this.calendarDtValidade = Calendar.getInstance();
    }

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

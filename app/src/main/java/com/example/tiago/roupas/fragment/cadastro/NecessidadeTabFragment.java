package com.example.tiago.roupas.fragment.cadastro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiago.roupas.R;
import com.example.tiago.roupas.model.Necessidade;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NecessidadeTabFragment extends Fragment {

    EditText editTextTipo, editTextDescricao, editTextJustificativa;
    TextView textViewTipo, textViewDescricao, textViewJustificativa;
    Button btnSalvar;
    Spinner spinnerTipoRoupas;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private GoogleSignInAccount account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_necessidade_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle(R.string.nav_header_user);

        this.setAccount();

        this.setFirebaseInstance();
        this.setDatabaseReference();

        this.setEditTexts();
        this.setTextViews();
        this.setSpinnerTipoRoupas();

        this.setBtnSalvar();

        this.spinnerTipoRoupas.requestFocus();

        this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );
    }

    public void exit(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Exit..!!!");
        // Icon Of Alert Dialog
        //alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure,You want to exit");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(),"You clicked over Yes",Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setSpinnerTipoRoupas(){

        this.spinnerTipoRoupas = (Spinner) getActivity().findViewById(R.id.spinnerTipoRoupasNecessidade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.tipo_roupas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTipoRoupas.setAdapter(adapter);
    }

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

    private void setBtnSalvar() {
        this.btnSalvar = (Button) getActivity().findViewById(R.id.buttonSalvar);
    }

    private View.OnClickListener btnSalvarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final Form form = new Form();

            if( form.isValid() ){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Salvar dados");
                alertDialogBuilder.setMessage("Deseja realmente salvar os dados?");
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        try {

                            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

                            String uid = currentUser.getUid();

                            Necessidade nec = new Necessidade(
                                    spinnerTipoRoupas.getSelectedItem().toString(),
                                    editTextDescricao.getText().toString().trim(),
                                    editTextJustificativa.getText().toString().trim()
                            );

                            mDatabaseReference.child( uid ).child("necessidades").push().setValue( nec );

                            Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_LONG).show();
                            form.clear();
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    };

    private  void setTextViews(){
        this.textViewTipo        = (TextView) getActivity().findViewById(R.id.textViewTipo);
        this.textViewDescricao     = (TextView) getActivity().findViewById(R.id.textViewDescricao);
        this.textViewJustificativa = (TextView) getActivity().findViewById(R.id.textViewJustificativa);
    }

    private void setEditTexts(){
        this.editTextDescricao     = (EditText) getActivity().findViewById(R.id.editTextDescricao);
        this.editTextJustificativa = (EditText) getActivity().findViewById(R.id.editTextJustificativa);
    }

    private class Form{

        public boolean isValid(){

            Boolean isValid = true;
            int textViewDescricaoMinSize     = 1;
            int textViewJustificativaMinSize = 1;

            if( spinnerTipoRoupas.getSelectedItemId() == 0 ){
                isValid = false;
                spinnerTipoRoupas.requestFocus();
                Toast.makeText(getContext(), "O campo Tipo precisa ser informado.", Toast.LENGTH_LONG).show();
            }else if( editTextDescricao.getText().toString().trim().length() < textViewDescricaoMinSize ){

                isValid = false;
                editTextDescricao.requestFocus();
                Toast.makeText(getContext(), "O campo "+textViewDescricao.getText()+" precisa ter pelo menos "+textViewDescricaoMinSize+" caractere(s).", Toast.LENGTH_LONG).show();

            }else if( editTextJustificativa.getText().toString().trim().length() < textViewJustificativaMinSize ){

                isValid = false;
                editTextJustificativa.requestFocus();
                Toast.makeText(getContext(), "O campo "+textViewJustificativa.getText()+" precisa ter pelo menos "+textViewJustificativaMinSize+" caractere(s).", Toast.LENGTH_LONG).show();
            }

            return isValid;
        }

        public void clear(){
            spinnerTipoRoupas.setSelection(0);
            editTextJustificativa.setText("");
            editTextDescricao.setText("");
        }
    }
}

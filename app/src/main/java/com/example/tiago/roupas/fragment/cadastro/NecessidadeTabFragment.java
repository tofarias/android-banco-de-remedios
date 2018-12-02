package com.example.tiago.roupas.fragment.cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        /*this.spinnerTipoRoupas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );
    }

    private void setSpinnerTipoRoupas(){

        this.spinnerTipoRoupas = (Spinner) getActivity().findViewById(R.id.spinnerTipoRoupas);
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

            Form form = new Form();

            if( form.isValid() ){

                try {

                    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

                    //String uui = mDatabaseReference.push().getKey();
                    //String uui = account.getId().toString();
                    String uid = currentUser.getUid();

                    //mDatabaseReference.child( uui ).child("user_id").setValue( account.getId().toString() );
                    //mDatabaseReference.child( uui ).child("tipo").setValue( spinnerTipoRoupas.getSelectedItem().toString() );
                    //mDatabaseReference.child( uui ).child("descricao").setValue( editTextDescricao.getText().toString().trim() );
                    //mDatabaseReference.child( uui ).child("justificativa").setValue( editTextJustificativa.getText().toString().trim() );

                    Necessidade nec = new Necessidade(spinnerTipoRoupas.getSelectedItem().toString(),
                                                      editTextDescricao.getText().toString().trim(),
                                                      editTextJustificativa.getText().toString().trim());

                    mDatabaseReference.child( uid ).child("usuario").child("uui").setValue( uid );
                    mDatabaseReference.child( uid ).child("usuario").child("nome").setValue( currentUser.getDisplayName() );
                    mDatabaseReference.child( uid ).child("usuario").child("email").setValue( currentUser.getEmail() );
                    mDatabaseReference.child( uid ).child("usuario").child("photo_url").setValue( currentUser.getPhotoUrl().toString() );

                    mDatabaseReference.child( uid ).child("necessidades").push().setValue( nec );

                    Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_LONG).show();
                    form.clear();
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    private  void setTextViews(){
        this.textViewTipo        = (TextView) getActivity().findViewById(R.id.textViewTipo);
        this.textViewDescricao     = (TextView) getActivity().findViewById(R.id.textViewDescricao);
        this.textViewJustificativa = (TextView) getActivity().findViewById(R.id.textViewCreatedAt);
    }

    private void setEditTexts(){
        this.editTextDescricao     = (EditText) getActivity().findViewById(R.id.editTextDescricao);
        //this.editTextTipo        = (EditText) getActivity().findViewById(R.id.editTextTipo);
        this.editTextJustificativa = (EditText) getActivity().findViewById(R.id.editTextJustificativa);
    }

    private class Form{

        private boolean isValid(){

            Boolean isValid = true;
            int textViewTipoMinSize        = 1;
            int textViewDescricaoMinSize     = 1;
            int textViewJustificativaMinSize = 1;

            /*if( editTextTipo.getText().toString().trim().length() < textViewTipoMinSize ){

                isValid = false;
                editTextTipo.requestFocus();
                Toast.makeText(getContext(), textViewTipo.getText()+" precisa ter pelo menos "+textViewTipoMinSize+" caractere(s).", Toast.LENGTH_LONG).show();

            }else if( editTextDescricao.getText().toString().trim().length() < textViewDescricaoMinSize ){

                isValid = false;
                editTextDescricao.requestFocus();
                Toast.makeText(getContext(), textViewDescricao.getText()+" precisa ter pelo menos "+textViewDescricaoMinSize+" caractere(s).", Toast.LENGTH_LONG).show();

            }else if( editTextJustificativa.getText().toString().trim().length() < textViewJustificativaMinSize ){

                isValid = false;
                editTextJustificativa.requestFocus();
                Toast.makeText(getContext(), textViewJustificativa.getText()+" precisa ter pelo menos "+textViewJustificativaMinSize+" caractere(s).", Toast.LENGTH_LONG).show();
            }*/

            return isValid;
        }

        private void clear(){
            //editTextTipo.setText("");
            editTextJustificativa.setText("");
            editTextDescricao.setText("");
        }
    }
}

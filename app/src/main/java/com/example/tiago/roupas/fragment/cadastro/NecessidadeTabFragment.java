package com.example.tiago.roupas.fragment.cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiago.roupas.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NecessidadeTabFragment extends Fragment {

    EditText editTextTitulo, editTextDescricao, editTextJustificativa;
    TextView textViewTitulo, textViewDescricao, textViewJustificativa;
    Button btnSalvar;

    private FirebaseAuth mFirebaseAuth;
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

        this.account = GoogleSignIn.getLastSignedInAccount(getContext());

        this.setFirebaseInstance();
        this.setDatabaseReference();

        this.setEditTexts();
        this.setTextViews();

        this.setBtnSalvar();

        this.editTextTitulo.requestFocus();

        this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );
    }

    private void setDatabaseReference() {
        this.mDatabaseReference = this.mFirebaseDatabase.getReference("necessidades");
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

            if( formIsValid() ){

                try {
                    String uui = mDatabaseReference.push().getKey();

                    mDatabaseReference.child( uui ).child("user_id").setValue( account.getId().toString() );
                    mDatabaseReference.child( uui ).child("titulo").setValue( editTextTitulo.getText().toString().trim() );
                    mDatabaseReference.child( uui ).child("descricao").setValue( editTextDescricao.getText().toString().trim() );
                    mDatabaseReference.child( uui ).child("justificativa").setValue( editTextJustificativa.getText().toString().trim() );

                    Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    private  void setTextViews(){
        this.textViewTitulo        = (TextView) getActivity().findViewById(R.id.textViewTitulo);
        this.textViewDescricao     = (TextView) getActivity().findViewById(R.id.textViewDescricao);
        this.textViewJustificativa = (TextView) getActivity().findViewById(R.id.textViewJustificativa);
    }

    private void setEditTexts(){
        this.editTextDescricao     = (EditText) getActivity().findViewById(R.id.editTextDescricao);
        this.editTextTitulo        = (EditText) getActivity().findViewById(R.id.editTextTitulo);
        this.editTextJustificativa = (EditText) getActivity().findViewById(R.id.editTextJustificativa);
    }

    private boolean formIsValid(){

        Boolean isValid = true;

        if( editTextTitulo.getText().toString().length() < 5 ){
            isValid = false;
            Toast.makeText(getContext(), textViewTitulo.getText()+" precisa ter pelo menos 5 carácteres.", Toast.LENGTH_LONG).show();
        }else if( editTextDescricao.getText().toString().length() < 5 ){
            isValid = false;
            Toast.makeText(getContext(), textViewDescricao.getText()+" precisa ter pelo menos 5 carácteres.", Toast.LENGTH_LONG).show();
        }else if( editTextJustificativa.getText().toString().length() < 10 ){
            isValid = false;
            Toast.makeText(getContext(), textViewJustificativa.getText()+" precisa ter pelo menos 10 carácteres.", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }
}

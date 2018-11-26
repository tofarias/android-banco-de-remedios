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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NecessidadeTabFragment extends Fragment {

    EditText editTextTitulo, editTextDescricao, editTextJustificativa;
    TextView textViewTitulo, textViewDescricao, textViewJustificativa;
    Button btnSalvar;

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

        this.setBtnSalvar();

        this.editTextTitulo.requestFocus();

        this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );
    }

    private void setAccount() {
        this.account = GoogleSignIn.getLastSignedInAccount(getContext());
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

            Form form = new Form();

            if( form.isValid() ){

                try {

                    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

                    //String uui = mDatabaseReference.push().getKey();
                    //String uui = account.getId().toString();
                    String uui = currentUser.getUid();

                    //mDatabaseReference.child( uui ).child("user_id").setValue( account.getId().toString() );
                    mDatabaseReference.child( uui ).child("titulo").setValue( editTextTitulo.getText().toString().trim() );
                    mDatabaseReference.child( uui ).child("descricao").setValue( editTextDescricao.getText().toString().trim() );
                    mDatabaseReference.child( uui ).child("justificativa").setValue( editTextJustificativa.getText().toString().trim() );

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String strDate = "Current Date : " + mdformat.format(calendar.getTime());

                    mDatabaseReference.child( uui ).child("created_at").setValue( mdformat.format(calendar.getTime()) );

                    Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_LONG).show();
                    form.clear();
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

    private class Form{

        private boolean isValid(){

            Boolean isValid = true;
            int textViewTituloMinSize        = 1;
            int textViewDescricaoMinSize     = 1;
            int textViewJustificativaMinSize = 1;

            if( editTextTitulo.getText().toString().trim().length() < textViewTituloMinSize ){

                isValid = false;
                editTextTitulo.requestFocus();
                Toast.makeText(getContext(), textViewTitulo.getText()+" precisa ter pelo menos "+textViewTituloMinSize+" caractere(s).", Toast.LENGTH_LONG).show();

            }else if( editTextDescricao.getText().toString().trim().length() < textViewDescricaoMinSize ){

                isValid = false;
                editTextDescricao.requestFocus();
                Toast.makeText(getContext(), textViewDescricao.getText()+" precisa ter pelo menos "+textViewDescricaoMinSize+" caractere(s).", Toast.LENGTH_LONG).show();

            }else if( editTextJustificativa.getText().toString().trim().length() < textViewJustificativaMinSize ){

                isValid = false;
                editTextJustificativa.requestFocus();
                Toast.makeText(getContext(), textViewJustificativa.getText()+" precisa ter pelo menos "+textViewJustificativaMinSize+" caractere(s).", Toast.LENGTH_LONG).show();
            }

            return isValid;
        }

        private void clear(){
            editTextTitulo.setText("");
            editTextJustificativa.setText("");
            editTextDescricao.setText("");
        }
    }
}

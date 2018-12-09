package com.br.tiago.roupas.fragment.cadastro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.br.tiago.roupas.R;
import com.br.tiago.roupas.model.Necessidade;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class NecessidadeTabFragment extends Fragment {

    EditText editTextTipo, editTextDescricao, editTextJustificativa;
    TextView textViewTipo, textViewDescricao, textViewJustificativa;
    Button   btnSalvar;
    Spinner  spinnerTipoRoupas;

    private FirebaseDatabase    mFirebaseDatabase;
    private DatabaseReference   mDatabaseReference;
    private FirebaseUser        currentUser;
    private GoogleSignInAccount account;

    private Button btnUpload, btnDeletar;
    private ImageView imageViewFoto;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private static final String storageURL = "gs://doar-roupas-75d0e.appspot.com";

    ProgressBar mProgressBarH;

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
        this.setFirebaseCurrentUser();

        this.setEditTexts();
        this.setTextViews();
        this.setSpinnerTipoRoupas();

        this.setBtnSalvar();

        this.spinnerTipoRoupas.requestFocus();

        this.btnSalvar.setOnClickListener( this.btnSalvarOnClickListener );

        this.mStorage = FirebaseStorage.getInstance();
        this.mStorageReference = this.mStorage.getReferenceFromUrl(storageURL);

        this.btnUpload = (Button) getActivity().findViewById(R.id.btnUpload);
        this.btnUpload.setOnClickListener(btnUploadOnClickListener);

        //this.btnDeletar = (Button) getActivity().findViewById(R.id.btnDeletar);
        //this.btnDeletar.setOnClickListener(btnDeleteOnClickListener);

        this.imageViewFoto = (ImageView) getActivity().findViewById(R.id.imageViewFoto);

        mProgressBarH = (ProgressBar) getActivity().findViewById(R.id.progressBarHorizontal);
        mProgressBarH.setVisibility(View.GONE);
    }

    private View.OnClickListener btnUploadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            selecionarFoto();
        }
    };

    private void selecionarFoto(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Selecionar Foto"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && requestCode == 1){

            Uri imagemSelecionada = data.getData();
            this.imageViewFoto.setImageURI(imagemSelecionada);
            //uploadFoto();
        }
    }

    private void uploadFoto(){

        Bitmap bitmap = ((BitmapDrawable) this.imageViewFoto.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

        byte[] imagem = outputStream.toByteArray();

        StorageReference imagemReference = this.mStorageReference.child("imagem").child("img-001.jpg");
        UploadTask uploadTask = imagemReference.putBytes(imagem);

        uploadTask.addOnProgressListener(getActivity(), new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());

                Toast.makeText(getContext(), "Aguarde, fazendo upload da imagem.", Toast.LENGTH_SHORT).show();

                int currentprogress = (int) progress;

                mProgressBarH.setVisibility(View.VISIBLE);
                mProgressBarH.setProgress(currentprogress);

                Log.i("mProgressBarH", Integer.toString(currentprogress) );
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgressBarH.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Upload realizado com sucesso", Toast.LENGTH_LONG).show();
            }
        });
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

    private void setFirebaseCurrentUser(){
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = mFirebaseAuth.getCurrentUser();
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

                            mDatabaseReference.child( uid ).child("usuario").child("id").setValue( uid );
                            mDatabaseReference.child( uid ).child("usuario").child("nome").setValue( currentUser.getDisplayName() );
                            mDatabaseReference.child( uid ).child("usuario").child("email").setValue( currentUser.getEmail() );
                            mDatabaseReference.child( uid ).child("usuario").child("photo_url").setValue( currentUser.getPhotoUrl().toString() );

                            uploadFoto();

                            //Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_LONG).show();
                            form.clear();

                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
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

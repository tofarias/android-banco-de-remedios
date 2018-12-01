package com.example.tiago.roupas.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tiago.roupas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class FragmentUpload extends Fragment {

    private Button btnUpload, btnDeletar;
    private ImageView imageViewFoto;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private static final String storageURL = "gs://doar-roupas-75d0e.appspot.com";

    ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.nav_header_upload);

        this.mStorage = FirebaseStorage.getInstance();
        this.mStorageReference = this.mStorage.getReferenceFromUrl(storageURL);

        this.btnUpload = (Button) getActivity().findViewById(R.id.btnUpload);
        this.btnUpload.setOnClickListener(btnUploadOnClickListener);

        this.btnDeletar = (Button) getActivity().findViewById(R.id.btnDeletar);
        this.btnDeletar.setOnClickListener(btnDeleteOnClickListener);

        this.imageViewFoto = (ImageView) getActivity().findViewById(R.id.imageViewFoto);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBarUpload);
        mProgressBar.setVisibility(View.GONE);
    }

    private View.OnClickListener btnDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            deletar();
        }
    };

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
            uploadFoto();
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

                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;

                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(currentprogress);
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Upload com sucesso", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deletar(){

        StorageReference imagemReference = this.mStorageReference.child("imagem").child("img-001.jpg");

        imagemReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Arquivo removido com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("DELETE", e.getMessage().toString());
            }
        });
    }
}

package com.example.tiago.roupas.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.example.tiago.roupas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

public class FragmentDownload extends Fragment {

    private Button btnDownload;
    private ImageView imageViewFoto;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private static final String storageURL = "gs://doar-roupas-75d0e.appspot.com";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.nav_header_download);

        this.mStorage = FirebaseStorage.getInstance();
        this.mStorageReference = this.mStorage.getReferenceFromUrl(storageURL);

        this.btnDownload = (Button) getActivity().findViewById(R.id.btnDownload);
        this.btnDownload.setOnClickListener(btnDownloadOnClickListener);

        this.imageViewFoto = (ImageView) getActivity().findViewById(R.id.imageViewFoto);
    }

    private View.OnClickListener btnDownloadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            download();
            obterMetadados();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && requestCode == 1){

            Uri imagemSelecionada = data.getData();
            this.imageViewFoto.setImageURI(imagemSelecionada);
        }
    }

    public void download(){

        final long MEGABYTES = 1024*1024*5;

        StorageReference imagemReference = this.mStorageReference.child("imagem").child("img-001.jpg");

        imagemReference.getBytes(MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imageViewFoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("download123", e.getMessage().toString());
            }
        });
    }

    public void obterMetadados(){
        StorageReference imagemReference = this.mStorageReference.child("imagem").child("img-001.jpg");

        imagemReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String imgNome = storageMetadata.getName();
                String imgTipo = storageMetadata.getContentType();
                String caminho = storageMetadata.getPath();
                String imgUri     = storageMetadata.getReference().getDownloadUrl().toString();
                long size = storageMetadata.getSizeBytes() / (1024 * 1024);

                TextView textViewMetadados = (TextView) getActivity().findViewById(R.id.textViewMetadata);
                textViewMetadados.setText("Nome: "+imgNome+"\nTipo: "+imgTipo+"\nCaminho: "+caminho+
                                          "\nURI: "+imgUri+"\nTamanho (MB): "+Long.toString(size));
            }
        });
    }
}
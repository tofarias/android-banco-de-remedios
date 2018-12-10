package com.br.tiago.roupas.activity.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.tiago.roupas.R;
import com.br.tiago.roupas.activity.MainActivity;
import com.br.tiago.roupas.model.Necessidade;
import com.br.tiago.roupas.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetalheNecessidadeActivity extends AppCompatActivity {

    private TextView textViewTipo, textViewJustificativa, textViewDescricao;
    private TextView textViewCreatedAt, textViewUsuario;

    private Button btnPossoAjudar;
    private ProgressBar mProgressBar;

    private ImageView imageViewFoto;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private static final String storageURL = "gs://doar-roupas-75d0e.appspot.com";

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_necessidade_detalhe);
        setTitle("Poderia Ajudar?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String necId = getIntent().getStringExtra("necId");

        this.textViewTipo = (TextView) (findViewById(R.id.textViewTipo));
        this.textViewJustificativa = (TextView) (findViewById(R.id.textViewJustificativa));
        this.textViewCreatedAt = (TextView) (findViewById(R.id.textViewCreatedAt));
        this.textViewDescricao = (TextView) (findViewById(R.id.textViewDescricao));
        this.textViewUsuario = (TextView) (findViewById(R.id.textViewUsuario));

        this.btnPossoAjudar = (Button)(findViewById(R.id.buttonPossoAjudar));
        this.btnPossoAjudar.setOnClickListener( btnPossoAjudarOnClickListener );

        this.mStorage = FirebaseStorage.getInstance();
        this.mStorageReference = this.mStorage.getReferenceFromUrl(storageURL);

        this.imageViewFoto = (ImageView) findViewById(R.id.imageViewFoto);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBarDetalheNecessidade);

        this.setDadosNecessidade(necId);
        this.setDadosUsuario(necId);
        this.setImageView(necId);
    }

    private View.OnClickListener btnPossoAjudarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(DetalheNecessidadeActivity.this, "Foi enviado um e-mail para o usuário "+textViewUsuario.getText()+" informado seu interesse em realizar a doação.",
                    Toast.LENGTH_LONG).show();
        }
    };

    private void setDadosNecessidade(String necId) {

        Query query = FirebaseDatabase.getInstance().getReference("usuarios/"+necId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( dataSnapshot.exists() ){

                    Necessidade necessidade = dataSnapshot.getValue(Necessidade.class);

                    textViewTipo.setText( necessidade.tipo );
                    textViewJustificativa.setText( necessidade.justificativa );
                    textViewDescricao.setText( necessidade.descricao );
                    textViewCreatedAt.setText( necessidade.getCreatedAt() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDadosUsuario(String necId) {

        Query query = FirebaseDatabase.getInstance().getReference("usuarios/"+necId)
                                      .getRef().getParent().getParent().child("usuario");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( dataSnapshot.exists() ){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    textViewUsuario.setText( usuario.nome );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if( item.getItemId() == android.R.id.home ){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setImageView(String necId){

        Query query = FirebaseDatabase.getInstance().getReference("usuarios/"+necId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( dataSnapshot.exists() ){

                    final long MEGABYTES = 1024*1024*5;

                    Necessidade necessidade = dataSnapshot.getValue(Necessidade.class);

                    if( necessidade.getImgUrl().isEmpty() ){

                    }else{
                        StorageReference imagemReference = mStorageReference.child("imagem").child(necessidade.getImgUrl());

                        imagemReference.getBytes(MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                imageViewFoto.setImageBitmap(bitmap);
                                imageViewFoto.setVisibility(View.VISIBLE);

                                mProgressBar.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("download123", e.getMessage().toString());
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

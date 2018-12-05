package com.example.tiago.roupas.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiago.roupas.R;
import com.example.tiago.roupas.activity.MainActivity;

public class DetalheNecessidadeActivity extends AppCompatActivity {

    private TextView textViewTipo, textViewJustificativa, textViewDescricao, textViewCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_necessidade_detalhe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String tipo = getIntent().getStringExtra("tipo");
        String justificativa = getIntent().getStringExtra("justificativa");
        String createdAt = getIntent().getStringExtra("createdAt");

        Toast.makeText(this, tipo, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, justificativa, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, createdAt, Toast.LENGTH_SHORT).show();


        this.textViewTipo = (TextView) (findViewById(R.id.textViewTipo));
        this.textViewJustificativa = (TextView) (findViewById(R.id.textViewJustificativa));
        this.textViewCreatedAt = (TextView) (findViewById(R.id.textViewCreatedAt));

        this.textViewTipo.setText( tipo );
        this.textViewJustificativa.setText( justificativa );
        this.textViewCreatedAt.setText( createdAt );

        //this.textViewTexto = (TextView) findViewById(R.id.);
        //this.textViewTexto.setText(texto);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if( item.getItemId() == android.R.id.home ){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

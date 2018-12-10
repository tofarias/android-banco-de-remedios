package com.br.tiago.roupas.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.br.tiago.roupas.R;
import com.br.tiago.roupas.fragment.CameraFragment;
import com.br.tiago.roupas.fragment.FragmentDownload;
import com.br.tiago.roupas.fragment.FragmentIBGE;
import com.br.tiago.roupas.fragment.FragmentNotificacao;
import com.br.tiago.roupas.fragment.FragmentUpload;
import com.br.tiago.roupas.fragment.FragmentUsuario;
import com.br.tiago.roupas.fragment.WifiReceiverFragment;
import com.br.tiago.roupas.fragment.cadastro.FragmentCadastro;
import com.br.tiago.roupas.fragment.home.FragmentHome;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
                                                               ,GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* INICIO AUTH */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /* FIM AUTH */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new FragmentHome());
        ft.commit();

        this.account = GoogleSignIn.getLastSignedInAccount(this);
        if( (this.account != null) ) {

            View headerView = navigationView.getHeaderView(0);
            TextView textViewUsuario = (TextView) headerView.findViewById(R.id.textViewUsuarioLabel);
            TextView textViewEmail = (TextView) headerView.findViewById(R.id.textViewEmail);
            ImageView imageViewUsuario = (ImageView) headerView.findViewById(R.id.imageViewUsuario);

            imageViewUsuario.setClipToOutline(true);
            textViewUsuario.setText(this.account.getDisplayName());
            textViewEmail.setText(this.account.getEmail());

            Glide.with(this)
                    .load(this.account.getPhotoUrl().toString())
                    .into(imageViewUsuario);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        this.displaySelectedScreen(itemId);

        return true;
    }


        private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;

        switch (itemId){
            case R.id.nav_user:
                fragment = new FragmentUsuario();
                break;
            /*case R.id.nav_medicine:
                fragment = new FragmentMedicamento();
                break;*/
            case R.id.nav_notificacoes:
                fragment = new FragmentNotificacao();
                break;
            case R.id.nav_sair:
                signOut();
                break;
            case R.id.nav_upload:
                fragment = new FragmentUpload();
                break;
            case R.id.nav_map:
                Intent myIntent = new Intent(this, MapActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_download:
                fragment = new FragmentDownload();
                break;
            case R.id.nav_retrofit_ibge:
                fragment = new FragmentIBGE();
                break;
            case R.id.nav_cadastro:
                fragment = new FragmentCadastro();
                break;
            case R.id.nav_wifi:
                fragment = new WifiReceiverFragment();
                break;
            case R.id.nav_home:
                fragment = new FragmentHome();
                break;
            case R.id.nav_camera:
                fragment = new CameraFragment();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.mFirebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = this.mFirebaseAuth.getCurrentUser();

        if( GoogleSignIn.getLastSignedInAccount(this) == null ){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void signOut(){

        this.mFirebaseAuth.signOut();
        Log.i("signOut","Desconectado do Firebase");

        Auth.GoogleSignInApi.signOut( this.mGoogleApiClient ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            Log.i("signOut","Desconectado do Google");
            }
        });

        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if( connectionResult != null ) {
            Toast.makeText(MainActivity.this, "Falha na autenticação: " + connectionResult.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}

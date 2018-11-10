package com.example.tiago.bancoderemedios.activity;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tiago.bancoderemedios.R;
import com.example.tiago.bancoderemedios.fragmet.FragmentMapa;
import com.example.tiago.bancoderemedios.fragmet.FragmentMedicamento;
import com.example.tiago.bancoderemedios.fragmet.FragmentUsuario;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
                                                               ,GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* INICIO AUTH */

        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if( this.mGoogleApiClient == null || !this.mGoogleApiClient.isConnected() ){
            startActivity(new Intent(this,LoginActivity.class));
            //finish();
        }*/

        /* FIM AUTH */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
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
            case R.id.nav_medicine:
                fragment = new FragmentMedicamento();
                break;
            case R.id.nav_map:
                //fragment = new FragmentMapa();
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                break;
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

        /*this.mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = this.mFirebaseAuth.getCurrentUser();*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if( connectionResult != null ) {
            Toast.makeText(MainActivity.this, "Falha na autenticação: " + connectionResult.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}

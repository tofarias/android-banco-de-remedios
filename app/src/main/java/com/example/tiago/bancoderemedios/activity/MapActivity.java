package com.example.tiago.bancoderemedios.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tiago.bancoderemedios.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Handler mHandler;
    int mTentativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        this.mHandler = new Handler();

        //PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                atualizarMapa(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.e("MAPS-APP", "Ocorreu um erro: "+status);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-30.028201, -51.227365);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Praça Quinze"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-30.028201, -51.227365), 17f));
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {

        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            this.mGoogleApiClient.disconnect();
        }

        this.mHandler.removeCallbacksAndMessages( null );

        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.verificarStatusGps();
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.mGoogleApiClient.connect();
    }

    private void obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPe rmissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },1);

            return;
        }else{
            //FusedLocationProviderClient location = LocationServices.getFusedLocationProviderClient(this);
            Location location = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);

            if( location != null ){
                this.mTentativas = 0;
                this.atualizarMapa(new LatLng(location.getLatitude(), location.getLongitude()));
            }else if( this.mTentativas < 10 ){
                this.mTentativas++;
                this.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        obterLocalizacao();
                    }
                },2000);
            }
        }
    }

    private void atualizarMapa(LatLng local){
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 17.0f));
        this.mMap.addMarker(new MarkerOptions().position(local).title("Local Atual!"));
    }

    private void verificarStatusGps(){
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder locationSettingRequest = new LocationSettingsRequest.Builder();
        locationSettingRequest.setAlwaysShow(true);
        locationSettingRequest.addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(this.mGoogleApiClient,locationSettingRequest.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch ( status.getStatusCode() ){
                    case LocationSettingsStatusCodes.SUCCESS:
                        obterLocalizacao();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try{
                            status.startResolutionForResult(MapActivity.this,2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("MAPS-APP", "Não foi possível obter a localização");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            if( resultCode == RESULT_OK ){
                this.mTentativas = 0;
                this.mHandler.removeCallbacksAndMessages( null );
                this.obterLocalizacao();
            }
        }
    }
}
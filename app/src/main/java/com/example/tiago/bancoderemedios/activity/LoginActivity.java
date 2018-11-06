package com.example.tiago.bancoderemedios.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tiago.bancoderemedios.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth    mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton    signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mFirebaseAuth = FirebaseAuth.getInstance();

        //findViewById(R.id.btnLogin).setOnClickListener(btnLogoutGoogleOnClickListener);
        findViewById(R.id.btnGooleLogin).setOnClickListener(btnLoginGoogleOnClickListener);
        //this.signInButton.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private View.OnClickListener btnLoginGoogleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if( result.isSuccess() ){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseLogin( account );
            }
        }
    }

    private void firebaseLogin(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        this.mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if( task.isSuccessful() ){

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            /*FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Log.i("FirebaseUser",user.getDisplayName());
                            Log.i("FirebaseEmail",user.getEmail());*/

                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this,task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if( connectionResult != null ){
            Toast.makeText(LoginActivity.this,"Falha na autenticação: "+connectionResult.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
package com.hkxps17.turnup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    String[] id = {"Guest"};

    final static String TAG = "LoginActivity";
    private int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button guest_button = findViewById(R.id.guest_button);
        guest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> idL = Arrays.asList(id);
                Set<String> idS = new HashSet<String>(idL);
                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this)
                        .edit()
                        .putStringSet("id", idS)
                        .commit();

                Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
                startActivity(intent);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.d(TAG,"OnCreate");
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"OnStart");

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            Log.d(TAG, "No user signed in");
        } else {
            //move to a new page here if login successful******
            Log.e(TAG, "Name: "+ account.getDisplayName());
            Log.e(TAG, "Fam Name: "+ account.getFamilyName());
            Log.e(TAG, "Email: "+ account.getEmail());
            Log.e(TAG, "Photo: "+ account.getPhotoUrl());

            //get token to send to back-end: account.getTokenID()

            id[0] = account.getEmail();
            List<String> idL = Arrays.asList(id);
            Set<String> idS = new HashSet<String>(idL);
            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this)
                    .edit()
                    .putStringSet("id", idS)
                    .commit();

            Log.d(TAG,"updateUI");

            Intent eventListActivityIntent = new Intent(LoginActivity.this, EventListActivity.class);
            startActivity(eventListActivityIntent);
        }
    }

    private void signIn() {
        Log.d(TAG,"signIN");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
}
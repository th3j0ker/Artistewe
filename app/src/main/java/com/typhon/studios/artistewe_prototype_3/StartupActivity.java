package com.typhon.studios.artistewe_prototype_3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.typhon.studios.artistewe_prototype_3.Home.MainActivity;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserRegisterationActivity;

import java.util.Arrays;

public class StartupActivity extends AppCompatActivity {
    private static final String TAG = "StartupActivity";
    private static final int RC_SIGN_IN = 123;
    private Context StartupContext = StartupActivity.this;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        checkIfUserIsAlreadyloggedIn();
    }

    public void checkIfUserIsAlreadyloggedIn(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            firebaseUserDataVerifier(FirebaseAuth.getInstance().getCurrentUser().getUid());
            // already signed in
        } else {
            setupSigninFirebaseUi();
            // not signed in
        }
    }

    public void setupSigninFirebaseUi(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build()
                                )).setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.artistewe_white_emblem)
                        .build(),RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(StartupContext, UserRegisterationActivity.class);
                startActivity(intent);
                //startActivity(SignedInActivity.createIntent(this, response));
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    //showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //showSnackbar(R.string.no_internet_connection);
                    return;
                }

                //showSnackbar(R.string.unknown_error);
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    public void firebaseUserDataVerifier(String currentuser){
        final Intent failedIntent = new Intent(StartupContext, UserRegisterationActivity.class);
        final Intent successIntent = new Intent(StartupContext, MainActivity.class);
        myRef = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(currentuser);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                if(userInformation.getFirstname().isEmpty()){
                    Toast.makeText(StartupContext, "checking firstname", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getLastname().isEmpty()){
                    Toast.makeText(StartupContext, "checking lastname", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getProfession().isEmpty()){
                    Toast.makeText(StartupContext, "checking profession", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getEmail().isEmpty()){
                    Toast.makeText(StartupContext, "checking email", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getProfileimage().isEmpty()){
                    Toast.makeText(StartupContext, "checking image", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getCountry().isEmpty()){
                    Toast.makeText(StartupContext, "checking country", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getAge().isEmpty()){
                    Toast.makeText(StartupContext, "checking age", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else if(userInformation.getUserid().isEmpty()){
                    Toast.makeText(StartupContext, "checking uid", Toast.LENGTH_SHORT).show();
                    startActivity(failedIntent);
                }else{
                    startActivity(successIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

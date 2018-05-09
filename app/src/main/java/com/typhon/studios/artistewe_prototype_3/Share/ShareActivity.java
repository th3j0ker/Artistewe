package com.typhon.studios.artistewe_prototype_3.Share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by root on 30/11/17.
 */

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    private Context primarycontext = ShareActivity.this;
    private static final int ACTIVITY_NUM = 3;
    private EditText myFirstNameEditText,
            myLastNameEditText,
            myAgeEditText,
            myProfessionEditText,
            myCountryEditText;
    private Button myUserRegisterSubmitButton;
    private ImageView myProfileImageSetterimageView;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_share);
        Log.d(TAG, "onCreate: started onCreate method");
        //bindViewByIds();
        //setupSubmitButton();
        setupEXBottomNavigationView();
        //setupProfileImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                myProfileImageSetterimageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void bindViewByIds(){
        myFirstNameEditText = (EditText)findViewById(R.id.myFirstNameEditText);
        myLastNameEditText = (EditText)findViewById(R.id.myLastNameEditText);
        myAgeEditText = (EditText)findViewById(R.id.myAgeEditText);
        myProfessionEditText = (EditText)findViewById(R.id.myProfesstionEditText);
        myCountryEditText = (EditText)findViewById(R.id.myCountryEditText);
    }
    public void setupProfileImage(){
        myProfileImageSetterimageView = (ImageView)findViewById(R.id.myProfileImageSetterImageView);
        myProfileImageSetterimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void setupSubmitButton(){
        myUserRegisterSubmitButton = (Button)findViewById(R.id.mySubmitUserRegisterButton);
        myUserRegisterSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myImageAddress = "images/"+ UUID.randomUUID().toString();
                String firstname = myFirstNameEditText.getText().toString();
                String lastname = myLastNameEditText.getText().toString();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String age = myAgeEditText.getText().toString();
                String profession = myProfessionEditText.getText().toString();
                String country = myCountryEditText.getText().toString();
                String uid = FirebaseAuth.getInstance().getUid();
                submitUserDataToFireBase(firstname,lastname,email,age,profession,country,uid,myImageAddress);
                uploadImageToFirebase(myImageAddress);
            }
        });
    }
    public void uploadImageToFirebase(final String imageUri){
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profilePics");
        //Firebase
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(imageUri);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void submitUserDataToFireBase(String firstname,
                                         String lastname,
                                         String email,
                                         String age,
                                         String profession,
                                         String country,
                                         String uid,
                                         String profileimage){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = database.getReference();
        UserInformation userInformation = new UserInformation();
        userInformation.setFirstname(firstname);
        userInformation.setLastname(lastname);
        userInformation.setEmail(email);
        userInformation.setAge(age);
        userInformation.setProfession(profession);
        userInformation.setCountry(country);
        userInformation.setUserid(uid);
        userInformation.setProfileimage(profileimage);
        myRef.child("RegisteredUsers").child(userID).setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(primarycontext, "successfully registered", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(primarycontext, "registeration failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //setup for our bottom navigation view so that we can later use it
    private void setupEXBottomNavigationView(){
        Log.d(TAG, "setupEXBottomNavigationView: setting up bottom navigation view ....");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavigationBarView);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(primarycontext, bottomNavigationViewEx);
       // BottomNavigationViewHelper.setupIconSize(bottomNavigationViewEx,32,this);

        //after using the above code you may not be able to highlight the icons
        //we do the following in order to do so
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
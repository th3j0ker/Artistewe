package com.typhon.studios.artistewe_prototype_3.UserRegisteration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.typhon.studios.artistewe_prototype_3.Home.MainActivity;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 30/11/17.
 */

public class UserRegisterationActivity extends AppCompatActivity {
    private static final String TAG = "UserRegisterationActivi";
    private Context primarycontext = UserRegisterationActivity.this;
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
        setContentView(R.layout.activity_user_registeration);
        Log.d(TAG, "onCreate: started onCreate method");
        bindViewByIds();
        setupSubmitButton();
        setupProfileImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathprofile = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathprofile);
                myProfileImageSetterimageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void bindViewByIds(){
        Log.d(TAG, "bindViewByIds: ");
        myFirstNameEditText = (EditText)findViewById(R.id.myFirstNameEditText);
        myLastNameEditText = (EditText)findViewById(R.id.myLastNameEditText);
        myAgeEditText = (EditText)findViewById(R.id.myAgeEditText);
        myProfessionEditText = (EditText)findViewById(R.id.myProfesstionEditText);
        myCountryEditText = (EditText)findViewById(R.id.myCountryEditText);
    }
    public void setupProfileImage(){
        Log.d(TAG, "setupProfileImage: ");
        myProfileImageSetterimageView = (ImageView)findViewById(R.id.myProfileImageSetterImageView);
        myProfileImageSetterimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private Uri filePathprofile;
    private final int PICK_IMAGE_REQUEST = 7121;
    private void chooseImage() {
        Log.d(TAG, "chooseImage: ");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void setupSubmitButton(){
        Log.d(TAG, "setupSubmitButton: ");
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

                if(firstname.isEmpty()){
                    popUpAlertMessage("Firstname");
                }else if(lastname.isEmpty()){
                    popUpAlertMessage("Lastname");
                }else if(age.isEmpty()){
                    popUpAlertMessage("Age");
                }else if(profession.isEmpty()){
                    popUpAlertMessage("Profession");
                }else if(country.isEmpty()){
                    popUpAlertMessage("Country");
                }else{

                    submitUserDataToFireBase(capitalize(firstname),
                            capitalize(lastname),
                            email,
                            age,
                            capitalize(profession),
                            capitalize(country),
                            uid,
                            myImageAddress);

                    uploadImageToFirebase(myImageAddress);
                }
            }
        });
    }
    public void popUpAlertMessage(String text){
        new AlertDialog.Builder(this)
                .setMessage("Sorry, "+text+" cannot be empty")
                .setNegativeButton("close", null)
                .show();
    }
    public void uploadImageToFirebase(final String imageUri){
        Log.d(TAG, "uploadImageToFirebase: ");
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profilePics");
        //Firebase
        if(filePathprofile != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(imageUri);
            ref.putFile(filePathprofile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
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

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public void submitUserDataToFireBase(String firstname,
                                         String lastname,
                                         String email,
                                         String age,
                                         final String profession,
                                         String country,
                                         String uid,
                                         String profileimage){
        Log.d(TAG, "submitUserDataToFireBase: ");
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
                    Intent intent = new Intent(primarycontext, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(primarycontext, "registeration failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Please enter information and press submit")
                .setNegativeButton("Got it!", null)
                .show();


    }

}
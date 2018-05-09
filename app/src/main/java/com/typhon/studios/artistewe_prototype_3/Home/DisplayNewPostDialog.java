package com.typhon.studios.artistewe_prototype_3.Home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.Models.LikesHandler;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class DisplayNewPostDialog {

    public DisplayNewPostDialog() {
    }

    public DisplayNewPostDialog(Context dialogContext, Activity dialogActivity) {
        this.dialogContext = dialogContext;
        this.dialogActivity = dialogActivity;
    }

    public Context dialogContext;
    public Activity dialogActivity;
    public EditText titleEditText,bodyeditText;
    public ImageView imagePostView;
    public Button postButton;
    public String myFirstname,myLastname,myUniqueid;
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public String selectedImageUri = "hello";
    public Uri filePath;
    public final int PICK_IMAGE_REQUEST = 71;

    public DatabaseReference databaseReference;
    public FirebaseStorage storage;
    public StorageReference storageReferenceUploadImage;

    public void postNewUpdate(){
        displayDialog();
    }

    public Uri getFilePath() {
        return filePath;
    }

    public void setFilePath(Uri filePath) {
        this.filePath = filePath;
    }

    public int getPICK_IMAGE_REQUEST() {
        return PICK_IMAGE_REQUEST;
    }

    public void displayDialog(){

        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(currentuser);

        try{

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation information = dataSnapshot.getValue(UserInformation.class);
                    myFirstname = information.getFirstname();
                    myLastname = information.getLastname();
                    myUniqueid = information.getUserid();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){

        }

        Dialog dialog = new Dialog(dialogContext);

        dialog.setTitle("NewPost");
        selectedImageUri = "hello";
        dialog.setContentView(R.layout.snippet_dialogue_for_input_homefragment);
        titleEditText = (EditText)dialog.findViewById(R.id.cardTitleEditText);
        bodyeditText = (EditText)dialog.findViewById(R.id.cardBodyEditText);
        imagePostView = (ImageView)dialog.findViewById(R.id.mypostDialogueHomeFragment);
        imagePostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImageUri = "images/"+ UUID.randomUUID().toString();
                chooseImage();
            }
        });
        postButton = (Button)dialog.findViewById(R.id.myNewPostButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String body = bodyeditText.getText().toString();
                //String email = "replaceThisWithFireAuth@firebase.Com";
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                uploadDataToFirebase(title,body,email,selectedImageUri,myFirstname,myLastname,myUniqueid);
                //fireBaseNewPostHelper.savedata(title,body);
            }
        });
        dialog.show();
    }
    public void uploadDataToFirebase(String title, String body, String email, final String imageuri, String firstname, String lastname, String uniqueid){
        Toast.makeText(dialogContext, "you entered "+title+" and "+body+"and the user is "+email, Toast.LENGTH_LONG).show();
        DatabaseReference myRef = database.getReference();
        String pushedNodeValue = myRef.child("posts").push().getKey();
        String pushedLikesNodeValue = myRef.child("posts").push().getKey();
        NewPost newPost = new NewPost();
        LikesHandler likesHandler = new LikesHandler();
        likesHandler.setPostid(pushedNodeValue);
        likesHandler.setUserd(uniqueid);
        newPost.setLikecount(0);
        newPost.setTitle(title);
        newPost.setBody(body);
        newPost.setEmail(email);
        newPost.setImageuri(imageuri);
        newPost.setFirstname(firstname);
        newPost.setLastname(lastname);
        newPost.setUniqueid(uniqueid);
        newPost.setLiked(false);
        newPost.setSaved(false);
        newPost.setNodevalue(pushedNodeValue);
        myRef.child("posts").child("main").child(pushedNodeValue).setValue(newPost);

        storageReferenceUploadImage = storage.getReference().child("Post");
        //Firebase
        if(filePath != null)
        {
            setFilePath(filePath);
            final ProgressDialog progressDialog = new ProgressDialog(dialogContext);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReferenceUploadImage.child(imageuri);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(dialogContext, "Uploaded", Toast.LENGTH_SHORT).show();
                            selectedImageUri = "hello";
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(dialogContext, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        dialogActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}

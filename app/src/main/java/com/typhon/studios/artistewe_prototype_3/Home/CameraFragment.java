package com.typhon.studios.artistewe_prototype_3.Home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.typhon.studios.artistewe_prototype_3.Home.FloatingActionButtonElements.BaseFragment;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.Models.LikesHandler;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;
import com.typhon.studios.artistewe_prototype_3.Profile.ProfileActivity;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserRegisterationActivity;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.util.RFABShape;
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 30/11/17.
 */

public class CameraFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{
    private static final String TAG = "CameraFragment";
    private DatabaseReference myRef;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReferenceUploadImage;
    private StorageReference storageReferenceProfilePicture;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera,container,false);
        bindViewsByID(view);
        rfaLayout = (RapidFloatingActionLayout) view.findViewById(R.id.rfab_group_sample_fragment_a_rfal_fragment_camera);
        return view;
    }

    //=======================================================FloatingActionButton==========================================================================================
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            initRFAB();
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        if(position == 0){
            postNewUpdate();
        }else{
            Toast.makeText(getActivity(), "someting else: " + position, Toast.LENGTH_SHORT).show();
        }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        Toast.makeText(getActivity(), "clicked icon: " + position, Toast.LENGTH_SHORT).show();
        rfabHelper.toggleContent();
    }

    @Override
    public void onInitialRFAB(RapidFloatingActionButton rfab) {
        this.rfaButton = rfab;
        initRFAB();
    }

    private void initRFAB() {
        if (null == rfaButton) {
            return;
        }

        /*
        // 可通过代码设置属性
        rfaLayout.setFrameColor(Color.RED);
        rfaLayout.setFrameAlpha(0.4f);

        rfaBtn.setNormalColor(0xff37474f);
        rfaBtn.setPressedColor(0xff263238);
        rfaBtn.getRfabProperties().setShadowDx(ABTextUtil.dip2px(context, 3));
        rfaBtn.getRfabProperties().setShadowDy(ABTextUtil.dip2px(context, 3));
        rfaBtn.getRfabProperties().setShadowRadius(ABTextUtil.dip2px(context, 5));
        rfaBtn.getRfabProperties().setShadowColor(0xffcccccc);
        rfaBtn.getRfabProperties().setStandardSize(RFABSize.MINI);
        rfaBtn.build();
        */
        Context context = getActivity();
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Post Update")
                .setResId(R.drawable.ic_upload_white)
                .setIconNormalColor(0xff6a1b9a)
                .setIconPressedColor(0xff4a148c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Global Top Ten")
                .setResId(R.drawable.ic_search_white_trim)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(context, 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Settings")
                        .setResId(R.drawable.ic_settings_white)
                        .setIconNormalColor(0xff4e342e)
                        .setIconPressedColor(0xff3e2723)
                        .setLabelColor(Color.WHITE)
                        .setLabelSizeSp(14)
                        .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(context, 4)))
                        .setWrapper(2)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(RFABTextUtil.dip2px(context, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(RFABTextUtil.dip2px(context, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }


    @Override
    public String getRfabIdentificationCode() {
           String resources = "RFAB_IDENTIFICATION_CODE_FRAGMENT_A";
           return resources;
    }

    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }



    //==============================================================================================================================================================================

    private void bindViewsByID(View view){
        storage = FirebaseStorage.getInstance();
        storageReferenceProfilePicture = storage.getReference().child("profilePics");
        try {
            final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            myRef = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(currentuser);
            final CircleImageView circleImageView = (CircleImageView)view.findViewById(R.id.myMainFragmentCircleWelcomeMessage);
            final TextView textView = (TextView)view.findViewById(R.id.myMainFragmentTextViewWelcomeMessage);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    String name = userInformation.getFirstname();
                    String imageuri = userInformation.getProfileimage();
                    String welcome_message = "Welcome "+name+"! see what's trending ";
                    if(currentuser!=null){
                        textView.setText(welcome_message);
                    }
                    if(imageuri!=null) {
                        storageReferenceProfilePicture.child(imageuri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext())
                                        .load(uri)
                                        .into(circleImageView);
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "image not found client not logged in", Toast.LENGTH_SHORT).show();
        }

    }

    //===================================================Post==========================================================
    EditText titleEditText,bodyeditText;
    ImageView imagePostView;
    Button postButton;
    String myFirstname,myLastname,myUniqueid;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    String selectedImageUri = "hello";
    boolean imageisclicked = false;

    private void postNewUpdate(){
        displayDialog();
    }

    private void displayDialog(){

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

        final Dialog dialog = new Dialog(getContext());

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
                Log.d(TAG, "onClick: post button for new post dialogue");
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
        Toast.makeText(getContext(), "you entered "+title+" and "+body+"and the user is "+email, Toast.LENGTH_LONG).show();
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
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReferenceUploadImage.child(imageuri);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            selectedImageUri = "hello";
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int POST_PICK_IMAGE_REQUEST = 7211;
    private Button setButtonImageUpload;
    private Button uploadButtonImageUpload;
    private ImageView myUploadImageToFireBaseImageView;


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                //myUploadImageToFireBaseImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
            imagePostView.setImageBitmap(bitmap);

        }catch (IOException ee ){
            ee.printStackTrace();
        }
        }
    }

    //========================================================================================================================================
}

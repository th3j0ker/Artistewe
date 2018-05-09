package com.typhon.studios.artistewe_prototype_3.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;
import com.typhon.studios.artistewe_prototype_3.Utils.GridImageAdapter;
import com.typhon.studios.artistewe_prototype_3.Utils.UniversalImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 30/11/17.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private Context primarycontext = ProfileActivity.this;
    private static final int ACTIVITY_NUM = 2;
    private static final int NUM_GRID_COLUMNS = 3;

    private ProgressBar profileProgressbar;
    private ImageView myProfilePhoto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started onCreate method");
        setupEXBottomNavigationView();
        //setupWidgetsForActivity(); --old
        //setProfileImage(); -- old
       //setupAccountSettingsMenu();
       setupManageProfileCard();
       displayUserInfo();
        //tempGridSetup(); --old
        //DEPRECATED
        //setupToolBarForProfileBar();
    }

    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private void displayUserInfo(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profilePics");
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(currentuser);
        final TextView myFullName = (TextView)findViewById(R.id.myProfilePersonalFullNameTextView);
        final TextView myProfession = (TextView)findViewById(R.id.myProfilePersonalProfessionTextView);
        final TextView myWebsite = (TextView)findViewById(R.id.myProfilePersonalWebsiteTextView);
        final SimpleDraweeView ProfilePicsimpleDraweeView = (SimpleDraweeView)findViewById(R.id.myProfilePersonalProfilePicCircleDraweeView);
        //final CircleImageView myprofileimage = (CircleImageView)findViewById(R.id.myProfilePersonalProfilePicCircleImageView);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                String fullname = userInformation.getFirstname()+" "+userInformation.getLastname();
                String profession = userInformation.getProfession();
                String website = userInformation.getEmail();
                final String profileimageuri = userInformation.getProfileimage();
                myFullName.setText(fullname);
                myProfession.setText(profession);
                myWebsite.setText(website);

                storageReference.child(profileimageuri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ProfilePicsimpleDraweeView.setImageURI(uri);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void tempGridSetup(){
        ArrayList<String> imageURLs = new ArrayList<>();
        imageURLs.add("http://78.media.tumblr.com/31248e0ca8168a1bd675df87f7d8639f/tumblr_inline_mkz12xfB1b1qb67j7.jpg\n");
        imageURLs.add("https://loremflickr.com/320/240/brazil,rio");
        imageURLs.add("https://loremflickr.com/320/240/dog");
        imageURLs.add("https://loremflickr.com/g/320/240/paris,girl/all");
        imageURLs.add("https://loremflickr.com/320/240");
        imageURLs.add("https://loremflickr.com/g/320/240/paris,girl/all");
        imageURLs.add("https://static.pexels.com/photos/257360/pexels-photo-257360.jpeg");
        imageURLs.add("http://images.all-free-download.com/images/graphiclarge/canoe_water_nature_221611.jpg");
        imageURLs.add("https://www.hdwallpapers.in/walls/nature_call-wide.jpg");
        imageURLs.add("https://static.pexels.com/photos/39811/pexels-photo-39811.jpeg");
        imageURLs.add("https://images.unsplash.com/photo-1442850473887-0fb77cd0b337?dpr=1&auto=format&fit=crop&w=376&h=251&q=60&cs=tinysrgb&ixid=dW5zcGxhc2guY29tOzs7Ozs%3D");

        setUpImageGrids(imageURLs);
    }

    public void setUpImageGrids(ArrayList<String> imageURLs){
        GridView myGridview = (GridView)findViewById(R.id.UploadedGridView);


        /*
        *
        * without this the images wont get their appropriate ratio
        *
        * inside of the gridview
        *
        * */

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        myGridview.setColumnWidth(imageWidth);

        GridImageAdapter gridImageAdapter = new GridImageAdapter(primarycontext,R.layout.layout_grid_image_view,"",imageURLs);
        myGridview.setAdapter(gridImageAdapter);
    }

    private  void setupAccountSettingsMenu(){
        ImageView accountSettingsImageView = (ImageView)findViewById(R.id.profileMenuImageView);
        accountSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings....");
                Intent intent = new Intent(primarycontext,AccountSettingsActivity.class);
                startActivity(intent);

            }
        });
    }
    private void setupManageProfileCard(){
        CardView manageProfileCard = (CardView)findViewById(R.id.myProfileActivityManageProfileCardView);
        manageProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(primarycontext,AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupWidgetsForActivity(){
        profileProgressbar = (ProgressBar)findViewById(R.id.profileProgressBar);
        profileProgressbar.setVisibility(View.GONE);
        myProfilePhoto = (ImageView)findViewById(R.id.profileImageCircleView);
    }
    public void setProfileImage(){
        Log.d(TAG, "setProfileImage: started.....");
        String imageURL = "http://tfwiki.net/mediawiki/images2/thumb/4/43/Cloud_Optimus_Prime.jpg/180px-Cloud_Optimus_Prime.jpg";
        UniversalImageLoader.setImage(imageURL,myProfilePhoto,null,"");
    }

    //DEPRECATED
    //to set up our menu item in tool bar of the profile page
//    private void setupToolBarForProfileBar(){
//        //our primary layout toolbar
//        Toolbar toolbar = (Toolbar)findViewById(R.id.profilePrimeToolBar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    //our menu item the we created in our xml file
//                    case R.id.profilePrimeItemMenu:
//                        Log.d(TAG, "onMenuItemClick: menu iten clicked on onMenuItem Listner"+item);
//                        break;
//                }
//                return false;
//            }
//        });
//    }



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
package com.typhon.studios.artistewe_prototype_3.Home;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.typhon.studios.artistewe_prototype_3.AdditionalWedgets.MovableFloatingActionButton;
import com.typhon.studios.artistewe_prototype_3.Home.FloatingActionButtonElements.BaseFragment;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.HomeFragment;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Share.ShareActivity;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserRegisterationActivity;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;
import com.typhon.studios.artistewe_prototype_3.Utils.SectionsPagerAdapter;
import com.typhon.studios.artistewe_prototype_3.Utils.UniversalImageLoader;
import com.typhon.studios.artistewe_prototype_3.extras.FireBaseNewPostHelper;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonGroupListener;
import com.wangjie.rapidfloatingactionbutton.rfabgroup.RapidFloatingActionButtonGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnRapidFloatingButtonGroupListener {
    private static final String TAG = "MainActivity";
    private Context primarycontext = MainActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;

    EditText titleEditText,bodyeditText;
    ImageView imagePostView;
    Button postButton;
    FirebaseStorage storage;
    StorageReference storageReference;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    FireBaseNewPostHelper fireBaseNewPostHelper;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Fresco.initialize(this);
    }

    //this is to set up firebase reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: oncreate started....");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Post");

        viewPager = (ViewPager) findViewById(R.id.myContainerViewPager);
        //setupViewPager(viewPager);
        setupBaseFragmentViewPager(viewPager);
        //setupFirebaseAuth();
        //cameraClicked();
        initializeImageLoader();
        setupEXBottomNavigationView();
        //setupViewPagerSectionsAdapter();
        setupHomeFragment();
        //setupFloatingActionButton();
        setupMovaFloatingActionButton();
        cameraButtonClicked();
        setupTopTabs();
        checkIfUserIsAlreadyloggedIn();
       // incompleteInformationHandler();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            setupSigninFirebaseUi();
        }
    }

    public void incompleteInformationHandler(){
        UserInformation userInformation = new UserInformation();
        if(FirebaseAuth.getInstance().getCurrentUser() != null &&
                userInformation.getFirstname() == null
                || userInformation.getLastname() == null
                || userInformation.getAge() == null
                || userInformation.getCountry() == null
                || userInformation.getProfileimage() == null
                || userInformation.getEmail() == null
                ||userInformation.getProfession() == null){
            AuthUI.getInstance()
                    .signOut(this);
            Intent intent = new Intent(MainActivity.this, UserRegisterationActivity.class);
            startActivity(intent);
        }
    }

    public void setupTopTabs(){
        //adds to our tab
        TabLayout tabLayout = (TabLayout)findViewById(R.id.myToolbarTabLayoutTabs);
        tabLayout.setupWithViewPager(viewPager);
        ImageView tabOne = (ImageView) LayoutInflater.from(this).inflate(R.layout.test_layout_top_tabs_home_tab, null);
        tabLayout.getTabAt(1).setCustomView(tabOne);
        ImageView tabtwo = (ImageView) LayoutInflater.from(this).inflate(R.layout.test_top_tabs_camera_fragment,null);
         //set icons for top tabs toolbas+tabblayout
        tabLayout.getTabAt(0).setCustomView(tabtwo);
        //   tabLayout.getTabAt(1).setIcon(R.drawable.ic_temp_primary_icon);
        ImageView tabthree = (ImageView) LayoutInflater.from(this).inflate(R.layout.test_top_tabs_messages_fragment,null);
        tabLayout.getTabAt(2).setCustomView(tabthree);
    }

    public void initializeImageLoader(){
        Log.d(TAG, "initializeImageLoader: started....");
        UniversalImageLoader universalimageloader = new UniversalImageLoader(primarycontext);
        ImageLoader.getInstance().init(universalimageloader.getConfig());
    }

//===========================================================================FloatingActionbutton================================================================
    private RapidFloatingActionButtonGroup rfabGroup;
    private List<BaseFragment> fragments = new ArrayList<>();

    private void setupBaseFragmentViewPager(ViewPager pager){
        rfabGroup = (RapidFloatingActionButtonGroup) findViewById(R.id.rfab_group_sample_activity_main);
        rfabGroup.setOnRapidFloatingButtonGroupListener(this);

        fragments.add(new CameraFragment());
        fragments.add(new HomeFragment());
        fragments.add(new MessagesFragment());

        pager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                rfabGroup.setSection(position);
            }

        });

        pager.setOffscreenPageLimit(3);

    }

    @Override
    public void onRFABGPrepared(RapidFloatingActionButtonGroup rapidFloatingActionButtonGroup) {
        for (BaseFragment fragment : fragments) {
            fragment.onInitialRFAB(rapidFloatingActionButtonGroup.getRFABByIdentificationCode(fragment.getRfabIdentificationCode()));
        }
    }

    class MyPageAdapter extends FragmentStatePagerAdapter {

        public MyPageAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }
    }
//=========================================================================FloatingActionButotn==================================================================================
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

    //thiis code sets up view pager for home, camera and messages
    private void setupViewPagerSectionsAdapter(){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragmentToList(new CameraFragment());
        sectionsPagerAdapter.addFragmentToList(new HomeFragment());
        sectionsPagerAdapter.addFragmentToList(new MessagesFragment());
        ViewPager viewPager = (ViewPager)findViewById(R.id.myContainerViewPager);
        viewPager.setAdapter(sectionsPagerAdapter);

        //adds to our tab
        TabLayout tabLayout = (TabLayout)findViewById(R.id.myToolbarTabLayoutTabs);
        tabLayout.setupWithViewPager(viewPager);


        /*
        * this is just for testing purpose -----------------------------------------------------------------------------------
        *
        * */

        ImageView tabOne = (ImageView)
                LayoutInflater.from(this).inflate(R.layout.test_layout_top_tabs_home_tab, null);
        tabLayout.getTabAt(1).setCustomView(tabOne);

 /*       View view1 = getLayoutInflater().inflate(R.layout.test_layout_top_tabs_home_tab, null);

        view1.findViewById(R.id.artisteweTabIconView).setBackgroundResource(R.drawable.artistewe_white_emblem);

        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));*/


        /*
        * this is just for testing purpose -----------------------------------------------------------------------------------
        *
        * */
        //set icons for top tabs toolbas+tabblayout
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_painting_white_trim).setText("");
     //   tabLayout.getTabAt(1).setIcon(R.drawable.ic_temp_primary_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_forums_white_trim);

    }

    private void cameraButtonClicked(){
    //    ImageView imageView = (ImageView)findViewById(R.id.myCameraHomeImageButton);
      //  imageView.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View v) {
                //displayDialogueForImageUpload();
                /*
                * Intent Intent3=new   Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivity(Intent3);
                *
                *
                * */
        //    }
     //   });
    }

    private void setupHomeFragment(){
        ListFragment fragment = new ListFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myMainActivityLayout,fragment);
        fragmentTransaction.commit();
        fragmentTransaction.detach(fragment);
    }


    private void setupMovaFloatingActionButton(){
       // MovableFloatingActionButton movableFloatingActionButton = (MovableFloatingActionButton) findViewById(R.id.myMovableFloatingActionButtonMainActivity);
       // movableFloatingActionButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
           // public void onClick(View view) {
                //displayDialog(view);
         //       Log.d(TAG, "onClick: for new post was successful");
                //displayDialogNewPost();
             //   String imageuri = "images/"+ UUID.randomUUID().toString();
               // displayDialog(imageuri);
           // }
        //});
    }

    private void displayDialogNewPost(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("NewPost");
        dialog.setContentView(R.layout.snippet_dialogue_for_input_homefragment);
        titleEditText = (EditText)dialog.findViewById(R.id.cardTitleEditText);
        bodyeditText = (EditText)dialog.findViewById(R.id.cardBodyEditText);
        postButton = (Button)dialog.findViewById(R.id.myNewPostButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: post button for new post dialogue");
                String title = titleEditText.getText().toString();
                String body = bodyeditText.getText().toString();
                String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                uploadDataToFirebaseForNewPost(title,body,email);
                //fireBaseNewPostHelper.savedata(title,body);
            }
        });
        dialog.show();
    }

    public void uploadDataToFirebaseForNewPost(String title,String body,String email){
        Toast.makeText(primarycontext, "you entered "+title+" and "+body, Toast.LENGTH_LONG).show();
        DatabaseReference myRef = database.getReference();
        NewPost newPost = new NewPost();
        newPost.setTitle(title);
        newPost.setBody(body);
        newPost.setEmail(email);
        myRef.child("NewPost").push().setValue(newPost);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void displayDialog(final String imageuri){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("NewPost");
        dialog.setContentView(R.layout.snippet_dialogue_for_input_homefragment);
        titleEditText = (EditText)dialog.findViewById(R.id.cardTitleEditText);
        bodyeditText = (EditText)dialog.findViewById(R.id.cardBodyEditText);
        imagePostView = (ImageView)dialog.findViewById(R.id.mypostDialogueHomeFragment);
        imagePostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                uploadDataToFirebase(title,body,email,imageuri);
                uploadImageToFirebase(imageuri);
                //fireBaseNewPostHelper.savedata(title,body);
            }
        });
        dialog.show();
    }
    public void uploadDataToFirebase(String title,String body,String email,String imageuri){
        Toast.makeText(primarycontext, "you entered "+title+" and "+body+"and the user is "+email, Toast.LENGTH_LONG).show();
        DatabaseReference myRef = database.getReference();
        NewPost newPost = new NewPost();
        newPost.setTitle(title);
        newPost.setBody(body);
        newPost.setEmail(email);
        newPost.setImageuri(imageuri);
        myRef.child("posts").push().setValue(newPost);
    }

    //------------------------------------------------------FIREBASE SIGNUP UI---------------------------------------------------------------------------------------------
//===========================================================================================================================================================

    public void checkIfUserIsAlreadyloggedIn(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
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
                Intent intent = new Intent(primarycontext, UserRegisterationActivity.class);
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


        //uupload image to firebase one of ts part
        //see upload image to firebase section


        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //myUploadImageToFireBaseImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            imagePostView.setImageBitmap(bitmap);

        }catch (IOException ee ){
            ee.printStackTrace();
        }
        }

    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //=============================================================================================================================================================
    private Uri filePath,postfilepath;
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

    public void uploadImageToFirebase(String imageuri){
        //Firebase
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(imageuri);
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

    //=============================================================================================================================================================



}

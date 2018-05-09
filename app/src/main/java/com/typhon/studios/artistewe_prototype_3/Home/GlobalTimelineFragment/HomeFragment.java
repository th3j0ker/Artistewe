package com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.typhon.studios.artistewe_prototype_3.Home.DisplayNewPostDialog;
import com.typhon.studios.artistewe_prototype_3.Home.FloatingActionButtonElements.BaseFragment;
import com.typhon.studios.artistewe_prototype_3.Home.ListAdapterHomeFragment;
//import com.typhon.studios.artistewe_prototype_3.Home.RecyclerViewAdapter;
import com.typhon.studios.artistewe_prototype_3.Likes.LikesActivity;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Utils.NotificationsHelper;
import com.typhon.studios.artistewe_prototype_3.extras.FireBaseNewPostHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonSeparateListener;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Created by root on 30/11/17.
 */

public class HomeFragment extends BaseFragment implements OnRapidFloatingButtonSeparateListener {

    Activity activity = getActivity();

    private static final String TAG = "HomeFragment";
    final static String database_url = "https://artistewe.firebaseio.com/";
    EditText titleEditText,bodyeditText;
    Button postButton;
    FireBaseNewPostHelper fireBaseNewPostHelper;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private RecyclerView myRecycleView;
    private DatabaseReference myRef;
    private FirebaseRecyclerAdapter<NewPost,HomeFragment.NewPostViewHolder> myrecAdapter;

    private RapidFloatingActionButton rfaButton;
    private StorageReference storageReference;
    private DatabaseReference databaseReferenceforlikes;
    private DatabaseReference databaseReferenceforNotifyMe;
    private static final String CHANNEL_ID = "NotificationButton";

    private boolean LIKES_PROCESS_RUNNING = false;
    private boolean NOTIFY_ME_PROCESS_RUNNING = false;
    private boolean DISLIKES_PROCESS_RUNNING = false;
    private boolean IMAGE_SWAP_PROCESS_RUNNING = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        //setupRecyclerView(view);
        //setupRealtimeDatabaseTimeline(view);
        if (isAdded() && activity != null) {
        }

        setupNewPostsRecyclerView(view);

        return view;
    }

    //==================================================================FloatingActionButton====================================================================================

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == rfaButton) {
            return;
        }
        rfaButton.setOnRapidFloatingButtonSeparateListener(this);
    }

    @Override
    public void onInitialRFAB(RapidFloatingActionButton rfab) {
        this.rfaButton = rfab;
        if (null == rfaButton) {
            return;
        }
        rfaButton.setOnRapidFloatingButtonSeparateListener(this);
    }

    String rfabreturnString;
    @Override
    public String getRfabIdentificationCode() {
       String resource = " RFAB_IDENTIFICATION_CODE_FRAGMENT_B";
        return resource;
    }

    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onRFABClick() {
        Toast.makeText(getActivity(), "B RFAB clicked", Toast.LENGTH_SHORT).show();

    }

    //====================================================================================================================================================================
    private FirebaseStorage storage;
    private String nodevalue;
    public void setupNewPostsRecyclerView(final View view){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Post");
        myRef = FirebaseDatabase.getInstance().getReference().child("posts").child("main");
        myRef.keepSynced(true);

        databaseReferenceforlikes = FirebaseDatabase.getInstance().getReference().child("posts").child("likes");
        databaseReferenceforNotifyMe = FirebaseDatabase.getInstance().getReference().child("posts").child("notifyme");
        databaseReferenceforlikes.keepSynced(true);
        databaseReferenceforNotifyMe.keepSynced(true);

        myRecycleView = (RecyclerView)view.findViewById(R.id.recyclerViewHomeFragment);
        myRecycleView.hasFixedSize();
        ((SimpleItemAnimator) myRecycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        myRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("posts").child("main");
        Query personsQuery = personsRef.orderByKey();
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<NewPost>().setQuery(personsQuery,NewPost.class).build();
        myrecAdapter = new FirebaseRecyclerAdapter<NewPost, HomeFragment.NewPostViewHolder>(personsOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final HomeFragment.NewPostViewHolder holder, final int position, @NonNull NewPost model) {

                final TextView myLikesCountTextView = (TextView)holder.myview.findViewById(R.id.cardViewLikesCount);
                final TextView myNotifisCountTextView = (TextView)holder.myview.findViewById(R.id.cardViewWishesCount);
                ImageView myLikesImageView = (ImageView)holder.myview.findViewById(R.id.cardViewStarImageButton);
                ImageView myNotifyMeImageView = (ImageView)holder.myview.findViewById(R.id.cardViewNotificationImageButton);
                //ImageView myPostImageView = (ImageView)holder.myview.findViewById(R.id.cardViewImagePostViewGlobalTimeline);

                String postNodeValue = getRef(position).getKey();
                DatabaseReference postReference = getRef(position);
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                holder.setTitle(model.getTitle());
                holder.setBody(model.getBody());;
                final String imageuri = model.getImageuri();
                String firstname = model.getFirstname();
                String lastname = model.getLastname();
                String uid = model.getUniqueid();
                String fullname = firstname+" "+lastname;
                holder.setFullName(fullname);
                // holder.setupTimelineImages(myImageuri,myPostImageView,position);

                holder.setupLikes(currentUser,postNodeValue,myLikesImageView,postReference);
                holder.toggleLikeButton(myLikesImageView,currentUser,postNodeValue,databaseReferenceforlikes);
                holder.toggleLikesCount(databaseReferenceforlikes,myLikesCountTextView,postNodeValue);

                holder.setupNotifyMe(currentUser,postNodeValue,myNotifyMeImageView,postReference);
                holder.toggleNotifyMeButton(myNotifyMeImageView,currentUser,postNodeValue,databaseReferenceforNotifyMe);
                holder.toggleNotifyMeCount(databaseReferenceforNotifyMe,myNotifisCountTextView,postNodeValue);
                holder.setupTimeLineImagesWithDraweeView(imageuri);
                //holder.setupLikes(model.isLiked(),model,model.getNodevalue());


                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), imageuri, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @NonNull
            @Override
            public HomeFragment.NewPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.global_timeline_single_item_home_fragment, parent, false);
                return new HomeFragment.NewPostViewHolder(view);
            }
        };

        myRecycleView.setAdapter(myrecAdapter);
    }
    public class NewPostViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public NewPostViewHolder(View itemView) {
            super(itemView);
            myview = itemView;
        }
        public void setTitle(String title){
            TextView mytitle = (TextView)myview.findViewById(R.id.cardViewTitleGlobalTimeLine);
            mytitle.setText(title);
        }
        public void setBody(String body){
            TextView mybody = (TextView)myview.findViewById(R.id.cardViewContentGlobalTimeline);
            mybody.setText(body);
        }
        public void setTime(long time){
            // TextView mytime = (TextView)myview.findViewById(R.id.cardTimeTextView);
            // NewPost np = new NewPost();
            // mytime.setText(DateFormat.format("dd-mm-yyyy (HH:mm:ss)",np.getTime()));
        }
        public void setEmail(String email){
            // TextView myemail = (TextView)myview.findViewById(R.id.cardUserNameTextView);
            //myemail.setText(email);
        }
        public void setFullName(String fullname){
            TextView myfullname = (TextView)myview.findViewById(R.id.cardViewFullNameGlobalTimeline);
            myfullname.setText(fullname);
        }
        public void setupLikes(final String currentUser, final String postNodeValue,final ImageView myLikesImageView,final DatabaseReference postreference) {
            myLikesImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LIKES_PROCESS_RUNNING = true;
                    databaseReferenceforlikes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(LIKES_PROCESS_RUNNING){

                                if(dataSnapshot.child(postNodeValue).hasChild(currentUser)) {
                                    databaseReferenceforlikes.child(postNodeValue).child(currentUser).removeValue();
                                   setupLikesCounter(postreference,currentUser);
                                    LIKES_PROCESS_RUNNING = false;

                                }else{
                                    databaseReferenceforlikes.child(postNodeValue).child(currentUser).setValue("liked");
                                   setupLikesCounter(postreference,currentUser);
                                    LIKES_PROCESS_RUNNING = false;

                                }
                            }
                        }

                        public void setupLikesCounter(DatabaseReference postReference, final String currentUser){
                            postReference.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    NewPost newPost = mutableData.getValue(NewPost.class);
                                    if(newPost == null){
                                        return Transaction.success(mutableData);
                                    }
                                    if(newPost.stars.containsKey(currentUser)){
                                        newPost.likecount = newPost.likecount - 1;
                                        newPost.stars.remove(currentUser);
                                    }else{
                                        newPost.likecount = newPost.likecount + 1;
                                        newPost.stars.put(currentUser,true);
                                    }
                                    mutableData.setValue(newPost);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        public void toggleLikeButton(final ImageView imageView, final String user, final String postnode, DatabaseReference postRef){
            postRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postnode).hasChild(user)){
                        imageView.setImageResource(R.drawable.ic_star_golden_35dp);
                    }else{
                        imageView.setImageResource(R.drawable.ic_star_grey_35dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void toggleLikesCount(final DatabaseReference databaseReference, final TextView textView, final String postnode){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long likes = dataSnapshot.child(postnode).getChildrenCount();
                    textView.setText(Long.toString(likes));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setupTimeLineImagesWithDraweeView(final String imageuri){
            final SimpleDraweeView simpleDraweeView = (SimpleDraweeView)myview.findViewById(R.id.cardViewImagePostViewGlobalTimelineSimpleDraw);
            final ProgressBar progressBar = (ProgressBar)myview.findViewById(R.id.cardViewGlobalTimeLineProgressBar);

            if(imageuri.equals("hello")){
                if (simpleDraweeView.getVisibility() != GONE) {
                    progressBar.setVisibility(GONE);
                    simpleDraweeView.setVisibility(GONE);
                    simpleDraweeView.setLayoutParams(new RelativeLayout.LayoutParams(0,0)); }
            } else if (imageuri != null){

                progressBar.setVisibility(GONE);
                storageReference.child(imageuri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        simpleDraweeView.setImageURI(uri);
                    }

                });
            }
        }
        public void setupImageLessView(){
            final SimpleDraweeView simpleDraweeView = (SimpleDraweeView)myview.findViewById(R.id.cardViewImagePostViewGlobalTimelineSimpleDraw);
                if (simpleDraweeView.getVisibility() != GONE) {
                    simpleDraweeView.setVisibility(GONE);
                    simpleDraweeView.setLayoutParams(new RelativeLayout.LayoutParams(0,0)); }
        }
        public void setupNotifyMe(final String currentUser, final String postNodeValue,final ImageView myNotifyMeImageView,final DatabaseReference postreference) {
            myNotifyMeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NOTIFY_ME_PROCESS_RUNNING = true;
                    databaseReferenceforNotifyMe.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(NOTIFY_ME_PROCESS_RUNNING){

                                if(dataSnapshot.child(postNodeValue).hasChild(currentUser)) {
                                    databaseReferenceforNotifyMe.child(postNodeValue).child(currentUser).removeValue();
                                    setupNotifyMeCounter(postreference,currentUser);
                                    NOTIFY_ME_PROCESS_RUNNING = false;

                                }else{
                                    databaseReferenceforNotifyMe.child(postNodeValue).child(currentUser).setValue("notified");
                                    setupNotifyMeCounter(postreference,currentUser);
                                    NOTIFY_ME_PROCESS_RUNNING = false;

                                }
                            }
                        }

                        public void setupNotifyMeCounter(DatabaseReference postReference, final String currentUser){
                            postReference.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    NewPost newPost = mutableData.getValue(NewPost.class);
                                    if(newPost == null){
                                        return Transaction.success(mutableData);
                                    }
                                    if(newPost.bells.containsKey(currentUser)){
                                        newPost.notifymecount = newPost.notifymecount - 1;
                                        newPost.bells.remove(currentUser);
                                    }else{
                                        newPost.notifymecount = newPost.notifymecount + 1;
                                        newPost.bells.put(currentUser,true);
                                    }
                                    mutableData.setValue(newPost);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        public void toggleNotifyMeCount(final DatabaseReference databaseReference, final TextView textView, final String postnode){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long likes = dataSnapshot.child(postnode).getChildrenCount();
                    textView.setText(Long.toString(likes));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void toggleNotifyMeButton(final ImageView imageView, final String user, final String postnode, DatabaseReference postRef){
            postRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postnode).hasChild(user)){
                        imageView.setImageResource(R.drawable.ic_notifications_black_24dp);
                    }else{
                        imageView.setImageResource(R.drawable.ic_notifications_none_black_24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }
    @Override
    public void onStart() {
        super.onStart();
        myrecAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myrecAdapter.stopListening();

    }



    //=============================================================================================================================================================
    //                          BELOW GIVEN METHODS AND CLASSES ARE JUST FOR FUTURE REFERENCE AND SHOULD NOT BE USED
    //=============================================================================================================================================================


    /*//deprecated++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void setupRecyclerView(View view){
        //this sets up the view
        RecyclerView myHomeRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewHomeFragment);
        ListAdapterHomeFragment listAdapterHomeFragment = new ListAdapterHomeFragment();
        myHomeRecyclerView.setAdapter(listAdapterHomeFragment);
        RecyclerView.LayoutManager myHomeFragmentlayoutManager = new LinearLayoutManager(getActivity());
        myHomeRecyclerView.setLayoutManager(myHomeFragmentlayoutManager);
    }

    //deprecated+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void setupRealtimeDatabaseTimeline(View view){

        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewHomeFragment);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager myHomeLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(myHomeLayoutManager);

        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //use this for congiguration such as root>recycler_data>names
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //this is just fo future reference
                ArrayList<String> values = (ArrayList<String>) dataSnapshot.child("recycler_data").getValue();
                //ArrayList<String> values = (ArrayList<String>) dataSnapshot.getValue();
                //this is for test
               // ArrayList<String> body = (ArrayList<String>) dataSnapshot.child("body").getValue();
                //recyclerView.setAdapter(new RecyclerViewAdapter(values));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
            }
        });
    }
    //deprecated++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //private class GetDataFromFirebase extends AsyncTask<Void,Void,Boolean>{

      //  @Override
        //protected void onPreExecute() {
          //  super.onPreExecute();
        //}

        //@Override
        //protected Boolean doInBackground(Void... voids) {
        //    return false;
        //}

       // @Override
        //protected void onPostExecute(Boolean aBoolean) {
      //      super.onPostExecute(aBoolean);
        //}
    //}
*/

}

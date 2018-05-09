package com.typhon.studios.artistewe_prototype_3.Likes;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.HomeFragment;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

/**
 * Created by root on 30/11/17.
 */

public class LikesActivity extends AppCompatActivity {
    private static final String TAG = "LikesActivity";
    private Context primarycontext = LikesActivity.this;
    private static final int ACTIVITY_NUM = 1;
    private FirebaseRecyclerAdapter<NewPost, LikesActivity.NewPostViewHolder> myrecAdapter;
    private DatabaseReference databaseReferenceforNotifyMe;
    private DatabaseReference databaseReferenceforUserInformation;
    private static final String CHANNEL_ID = "NotificationButton";
    private RecyclerView myRecycleView;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Log.d(TAG, "onCreate: started onCreate method");
        setupEXBottomNavigationView();
        setupNewPostsRecyclerView();
    }


    public void setupNewPostsRecyclerView() {

        databaseReferenceforUserInformation = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers");
        myRef = FirebaseDatabase.getInstance().getReference().child("posts").child("main");
        myRef.keepSynced(true);

        databaseReferenceforNotifyMe = FirebaseDatabase.getInstance().getReference().child("posts").child("notifyme");
        databaseReferenceforNotifyMe.keepSynced(true);

        myRecycleView = (RecyclerView)findViewById(R.id.myLikesActivityRecyclerView);
        myRecycleView.hasFixedSize();

        ((SimpleItemAnimator) myRecycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        myRecycleView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("posts").child("notifyme");
        Query personsQuery = personsRef.orderByKey();
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<NewPost>().setQuery(personsQuery, NewPost.class).build();
        myrecAdapter = new FirebaseRecyclerAdapter<NewPost, LikesActivity.NewPostViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull LikesActivity.NewPostViewHolder holder, int position, @NonNull NewPost model) {

                String postNodeValue = getRef(position).getKey();
                DatabaseReference postReference = getRef(position);
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                holder.setupListOfNotifications(postNodeValue);


            }

            @NonNull
            @Override
            public LikesActivity.NewPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_likes_recycler_view_single_item, parent, false);
                return new LikesActivity.NewPostViewHolder(view);
            }
        };
        myRecycleView.setAdapter(myrecAdapter);
    }

    public class NewPostViewHolder extends RecyclerView.ViewHolder {
        View myView;
        public NewPostViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void postList(String test){
            final TextView textView = (TextView)myView.findViewById(R.id.cardViewLikesActivityTextView);
            textView.setText(test);
        }
        public void setupListOfNotifications(final String postNodeValue){
            databaseReferenceforNotifyMe.child(postNodeValue).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        String uid= snapshot.getKey();
                        final String value=snapshot.getValue().toString();
                        final TextView textView = (TextView)myView.findViewById(R.id.cardViewLikesActivityTextView);
                        databaseReferenceforUserInformation.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                                String first = userInformation.getFirstname();
                                String last = userInformation.getLastname();
                                textView.setText(first+""+last);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    /*databaseReferenceforUserInformation.child(uid.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                            final TextView textView = (TextView)myView.findViewById(R.id.cardViewLikesActivityTextView);
                            textView.setText(ass);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        myrecAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myrecAdapter.stopListening();
    }
}

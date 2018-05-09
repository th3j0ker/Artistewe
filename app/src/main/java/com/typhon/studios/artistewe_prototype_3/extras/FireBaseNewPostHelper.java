package com.typhon.studios.artistewe_prototype_3.extras;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;

import java.util.ArrayList;

public class FireBaseNewPostHelper {

    Context context;
    String database_url;
    ListView listView;
    Firebase firebase;
    ArrayList<NewPost> myNewPosts = new ArrayList<>();
    NewPostAdapter newPostAdapter;

    public FireBaseNewPostHelper(Context context, String database_url,ListView listView){
        this.context=context;
        this.database_url=database_url;
        this.listView=listView;

        Firebase.setAndroidContext(context);
        firebase=new Firebase(database_url);
    }

    public void savedata(String title,String body){
        NewPost newPost = new NewPost();
        newPost.setTitle(title);
        newPost.setBody(body);
        firebase.child("NewPost").push().setValue(newPost);
    }

    public void refreshdata(){
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getupdates(DataSnapshot dataSnapshot){
        myNewPosts.clear();

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            NewPost newPost = new NewPost();
            newPost.setTitle(ds.getValue(NewPost.class).getTitle());
            newPost.setBody(ds.getValue(NewPost.class).getBody());
            myNewPosts.add(newPost);
        }
        if(myNewPosts.size()>0){
            newPostAdapter = new NewPostAdapter(context,myNewPosts);
            listView.setAdapter((ListAdapter)newPostAdapter);
        }else{
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}

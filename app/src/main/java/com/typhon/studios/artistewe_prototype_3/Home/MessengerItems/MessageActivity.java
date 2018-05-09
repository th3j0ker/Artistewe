package com.typhon.studios.artistewe_prototype_3.Home.MessengerItems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.HomeFragment;
import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;
import com.typhon.studios.artistewe_prototype_3.Home.MainActivity;
import com.typhon.studios.artistewe_prototype_3.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class MessageActivity extends AppCompatActivity {

    private String friendFirstname,
            friendLastname,
            friendEmail,
            friendUid,
            currenUsertUid;

    private RecyclerView myRecycleView;
    private DatabaseReference myRef;
    private FirebaseRecyclerAdapter<MessageData,MessageActivity.MessageDataViewHolder> myrecAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setupViewsGetItemsFromIntent();
        sendMessage(currenUsertUid+friendUid,currenUsertUid,friendUid+currenUsertUid,friendUid);
        displayMessageOnScreen(currenUsertUid+friendUid,currenUsertUid);


    }


    private void setupViewsGetItemsFromIntent(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView myFirstname = (TextView)findViewById(R.id.messagetopbarNameTextView);
        TextView myLastname = (TextView)findViewById(R.id.messageTopBarSurnameTextView);
        if(bundle!=null){
             friendFirstname = (String)bundle.get("firstname");
             friendLastname = (String)bundle.get("lastname");
             friendEmail = (String)bundle.get("email");
             friendUid = (String)bundle.get("uid");
             currenUsertUid = FirebaseAuth.getInstance().getUid();
            myFirstname.setText(friendFirstname);
            myLastname.setText(friendLastname);
        }
    }

    private void sendMessage(final String messageID,final String uniqueID,final String friendmessageID,final String frienduniqueID){
        ImageButton myMessageSendButton = (ImageButton) findViewById(R.id.mySendMessageButton);
        final EditText myMessage = (EditText)findViewById(R.id.myMessageInputEdittext);
        myMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = myMessage.getText().toString();
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("UserMessages")
                        .child(uniqueID)
                        .child(messageID)
                        .push()
                        .setValue(new MessageData(message,currenUsertUid));
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("UserMessages")
                        .child(frienduniqueID)
                        .child(friendmessageID)
                        .push()
                        .setValue(new MessageData(message,friendUid));
                myMessage.setText("");
                myMessage.requestFocus();
            }
        });
    }

    public void checkIfUserIsSender() {
        if (false) {

        } else {

        }
    }

    String currentUserUserid;
    public void displayMessageOnScreen(String messageID,String uniqueID){

        myRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserMessages")
                .child(uniqueID)
                .child(messageID);

        myRef.keepSynced(true);

        myRecycleView = (RecyclerView)findViewById(R.id.myMessageActivityRecyclerView);
        myRecycleView.hasFixedSize();
        myRecycleView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference personsRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserMessages")
                .child(uniqueID)
                .child(messageID);
        Query personsQuery = personsRef.orderByKey();
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<MessageData>().setQuery(personsQuery,MessageData.class).build();

        myrecAdapter = new FirebaseRecyclerAdapter<MessageData, MessageActivity.MessageDataViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MessageDataViewHolder holder, int position, @NonNull MessageData model) {
                holder.setMessage(model.getMessage());
                currentUserUserid = model.getUniqueid();
                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MessageActivity.this, currentUserUserid, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public MessageActivity.MessageDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(currentUserUserid == FirebaseAuth.getInstance().getUid()){
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.snippet_message_recycler_activity_single_item_sender, parent, false);
                    return new MessageActivity.MessageDataViewHolder(view);
                }else{
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.snippet_message_recycler_activity_single_item_receiver, parent, false);
                    return new MessageActivity.MessageDataViewHolder(view);
                }
            }
        };
        myRecycleView.setAdapter(myrecAdapter);
    }

    public static class MessageDataViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MessageDataViewHolder(View itemView) {
            super(itemView);
            myview = itemView;
        }
        public void setMessage(String message){
            TextView Mymessage = (TextView)myview.findViewById(R.id.myMessageBodyTextView);
            Mymessage.setText(message);
        }
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

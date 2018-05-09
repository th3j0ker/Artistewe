package com.typhon.studios.artistewe_prototype_3.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.typhon.studios.artistewe_prototype_3.Home.FloatingActionButtonElements.BaseFragment;
import com.typhon.studios.artistewe_prototype_3.Home.MessengerItems.MessageActivity;
import com.typhon.studios.artistewe_prototype_3.Profile.ProfileActivity;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.UserRegisteration.UserInformation;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.util.RFABShape;
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30/11/17.
 */

public class MessagesFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{
    private static final String TAG = "MessagesFragment";
    Activity myActivity;
    Context myContext;
    private RecyclerView myRecycleView;
    private DatabaseReference myRef;
    private FirebaseRecyclerAdapter<UserInformation,MessagesFragment.UserInformationViewHolder> myNotificationRecyclerAdapter;

    //================================================================FloatingActionButton=====================================================================================================
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper rfabHelper;
    private String currentUserEmail;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRFAB();

    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        Toast.makeText(getActivity(), "clicked label: " + position, Toast.LENGTH_SHORT).show();
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
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Status update")
                .setResId(R.mipmap.ico_test_d)
                .setIconNormalColor(0xff6a1b9a)
                .setIconPressedColor(0xff4a148c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(currentUserEmail)
                .setResId(R.mipmap.ico_test_c)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(14)
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(context, 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Change Username")
                .setResId(R.mipmap.ico_test_b)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Mute Everyone")
                .setResId(R.mipmap.ico_test_a)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)
                .setWrapper(3)
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
        String resource = "RFAB_IDENTIFICATION_CODE_FRAGMENT_C";
        return resource;
    }

    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }
    //===================================================================================================================================================================================================


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages,container,false);
        setupRegisteredUsersRecyclerView(view);
        rfaLayout = (RapidFloatingActionLayout) view.findViewById(R.id.rfab_group_sample_fragment_c_rfal_message_fragment);
        return view;
    }
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public void setupRegisteredUsersRecyclerView(View view){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profilePics");

        myRef = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers");
        myRef.keepSynced(true);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        myRecycleView = (RecyclerView)view.findViewById(R.id.myMessagesFragmentRecyclerView);
        myRecycleView.hasFixedSize();
        myRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        DatabaseReference personalRef = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers");
        Query personalQuery = personalRef.orderByKey();

        FirebaseRecyclerOptions personalOptions =
                new FirebaseRecyclerOptions.Builder<UserInformation>()
                        .setQuery(personalQuery,UserInformation.class)
                        .build();

        myNotificationRecyclerAdapter = new FirebaseRecyclerAdapter<UserInformation, UserInformationViewHolder>(personalOptions) {
            @Override
            protected void onBindViewHolder(@NonNull UserInformationViewHolder holder, int position, @NonNull UserInformation model) {
                holder.setFirstName(model.getFirstname(),model.getLastname());
                final String mynameuser = model.getFirstname()+" "+model.getLastname();
                final String myFirstname = model.getFirstname();
                final String myLastname = model.getLastname();
                final String myEmail = model.getEmail();
                final String myUid = model.getUserid();
                final String myCurrentUid = FirebaseAuth.getInstance().getUid();
                final String imageuri = model.getProfileimage();
                final ImageView profileimageview = (ImageView)holder.myView.findViewById(R.id.myCircleImageViewMessenger);
                    if (imageuri != null) {
                        storageReference.child(imageuri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                if(myContext == null){
                                    return;
                                }

                                Glide.with(getContext())
                                        .load(uri)
                                        .into(profileimageview);

                            }
                        });
                    }


                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(),MessageActivity.class);
                        intent.putExtra("username",mynameuser);
                        intent.putExtra("firstname",myFirstname);
                        intent.putExtra("lastname",myLastname);
                        intent.putExtra("uid",myUid);
                        intent.putExtra("email",myEmail);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public UserInformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_contact_item_with_profile_icon,parent,false);
                return new MessagesFragment.UserInformationViewHolder(view);
            }
        };
        myRecycleView.setAdapter(myNotificationRecyclerAdapter);
    }


    public class UserInformationViewHolder extends RecyclerView.ViewHolder {
        View myView;
        public UserInformationViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void setFirstName(String firstName,String lastname){
            TextView myusername = (TextView)myView.findViewById(R.id.myUsernameContactTextView);
            myusername.setText(firstName+" "+lastname);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        myNotificationRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myNotificationRecyclerAdapter.stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = getActivity();
        myContext = getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
        myActivity = null;
    }
}

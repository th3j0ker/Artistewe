package com.typhon.studios.artistewe_prototype_3.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.typhon.studios.artistewe_prototype_3.Home.MainActivity;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.StartupActivity;

/**
 * Created by root on 1/12/17.
 */

public class SignOutFragment extends Fragment {
    private static final String TAG = "SignOutFragment";

    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth fAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out,container,false);
        Log.d(TAG, "onCreate: signing out the user!!!");
        //if(fAuth.getCurrentUser()==null){navigteToLogin();}

        Button mySignoutButton = (Button)view.findViewById(R.id.mySignOutFragmentButton);
        mySignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        return view;
    }

    public void signOut(){
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(), StartupActivity.class);
                        startActivity(intent);
                     getActivity().finish();
                    }
                });
    }
}

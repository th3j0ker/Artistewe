package com.typhon.studios.artistewe_prototype_3.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.typhon.studios.artistewe_prototype_3.Models.User;
import com.typhon.studios.artistewe_prototype_3.R;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 2/12/17.
 */

public class FireBaseMethods {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase myfireFirebaseDatabase;
    private DatabaseReference mydatabaseReference;

    private String userID;
    private Context myContext;

    public FireBaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        myContext = context;
        myfireFirebaseDatabase = FirebaseDatabase.getInstance();
        mydatabaseReference = myfireFirebaseDatabase.getReference();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfFirstNameExists(String firstname, DataSnapshot dataSnapshot){
        Log.d(TAG, "checkIfFirstNameExists:  checking if username"+firstname+" exists or not...");

        User user = new User();
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            Log.d(TAG, "checkIfFirstNameExists: DATA SNAP SHOT"+ds);

            //use child() if the hierarchy is different
            user.setFirst_name(ds.getValue(User.class).getFirst_name());
            Log.d(TAG, "checkIfFirstNameExists: First_Name is "+user.getFirst_name());
            
            if(user.getFirst_name().equals(firstname)){
                Log.d(TAG, "checkIfFirstNameExists: THE FIRST NAME ALREADY EXISTS, NOTHING TO WORRY ABOUT!"+user.getFirst_name());
                return true;
            }
/*

              USE THIS ONLY FOR USERNAME AND NOTHING ELSE, REFER LECTURE 25 FOR FURTHURE HELP
              
            if(StringManipulation.expandUsername(user.getUserame()).equals(username))
*/
        }
        return false;
        
    }

    public void registerNewUser(final String email,final String firstname,final String lastname,String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    //we removed "this" from context to resolve the error
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(myContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: authentication changed .."+userID);
                        }

                        // ...
                    }
                });
    }

    public void addNewUser(String email,String firstname,String discription,String website, String profile_picture){
        User user = new User(email,firstname,9878987,userID);

    }

}

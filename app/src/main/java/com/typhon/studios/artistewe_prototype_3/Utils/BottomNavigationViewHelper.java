package com.typhon.studios.artistewe_prototype_3.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.typhon.studios.artistewe_prototype_3.Likes.LikesActivity;
import com.typhon.studios.artistewe_prototype_3.Home.MainActivity;
import com.typhon.studios.artistewe_prototype_3.Profile.ProfileActivity;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Search.SearchActivity;
import com.typhon.studios.artistewe_prototype_3.Share.ShareActivity;

/**
 * Created by root on 30/11/17.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupIconSize(BottomNavigationViewEx bottomNavigationViewEx,int SIZE,Context context){

        for (int i = 0; i < bottomNavigationViewEx.getChildCount(); i++) {
            View iconView = bottomNavigationViewEx.getChildAt(i).findViewById(android.support.design.R.id.icon);
            ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SIZE, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SIZE, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom navigation view settings...");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(28,28);
    }

    //this is how we will navigate between activities
    public static void enableNavigation(final Context context, final BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_house:
                        //view.setItemBackground(0,R.color.colorYellow);
                        //context is needed to be referenced because the object class is unaware of it primarily
                        Intent houseintent = new Intent(context, MainActivity.class);//0
                        houseintent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(houseintent);
                        break;
                    case R.id.ic_circle:
                        //view.setIconSizeAt(3,35,35);
                        //view.setItemBackground(3,R.color.colorYellow);
                        Intent circleIntent = new Intent(context, ShareActivity.class);//3
                        circleIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(circleIntent);
                        break;
                    case R.id.ic_search:
                        Intent searchIntent = new Intent(context, SearchActivity.class);//4
                        searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(searchIntent);
                        break;
                    case R.id.ic_alert:
                        Intent alertIntent = new Intent(context, LikesActivity.class);//1
                        alertIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(alertIntent);
                        break;
                    case R.id.ic_android:
                        Intent androidintent = new Intent(context, ProfileActivity.class);//2
                        androidintent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(androidintent);
                        break;
                }
                return false;
            }
        });
    }
}

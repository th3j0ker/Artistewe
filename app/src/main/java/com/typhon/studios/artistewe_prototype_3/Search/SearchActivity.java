package com.typhon.studios.artistewe_prototype_3.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;

/**
 * Created by root on 30/11/17.
 */

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    private Context primarycontext = SearchActivity.this;
    private static final int ACTIVITY_NUM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started onCreate method");
        setupEXBottomNavigationView();
    }

    //setup for our bottom navigation view so that we can later use it
    private void setupEXBottomNavigationView(){
        Log.d(TAG, "setupEXBottomNavigationView: setting up bottom navigation view ....");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavigationBarView);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(primarycontext, bottomNavigationViewEx);
        //BottomNavigationViewHelper.setupIconSize(bottomNavigationViewEx,32,this);

        //after using the above code you may not be able to highlight the icons
        //we do the following in order to do so
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}

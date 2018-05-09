package com.typhon.studios.artistewe_prototype_3.Profile;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Utils.BottomNavigationViewHelper;
import com.typhon.studios.artistewe_prototype_3.Utils.SectionsStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by root on 1/12/17.
 */

public class AccountSettingsActivity extends AppCompatActivity{
    private static final String TAG = "AccountSettingsActivity";
    private Context myContext;
    private static final int ACTIVITY_NUM = 2;
    private SectionsStatePagerAdapter myPagerAdapter;
    private ViewPager myViewpager;
    private RelativeLayout myRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        myContext = AccountSettingsActivity.this;
        Log.d(TAG, "onCreate: started on create");
        myViewpager = (ViewPager)findViewById(R.id.myContainerViewPager);
        myRelativeLayout = (RelativeLayout) findViewById(R.id.relativelayout1settings);
        //setupEXBottomNavigationView();
        setupSettingsList();
        backArrowtoProfile();
        setupFragments();
    }

    private void setupFragments(){
        myPagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragmentToLists(new EditProfileFragment(),getString(R.string.edit_profile));
        myPagerAdapter.addFragmentToLists(new SignOutFragment(),getString(R.string.sign_out));
        myPagerAdapter.addFragmentToLists(new PrivacySettingsFragment(),"privacy");

    }

    private void setViewPager(int fragmentNumber){
        Log.d(TAG, "setViewPager:  setting up view pager ...");
        myRelativeLayout.setVisibility(View.GONE);
        myViewpager.setAdapter(myPagerAdapter);
        myViewpager.setCurrentItem(fragmentNumber);
        
    }

    private void setupSettingsList(){
        Log.d(TAG, "setupSettingsList: setup setings taking place");
        ListView mylistListView = (ListView) findViewById(R.id.accountSettingListView);
        ArrayList<String> mySettingOptionsList = new ArrayList<>();
        mySettingOptionsList.add(getString(R.string.edit_profile));
        mySettingOptionsList.add(getString(R.string.sign_out));
        mySettingOptionsList.add("Privacy");

        ArrayAdapter arrayAdapter = new ArrayAdapter(myContext,android.R.layout.simple_list_item_1,mySettingOptionsList);
        mylistListView.setAdapter(arrayAdapter);

        mylistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick:you clicked pager adapter setView pager is currently running");
                setViewPager(position);
            }
        });

    }
    //setup for our bottom navigation view so that we can later use it
    private void setupEXBottomNavigationView(){
        Log.d(TAG, "setupEXBottomNavigationView: setting up bottom navigation view ....");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavigationBarView);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(myContext, bottomNavigationViewEx);
        //after using the above code you may not be able to highlight the icons
        //we do the following in order to do so
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    
    private void backArrowtoProfile(){
        Log.d(TAG, "backArrowtoProfile: back arrrow method started");;
        ImageView imageView = (ImageView)findViewById(R.id.backArrowProfileSettings);
        
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked back arrow");
                finish();
            }
        });
    }
}

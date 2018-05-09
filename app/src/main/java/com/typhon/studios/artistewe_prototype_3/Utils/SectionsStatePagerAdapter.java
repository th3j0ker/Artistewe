package com.typhon.studios.artistewe_prototype_3.Utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 1/12/17.
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> myFragmentList = new ArrayList<>();
    private final HashMap<Fragment,Integer> myFragments = new HashMap<>();
    private final HashMap<String,Integer> myFragmentNumbers = new HashMap<>();
    private final HashMap<Integer,String> myFragmentNames = new HashMap<>();
    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return myFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }


    public void addFragmentToLists(Fragment fragment, String fragmentName){
        myFragmentList.add(fragment);
        myFragments.put(fragment,myFragmentList.size()-1);
        myFragmentNumbers.put(fragmentName,myFragmentList.size()-1);
        myFragmentNames.put(myFragmentList.size()-1,fragmentName);

    }

/*
*
* returns the fragent with name @param
*
* @param fragmentname
*
*
* */

    private Integer getFragmentNumber(String fragmentName){
        if(myFragmentNumbers.containsKey(fragmentName)){
            return myFragmentNumbers.get(fragmentName);
        }else{
            return null;
        }
    }


/*
*
* returns the fragent with name @param
*
* @param fragment
*
*
* */

    private Integer getFragmentNumber(Fragment fragment){
        if(myFragmentNumbers.containsKey(fragment)){
            return myFragmentNumbers.get(fragment);
        }else{
            return null;
        }
    }


    /*
*
* returns the fragent with name @param
*
* @param fragmentname
*
*
* */

    private String getFragmentName(Integer fragmentNumber){
        if(myFragmentNames.containsKey(fragmentNumber)){
            return myFragmentNames.get(fragmentNumber);
        }else{
            return null;
        }
    }



}


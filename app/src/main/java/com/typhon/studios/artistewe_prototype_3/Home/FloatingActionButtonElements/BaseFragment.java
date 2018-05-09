package com.typhon.studios.artistewe_prototype_3.Home.FloatingActionButtonElements;

import android.support.v4.app.Fragment;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;


public abstract class BaseFragment extends Fragment {

    public abstract String getRfabIdentificationCode();

    public abstract String getTitle();

    public void onInitialRFAB(RapidFloatingActionButton rfab) {
    }

}

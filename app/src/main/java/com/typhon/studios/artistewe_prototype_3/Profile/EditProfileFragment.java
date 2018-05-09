package com.typhon.studios.artistewe_prototype_3.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.typhon.studios.artistewe_prototype_3.R;
import com.typhon.studios.artistewe_prototype_3.Utils.UniversalImageLoader;

/**
 * Created by root on 1/12/17.
 */

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    private ImageView myProfilePhoto;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
        myProfilePhoto = (ImageView)view.findViewById(R.id.EditprofilepicImageView);
        //setProfileImage();
        //navigate back to our profile settings activity
        return view;
    }
    public void setProfileImage(){
        Log.d(TAG, "setProfileImage: started.....");
        String imageURL = "http://tfwiki.net/mediawiki/images2/thumb/4/43/Cloud_Optimus_Prime.jpg/180px-Cloud_Optimus_Prime.jpg";
        UniversalImageLoader.setImage(imageURL,myProfilePhoto,null,"");
    }

}

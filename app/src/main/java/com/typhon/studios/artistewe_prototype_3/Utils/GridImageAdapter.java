package com.typhon.studios.artistewe_prototype_3.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.typhon.studios.artistewe_prototype_3.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by root on 1/12/17.
 */

public class GridImageAdapter extends ArrayAdapter<String> {

    private Context myContext;
    private LayoutInflater mylayoutInflater;
    private int layoutResource;
    private String myAppend;
    private ArrayList<String> imageURLs;

    public GridImageAdapter(Context myContext,int layoutResource, String myAppend, ArrayList<String> imageURLs) {
        super(myContext,layoutResource,imageURLs);
        mylayoutInflater = (LayoutInflater)myContext.getSystemService(myContext.LAYOUT_INFLATER_SERVICE);
        this.myContext = myContext;
        this.layoutResource = layoutResource;
        this.myAppend = myAppend;
        this.imageURLs = imageURLs;
    }

    /*
    *
    * holder is used to create the widgets and doesnt let the app slow down
    *
    *
    * */
    private static class ViewHolder{
        SquareImageViewForGridView image;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*
        *
        * view holder builds widegets same way as recycler view(builds pattern
        *
        *
        * */

        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = mylayoutInflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.gridImageProgressBar);
            viewHolder.image = (SquareImageViewForGridView) convertView.findViewById(R.id.gridImageView);
            /*
            *
            *
            * setTag is used to store widgets in memory
            *
            * */
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String imageURL = getItem(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(myAppend + imageURL, viewHolder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(viewHolder.progressBar!= null){
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            }
        });
        return convertView;
    }
}

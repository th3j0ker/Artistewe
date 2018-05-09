package com.typhon.studios.artistewe_prototype_3.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.typhon.studios.artistewe_prototype_3.R;

/**
 * Created by root on 1/12/17.
 */

public class UniversalImageLoader {
    private static final int defaultImage = R.drawable.ic_android;
    private Context myContext;

    public UniversalImageLoader(Context myContext) {

        this.myContext = myContext;
    }

    /*
    *
    *
    * Below given is the image loader setup
    *
    *
    *
    * */
    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(myContext)
                .defaultDisplayImageOptions(defaultImageOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024).build();

        return configuration;
    }

    /*
    *
    * now this method will be useful for
    * selecting the image only when a user has opened it
    * rather then just taking it up from the list
    *
    *
    * also this can only be used for static imagess and cannot be used for images that are dynamic, or if they are set on list or grid view,
    * this may also not work for fragments and activities...
    *
    * */

    //the string given as append is because of the different types of file types that an image loader can take
    //example, imgURL = home/something/somefile/image/jpg and append is Append = HTTP:// , FILE:// , and so on
    public static void setImage(String imgURL, ImageView image, final ProgressBar progress, String Append){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(Append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(progress != null){
                    progress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(progress != null){
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(progress != null){
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(progress != null){
                    progress.setVisibility(View.GONE);
                }
            }
        });
    }


}

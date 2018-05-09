package com.typhon.studios.artistewe_prototype_3.extras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.NewPost;
import com.typhon.studios.artistewe_prototype_3.R;

import java.util.ArrayList;

public class NewPostAdapter extends BaseAdapter{

    Context context;
    ArrayList<NewPost> myNewPosts;
    LayoutInflater inflater;

    public NewPostAdapter(Context context, ArrayList<NewPost> myNewPosts) {
        this.context = context;
        this.myNewPosts = myNewPosts;
    }

    @Override
    public int getCount() {
        return myNewPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return myNewPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null){
            inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }if (view == null){
            view = inflater.inflate(R.layout.fragment_home_single_item,viewGroup,false);
        }

        NewPostHolder newPostHolder = new NewPostHolder(view);
        newPostHolder.title.setText(myNewPosts.get(i).getTitle());
        newPostHolder.body.setText(myNewPosts.get(i).getBody());
        return view;
    }
}

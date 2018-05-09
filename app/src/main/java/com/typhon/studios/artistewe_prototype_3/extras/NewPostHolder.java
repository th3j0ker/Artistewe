package com.typhon.studios.artistewe_prototype_3.extras;

import android.view.View;
import android.widget.TextView;

import com.typhon.studios.artistewe_prototype_3.R;

public class NewPostHolder {

    TextView title,body;

    public NewPostHolder(View view){
        title = (TextView)view.findViewById(R.id.cardNameTextview);
        body = (TextView)view.findViewById(R.id.cardBodyTextview);
    }
}

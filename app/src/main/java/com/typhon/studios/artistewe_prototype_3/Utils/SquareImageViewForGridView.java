package com.typhon.studios.artistewe_prototype_3.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by root on 1/12/17.
 */

public class SquareImageViewForGridView extends AppCompatImageView {




    /*
    *
    *
    *
    *
    * this is used to square the uneven images in our gridview inside the ProfileActivity
    * @param onmeasure
    *
    *
    * */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public SquareImageViewForGridView(Context context) {
        super(context);
    }

    public SquareImageViewForGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageViewForGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

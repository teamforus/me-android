package io.forus.me.android.presentation.helpers;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageLoader {


    /**
     * ImageLoader Constructor
     *
     * @param context application Context
     */
    private ImageLoader(Context context) {

    }





    public static void load(Context context, String url, final ImageView imageView) {

        if (imageView == null || url == null)
            return;


        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }





}
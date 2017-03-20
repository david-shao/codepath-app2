package com.david.nytimessearch.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by David on 3/19/2017.
 */

public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
//        Picasso.with(view.getContext()).load(url).into(view);
        Glide.with(view.getContext()).load(url).into(view);
    }

}
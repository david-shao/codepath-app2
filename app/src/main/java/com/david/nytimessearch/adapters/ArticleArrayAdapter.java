package com.david.nytimessearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.nytimessearch.R;
import com.david.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by David on 3/16/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    Context context;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article_result, articles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get data item for position
        Article article = getItem(position);

        //check to see if existing view is being reused
        //if not using recycled view -> inflate layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        //find image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        //clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadLine());

        //populate thumbnail image
        //remote download the image in background
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(context).load(thumbnail)
                    .into(imageView);
        }

        return convertView;
    }
}

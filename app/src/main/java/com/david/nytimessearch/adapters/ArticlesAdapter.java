package com.david.nytimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.nytimessearch.R;
import com.david.nytimessearch.activities.ArticleActivity;
import com.david.nytimessearch.models.Article;
import com.david.nytimessearch.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by David on 3/19/2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Target {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public DynamicHeightImageView ivImage;
        public TextView tvTitle;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivImage = (DynamicHeightImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            //do nothing?
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Calculate the image ratio of the loaded bitmap
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
//            Log.d("DEBUG", "setting height ratio: " + ratio);
            // Set the ratio for the image
            ivImage.setHeightRatio(ratio);
            // Load the image into the view
            ivImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("DEBUG", "Bitmap failed to load.");
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Article article = articles.get(position);
                //create intent to display article
                Intent i = new Intent(context, ArticleActivity.class);
                //pass in article into intent
                i.putExtra("article", article);
                //launch activity
                context.startActivity(i);
            }
        }
    }

    // Store a member variable for the articles
    private List<Article> articles;
    // Store the context for easy access
    private Context context;

    // Pass in the article array into the constructor
    public ArticlesAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return this.context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_staggered_article, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticlesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = this.articles.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadLine());

        DynamicHeightImageView imageView = viewHolder.ivImage;
        imageView.setImageResource(0);

        String thumbnail = article.getThumbNail();
//        Log.d("DEBUG", "thumbnail url: " + thumbnail);
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(context).load(thumbnail)
                    .into(viewHolder);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return this.articles.size();
    }
}

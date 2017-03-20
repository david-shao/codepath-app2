package com.david.nytimessearch.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.david.nytimessearch.R;
import com.david.nytimessearch.databinding.ActivityArticleBinding;
import com.david.nytimessearch.models.Article;

/**
 * Created by David on 3/16/2017.
 */

public class ArticleActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ShareActionProvider shareAction;
    private Intent shareIntent;
    WebView webView;

    ActivityArticleBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        Article article = getIntent().getParcelableExtra("article");
        String url = article.getWebUrl();
        webView = binding.wvArticle;

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    public void prepareShareIntent() {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
    }

    // Attaches the share intent to the share menu item provider
    public void attachShareIntentAction() {
        if (shareAction != null && shareIntent != null)
            shareAction.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        prepareShareIntent();
        attachShareIntentAction();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return true;
    }
}

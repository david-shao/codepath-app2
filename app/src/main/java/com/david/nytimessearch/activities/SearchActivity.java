package com.david.nytimessearch.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.david.nytimessearch.R;
import com.david.nytimessearch.adapters.ArticlesAdapter;
import com.david.nytimessearch.databinding.ActivitySearchBinding;
import com.david.nytimessearch.fragments.SettingsFragment;
import com.david.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.david.nytimessearch.models.Article;
import com.david.nytimessearch.models.Settings;
import com.david.nytimessearch.net.ArticleClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements SettingsFragment.SettingsDialogListener {

    Toolbar toolbar;
//    EditText etQuery;
//    GridView gvResults;
//    Button btnSearch;
    SearchView svArticles;
    RecyclerView rvArticles;

    List<Article> articles;
//    ArticleArrayAdapter adapter;
    ArticlesAdapter adapter;
    EndlessRecyclerViewScrollListener scrollListener;

    Settings settings;
    ArticleClient client;

    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setupViews();

        settings = new Settings();
        client = new ArticleClient(getApplicationContext());
    }

    private void setupViews() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

//        etQuery = (EditText) findViewById(etQuery);
//        gvResults = (GridView) findViewById(gvResults);
//        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
//        adapter = new ArticleArrayAdapter(this, articles);
//        gvResults.setAdapter(adapter);
//
//        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                //create intent to display article
//                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
//                //get article to display
//                Article article = articles.get(position);
//                //pass in article into intent
//                i.putExtra("article", article);
//                //launch activity
//                startActivity(i);
//            }
//        });
//
//        gvResults.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                //max 100 pages
//                if (page >= 100) {
//                    return false;
//                }
////                String query = etQuery.getText().toString();
//                String query = svArticles.getQuery().toString();
//                Log.d("DEBUG", "scrolling to page " + (page - 1));
//                fetchArticles(query, page - 1, false);
//                return true;
//            }
//        });

        rvArticles = binding.rvArticles;
        adapter = new ArticlesAdapter(this, articles);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);
        // Set layout manager to position the items
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //max 100 pages
                if (page >= 100) {
                    return;
                }
                String query = svArticles.getQuery().toString();
                Log.d("DEBUG", "scrolling to page " + (page));
                fetchArticles(query, page, false);
            }
        };
        rvArticles.addOnScrollListener(scrollListener);
    }

    public void onArticleSearch(View view) {
//        String query = etQuery.getText().toString();
//        Toast.makeText(this, "Search for " + query, Toast.LENGTH_LONG).show();

//        fetchArticles(query, 0, true);
    }

    private void fetchArticles(String query, int page, boolean clear) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        if (clear) {
//            adapter.clear();
            articles.clear();
            adapter.notifyDataSetChanged();
            scrollListener.resetState();
        }
        client.getArticles(query, settings, page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    int curSize = adapter.getItemCount();
                    List<Article> newItems = Article.fromJSONArray(articleJsonResults);
                    articles.addAll(newItems);
                    adapter.notifyItemRangeInserted(curSize, newItems.size());
                    //completed this api call, so remove from queue
                    client.completeTransaction();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Toast.makeText(getApplicationContext(), "Network error: " + errorResponse.toString(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", "fetch articles failed with code: " + statusCode);
                //retry the api call in case of rate limit failure
                if (statusCode == 429) {
                    client.retryTransaction();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search_articles);
        svArticles = (SearchView) MenuItemCompat.getActionView(searchItem);

        svArticles.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                fetchArticles(query, 0, true);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                svArticles.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.settings:
                FragmentManager fm = getSupportFragmentManager();
                SettingsFragment settingsFragment = SettingsFragment.newInstance(settings);
                settingsFragment.show(fm, "fragment_settings");
                break;
        }

        return true;
    }

    @Override
    public void onSave(Settings settings) {
        this.settings = settings;
//        String query = etQuery.getText().toString();
        String query = svArticles.getQuery().toString();
        fetchArticles(query, 0, true);
    }
}

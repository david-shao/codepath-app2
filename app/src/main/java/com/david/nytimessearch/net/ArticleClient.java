package com.david.nytimessearch.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.david.nytimessearch.models.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by David on 3/17/2017.
 */

public class ArticleClient {

    private class Transaction {
        String query;
        Settings settings;
        int page;
        JsonHttpResponseHandler handler;
        public Transaction(String query, Settings settings, int page, JsonHttpResponseHandler handler) {
            this.query = query;
            this.settings = settings;
            this.page = page;
            this.handler = handler;
        }
    }

    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private AsyncHttpClient client;
    private Context context;

    //queue to store our api calls
    Queue<Transaction> queue;
    Timer timer;

    public ArticleClient(Context context) {
        this.context = context;
        this.client = new AsyncHttpClient();
        this.queue = new LinkedList<>();
        this.timer = new Timer();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("DEBUG", "network available returning: " + (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()));
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            Log.d("DEBUG", "exit value: " + exitValue);
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        Log.d("DEBUG", "not online");
        return false;
    }

    // Method for accessing the search API
    public void getArticles(final String query, Settings settings, int page, JsonHttpResponseHandler handler) {
        Transaction t = new Transaction(query, settings, page, handler);
        queue.add(t);
        tryTransaction(t);
    }

    //private helper method to perform the api call
    private void tryTransaction(Transaction t) {
        if (!isNetworkAvailable()) {
            Log.d("DEBUG", "no network will retry later");
            //if no connection, retry later
            retryTransaction();
            return;
        }
        final String query = t.query;
        Settings settings = t.settings;
        int page = t.page;
        JsonHttpResponseHandler handler = t.handler;
        Log.d("DEBUG", "fetching page " + (page));
        try {
            String url = getApiUrl("articlesearch.json");
            RequestParams params = new RequestParams();
            params.put("api-key", "3f176423a9bc4487b2eeb557adba0d5e");
            params.put("page", page);
            params.put("q", query);
            //apply settings
            if (settings.getSort() == 1) {
                params.put("sort", "oldest");
            }
            if (settings.getBeginDate() != null) {
                params.put("begin_date", new SimpleDateFormat("yyyyMMdd").format(settings.getBeginDate()));
            }
            if (settings.artsFilter() || settings.fashionFilter() || settings.sportsFilter()) {
                StringBuilder sb = new StringBuilder();
                sb.append("news_desk:(");
                if (settings.artsFilter()) {
                    sb.append("\"Arts\" ");
                }
                if (settings.fashionFilter()) {
                    sb.append("\"Fashion & Style\" ");
                }
                if (settings.sportsFilter()) {
                    sb.append("\"Sports\" ");
                }
                sb.append(")");
                params.put("fq", sb.toString());
            }

            client.get(url, params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //called after a successful api call to remove transaction from queue
    public void completeTransaction() {
        queue.remove();
    }

    //called after a failed api call to retry a transaction
    public void retryTransaction() {
        if (!queue.isEmpty()) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!queue.isEmpty()) {
                        tryTransaction(queue.peek());
                    }
                }
            }, 1000);
        }
    }
}

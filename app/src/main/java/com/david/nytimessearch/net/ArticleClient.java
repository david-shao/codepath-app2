package com.david.nytimessearch.net;

import com.david.nytimessearch.models.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;

/**
 * Created by David on 3/17/2017.
 */

public class ArticleClient {
    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private AsyncHttpClient client;

    public ArticleClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getArticles(final String query, Settings settings, int page, JsonHttpResponseHandler handler) {
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
}

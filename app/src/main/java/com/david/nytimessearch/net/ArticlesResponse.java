package com.david.nytimessearch.net;

import com.david.nytimessearch.models.Article;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/22/2017.
 */

public class ArticlesResponse {

    ArticlesNestedResponse response;

    public static ArticlesResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        ArticlesResponse articlesResponse = gson.fromJson(response, ArticlesResponse.class);
        return articlesResponse;
    }

    public List<Article> getArticles() {
        return response.articles;
    }

    public class ArticlesNestedResponse {
        @SerializedName("docs")
        public List<Article> articles;

        public ArticlesNestedResponse() {
            articles = new ArrayList<>();
        }
    }
}

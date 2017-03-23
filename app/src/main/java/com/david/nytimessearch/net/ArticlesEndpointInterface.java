package com.david.nytimessearch.net;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by David on 3/22/2017.
 */

public interface ArticlesEndpointInterface {
    @GET("articlesearch.json")
    Call<ArticlesResponse> getArticles(@QueryMap Map<String, String> options);
}

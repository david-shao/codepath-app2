package com.david.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/16/2017.
 */

public class Article implements Parcelable {

    @SerializedName("web_url")
    String webUrl;

    @SerializedName("headline")
    HeadLine headLine;

    @SerializedName("multimedia")
    List<Multimedia> media;

    String thumbNail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine.headLine;
    }

    public String getThumbNail() {
        if (TextUtils.isEmpty(thumbNail)) {
            if (media.size() > 0) {
                //use first image as default
                this.thumbNail = "http://www.nytimes.com/" + media.get(0).url;
                //try to find the xlarge image if it exists
                for (int i = 1; i < media.size(); i++) {
                    if (media.get(i).subType.equals("xlarge")) {
                        this.thumbNail = "http://www.nytimes.com/" + media.get(i).url;
                    }
                }
            } else {
                this.thumbNail = "";
            }
        }
        return thumbNail;
    }

//    public Article(JSONObject jsonObject) {
//        try {
//            this.webUrl = jsonObject.getString("web_url");
//            this.headLine = jsonObject.getJSONObject("headline").getString("main");
//
//            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
//            if (multimedia.length() > 0) {
//                //use first image as default
//                JSONObject multimediaJson = multimedia.getJSONObject(0);
//                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
//                //try to find the xlarge image if it exists
//                for (int i = 1; i < multimedia.length(); i++) {
//                    multimediaJson = multimedia.getJSONObject(i);
//                    if (multimediaJson.getString("subtype").equals("xlarge")) {
//                        this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
//                    }
//                }
//            } else {
//                this.thumbNail = "";
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    public static List<Article> fromJSONArray(JSONArray array) {
//        List<Article> results = new ArrayList<>();
//
//        for (int i = 0; i < array.length(); i++) {
//            try {
//                results.add(new Article(array.getJSONObject(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return results;
//    }

    private Article(Parcel in) {
        this.webUrl = in.readString();
        this.headLine = in.readParcelable(HeadLine.class.getClassLoader());
        this.media = new ArrayList<>();
        in.readTypedList(this.media, Multimedia.CREATOR);
        this.thumbNail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.webUrl);
        parcel.writeParcelable(this.headLine, i);
        parcel.writeTypedList(this.media);
        parcel.writeString(this.thumbNail);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel parcel) {
            return new Article(parcel);
        }

        @Override
        public Article[] newArray(int i) {
            return new Article[i];
        }
    };
}

package com.david.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by David on 3/22/2017.
 */

public class Multimedia implements Parcelable {
    public String url;
    @SerializedName("subtype")
    public String subType;

    private Multimedia(Parcel in) {
        this.url = in.readString();
        this.subType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.subType);
    }

    public static final Parcelable.Creator<Multimedia> CREATOR = new Parcelable.Creator<Multimedia>() {
        @Override
        public Multimedia createFromParcel(Parcel parcel) {
            return new Multimedia(parcel);
        }

        @Override
        public Multimedia[] newArray(int i) {
            return new Multimedia[i];
        }
    };
}
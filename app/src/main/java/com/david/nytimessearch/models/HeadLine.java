package com.david.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by David on 3/22/2017.
 */

public class HeadLine implements Parcelable {
    @SerializedName("main")
    public String headLine;

    private HeadLine(Parcel in) {
        this.headLine = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.headLine);
    }

    public static final Parcelable.Creator<HeadLine> CREATOR = new Parcelable.Creator<HeadLine>() {
        @Override
        public HeadLine createFromParcel(Parcel parcel) {
            return new HeadLine(parcel);
        }

        @Override
        public HeadLine[] newArray(int i) {
            return new HeadLine[i];
        }
    };
}

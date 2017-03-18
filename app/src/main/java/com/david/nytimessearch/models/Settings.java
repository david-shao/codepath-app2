package com.david.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by David on 3/17/2017.
 */

public class Settings implements Parcelable {

    Date beginDate;
    int sort;
    boolean artsFilter;
    boolean fashionFilter;
    boolean sportsFilter;

    public Date getBeginDate() {
        return beginDate;
    }

    public int getSort() {
        return sort;
    }

    public boolean artsFilter() {
        return artsFilter;
    }

    public boolean fashionFilter() {
        return fashionFilter;
    }

    public boolean sportsFilter() {
        return sportsFilter;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setArtsFilter(boolean artsFilter) {
        this.artsFilter = artsFilter;
    }

    public void setFashionFilter(boolean fashionFilter) {
        this.fashionFilter = fashionFilter;
    }

    public void setSportsFilter(boolean sportsFilter) {
        this.sportsFilter = sportsFilter;
    }

    public Settings() {
        beginDate = null;
        sort = 0;
        artsFilter = false;
        fashionFilter = false;
        sportsFilter = false;
    }

    private Settings(Parcel in) {
        this.beginDate = new Date(in.readLong());
        this.sort = in.readInt();
        this.artsFilter = in.readByte() != 0;
        this.fashionFilter = in.readByte() != 0;
        this.sportsFilter = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.beginDate.getTime());
        parcel.writeByte((byte) (this.artsFilter ? 1 : 0));
        parcel.writeByte((byte) (this.fashionFilter ? 1 : 0));
        parcel.writeByte((byte) (this.sportsFilter ? 1 : 0));
    }

    public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel parcel) {
            return new Settings(parcel);
        }

        @Override
        public Settings[] newArray(int i) {
            return new Settings[i];
        }
    };
}

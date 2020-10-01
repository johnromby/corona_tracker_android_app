package com.johnromby_au518762.coronatrackerapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {
    public String countryName;
    public String countryCode;
    public int flagImgResId;
    public int numInfected;
    public int numDeath;
    public double userRating;
    public String userNote;

    public Country(String countryName, String countryCode, int flagImgResId, int numInfected, int numDeath, double userRating, String userNote) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.flagImgResId = flagImgResId;
        this.numInfected = numInfected;
        this.numDeath = numDeath;
        this.userRating = userRating;
        this.userNote = userNote;
    }

    protected Country(Parcel in) {
        countryName = in.readString();
        countryCode = in.readString();
        flagImgResId = in.readInt();
        numInfected = in.readInt();
        numDeath = in.readInt();
        userRating = in.readDouble();
        userNote = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    // Not using these atm, but should probably do that.
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getFlagImgResId() {
        return flagImgResId;
    }

    public void setFlagImgResId(int flagImgResId) {
        this.flagImgResId = flagImgResId;
    }

    public int getNumInfected() {
        return numInfected;
    }

    public void setNumInfected(int numInfected) {
        this.numInfected = numInfected;
    }

    public int getNumDeath() {
        return numDeath;
    }

    public void setNumDeath(int numDeath) {
        this.numDeath = numDeath;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", flagImgResId=" + flagImgResId +
                ", numInfected=" + numInfected +
                ", numDeath=" + numDeath +
                ", userRating=" + userRating +
                ", userNote='" + userNote + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryName);
        dest.writeString(countryCode);
        dest.writeInt(flagImgResId);
        dest.writeInt(numInfected);
        dest.writeInt(numDeath);
        dest.writeDouble(userRating);
        dest.writeString(userNote);
    }
}
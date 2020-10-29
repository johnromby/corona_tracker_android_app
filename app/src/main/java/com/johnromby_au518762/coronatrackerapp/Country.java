package com.johnromby_au518762.coronatrackerapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@Entity(tableName = "country_table")
public class Country {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String countryName;
    private String countryCode;
    private int numInfected;
    private int numDeath;
    private double userRating;
    private String userNote;

    public Country(String countryName, String countryCode, int numInfected, int numDeath, double userRating, String userNote) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.numInfected = numInfected;
        this.numDeath = numDeath;
        this.userRating = userRating;
        this.userNote = userNote;
    }

    // GETTERS & SETTERS:
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

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

    public int getNumInfected() {
        return numInfected;
    }
    public String getNumInfectedAsString() {
        return DecimalFormatter(numInfected);
    }
    public void setNumInfected(int numInfected) {
        this.numInfected = numInfected;
    }

    public int getNumDeath() {
        return numDeath;
    }
    public String getNumDeathAsString() {
        return DecimalFormatter(numDeath);
    }
    public void setNumDeath(int numDeath) {
        this.numDeath = numDeath;
    }

    public double getUserRating() {
        return userRating;
    }
    public String getUserRatingAsString() {
        return Double.toString(userRating);
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

    public String getCasesSlashDeaths() {
        return getNumInfectedAsString() + " / " + getNumDeathAsString();
    }

    // https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
    private static String DecimalFormatter(int value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("###,###");
        return df.format(value);
    }
}

package com.johnromby_au518762.coronatrackerapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Covid19ApiSummery {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Global")
    @Expose
    private Global global;
    @SerializedName("Countries")
    @Expose
    private List<CountryLive> countries = null;
    @SerializedName("Date")
    @Expose
    private String date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public List<CountryLive> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryLive> countries) {
        this.countries = countries;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

// Credit goes to Coding in Flow: https://codinginflow.com
// Also inspired by (E20) ITSMAP "L7 - Fragments and Advanced UI" Tracker demo app.
// Lastly Async processing with Executor was inspired by (E20) ITSMAP "L4 - Persistence" room demo app.

package com.johnromby_au518762.coronatrackerapp.model;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.johnromby_au518762.coronatrackerapp.App;
import com.johnromby_au518762.coronatrackerapp.database.CountryDao;
import com.johnromby_au518762.coronatrackerapp.database.CountryDatabase;
import com.johnromby_au518762.coronatrackerapp.model.gson.CountryLive;
import com.johnromby_au518762.coronatrackerapp.model.gson.Covid19ApiSummery;

public class CountryRepository {
    // For debugging
    private static final String TAG = "CountryRepository";
    // Singleton
    private static CountryRepository instance;

    // This is to replace AsyncTask, which is deprecated
    private static ExecutorService executor;

    private static CountryDao countryDao;
    private static LiveData<List<Country>> allCountries;
    private static ArrayList<CountryLive> countriesLive;
    private static RequestQueue requestQueue;

    private Country currentCountry;

    public CountryRepository() {
    }

    // Get singleton repository
    public static CountryRepository getInstance() {
        if (instance == null) {
            instance = new CountryRepository();
            Log.d(TAG, "getInstance: Singleton CountryRepository created successfully.");

            executor = Executors.newSingleThreadExecutor();

            CountryDatabase database = CountryDatabase.getInstance(App.getAppContext());
            countryDao = database.countryDao();
            allCountries = countryDao.getAllCountries();

            requestQueue = Volley.newRequestQueue(App.getAppContext());
            countriesLive = new ArrayList<>();
        }
        return instance;
    }

    // region Simple Getters and Setters
    public Country getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(int index) {
        this.currentCountry = allCountries.getValue().get(index);
    }
    // endregion

    //region Volley
    // TODO (Improvement): Should probably move all the Volley stuff to its own class.
    //  OBS. Had some strange issues moving this to its own class, so it stays here for now!
    // Inspiration: https://www.youtube.com/watch?v=bRvLg27EWp0&list=PLrnPJCHvNZuBCiCxN8JPFI57Zhr5SusRL&index=4
    // And by E20-ITSMAP L6 Demo video: "Rick and Morty Gallery with Volley and Glide"
    public void sendRequestForDailySummery(String countryName) {
        String url = "https://api.covid19api.com/summary";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        parseJson(response);
                        if (!countryName.isEmpty() || countryName == null) {
                            CountryLive country = containsCountry(countryName);
                            if (country != null) createCountry(country);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "That did not work!", error);
            }
        });

        requestQueue.add(stringRequest);
    }

    private void createCountry(CountryLive country) {
        // Checking for duplicate entry
        List<Country> tmpList = getAllCountries().getValue();
        for (int i = 0; i < tmpList.size(); i++) {
            if (tmpList.get(i).getCountryCode().equals(country.getCountryCode())) return;
        }

        Country newCountry = new Country(
                country.getCountry(),
                country.getCountryCode(),
                country.getTotalConfirmed(),
                country.getTotalDeaths(),
                0.0, "");
        insert(newCountry);
    }

    private CountryLive containsCountry(String countryName) {
        for (CountryLive currentCountry : countriesLive) {
            if (currentCountry.getCountry().toLowerCase().equals(countryName.toLowerCase()) ||
                    currentCountry.getCountryCode().toLowerCase().equals(countryName.toLowerCase())) {
                return currentCountry;
            }
        }
        return null;
    }

    private void parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        Covid19ApiSummery dailySummery = gson.fromJson(json, Covid19ApiSummery.class);
        if (dailySummery != null) {
            countriesLive.addAll(dailySummery.getCountries());
            updateAllCountries(dailySummery.getCountries());
        }
    }
    //endregion

    // region Repository API methods
    // This is the abstraction layer between the ViewModel and the Room Database.
    public void insert(Country country) {
        insertCountryAsync(country);
    }

    public void update(Country country) {
        updateCountryAsync(country);
    }

    public void delete(Country country) {
        deleteCountryAsync(country);
    }

    public void deleteAllCountries() {
        deleteAllCountriesAsync();
    }

    public Country getSingleCountry(int id) {
        return getSingleCountryAsync(id);
    }

    public Country getSingleRandomCountry() {
        return getSingleRandomCountryAsync();
    }

    public LiveData<List<Country>> getAllCountries() {
        return allCountries;
    }

    // region Async methods
    private void insertCountryAsync(Country country){
        executor.execute(() -> countryDao.insert(country));
    }

    private void updateCountryAsync(Country country){
        executor.execute(() -> countryDao.update(country));
    }

    // Used directly in Volley
    private void updateAllCountries(List<CountryLive> countriesLive) {
        for (Country cc: allCountries.getValue()) {
            for (CountryLive live: countriesLive) {
                if (live.getCountryCode().equals(cc.getCountryCode())) {
                    Country country = new Country(
                            live.getCountry(),
                            live.getCountryCode(),
                            live.getTotalConfirmed(),
                            live.getTotalDeaths(),
                            cc.getUserRating(),
                            cc.getUserNote()
                    );
                    country.setId(cc.getId());
                    update(country);
                    Log.d(TAG, "updateAllCountries: Current country with country code " + cc.getCountryCode() + " just got updated.");
                }
            }
        }
    }

    private void deleteCountryAsync(Country country){
        executor.execute(() -> countryDao.delete(country));
    }

    private void deleteAllCountriesAsync() {
        executor.submit(() -> {
            countryDao.deleteAllCountries();
            return null;
        });
    }

    private Country getSingleCountryAsync(int id) {
        Future<Country> country = executor.submit(() -> countryDao.findCountry(id));

        try {
            return country.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Country getSingleRandomCountryAsync() {
        Future<Country> country = executor.submit(() -> countryDao.getSingleRandomCountry());

        try {
            return country.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
    // endregion
    // endregion
}
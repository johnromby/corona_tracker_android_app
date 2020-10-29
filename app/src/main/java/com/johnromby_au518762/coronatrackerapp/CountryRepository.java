// Overall credit goes to Coding in Flow: https://codinginflow.com

package com.johnromby_au518762.coronatrackerapp;

import android.app.Application;
import android.os.AsyncTask;
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

import com.johnromby_au518762.coronatrackerapp.model.CountryLive;
import com.johnromby_au518762.coronatrackerapp.model.Covid19ApiSummery;

public class CountryRepository {
    // For debugging
    private static final String TAG = "CountryRepository";

    private CountryDao countryDao;
    private RequestQueue requestQueue;
    private LiveData<List<Country>> allCountries;
    private ArrayList<CountryLive> countriesLive;

    public CountryRepository(Application application) {
        CountryDatabase database = CountryDatabase.getInstance(application);
        countryDao = database.countryDao();
        allCountries = countryDao.getAllCountries();

        requestQueue = Volley.newRequestQueue(application);
        countriesLive = new ArrayList<>();
    }

    // TODO: Should probably move all the Volley stuff to it's own class.
    // Inspiration: https://www.youtube.com/watch?v=bRvLg27EWp0&list=PLrnPJCHvNZuBCiCxN8JPFI57Zhr5SusRL&index=4
    // And by E20-ITSMAP L6 Demo video: "Rick and Morty Gallery with Volley and Glide"
    public void sendRequest(String countryName) {
        String url = "https://api.covid19api.com/summary";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        parseJson(response);
                        CountryLive country = containsCountry(countryName);
                        if (country != null) createCountry(country);
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
        for (CountryLive currentCountry: countriesLive) {
            if (currentCountry.getCountry().toLowerCase().equals(countryName.toLowerCase()) ||
                currentCountry.getCountryCode().toLowerCase().equals(countryName.toLowerCase())){
                return currentCountry;
            }
        }
        return null;
    }

    private void parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        Covid19ApiSummery dailySummery =  gson.fromJson(json, Covid19ApiSummery.class);
        if(dailySummery != null) {
            countriesLive.addAll(dailySummery.getCountries());
        }
    }

    //region Repository API methods. This is the abstraction layer between the ViewModel and the Room Database.
    public void insert(Country country) {
        new InsertCountryAsyncTask(countryDao).execute(country);
    }

    public void update(Country country) {
        new UpdateCountryAsyncTask(countryDao).execute(country);
    }

    public void delete(Country country) {
        new DeleteCountryAsyncTask(countryDao).execute(country);
    }

    public void deleteAllCountries() {
        new DeleteAllCountriesAsyncTask(countryDao).execute();
    }

    public LiveData<List<Country>> getAllCountries() {
        return allCountries;
    }

    // TODO: Should probably upgrade to use Executor instead, AsyncTask is deprecated!
    private static class InsertCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;

        private InsertCountryAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.insert(countries[0]);
            return null;
        }
    }

    private static class UpdateCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;

        private UpdateCountryAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.update(countries[0]);
            return null;
        }
    }

    private static class DeleteCountryAsyncTask extends AsyncTask<Country, Void, Void> {
        private CountryDao countryDao;

        private DeleteCountryAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.delete(countries[0]);
            return null;
        }
    }

    private static class DeleteAllCountriesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CountryDao countryDao;

        private DeleteAllCountriesAsyncTask(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            countryDao.deleteAllCountries();
            return null;
        }
    }
    //endregion
}

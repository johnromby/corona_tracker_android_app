// Credit: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-4-repository

package com.johnromby_au518762.coronatrackerapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CountryRepository {
    private CountryDao countryDao;
    private LiveData<List<Country>> allCountries;

    public CountryRepository(Application application) {
        CountryDatabase database = CountryDatabase.getInstance(application);
        countryDao = database.countryDao();
        allCountries = countryDao.getAllCountries();
    }

    //region Repository API methods. This is the abstraction layer between the ViewModel and the Room Database.
    public void insert (Country country) {
        new InsertCountryAsyncTask(countryDao).execute(country);
    }

    public void update (Country country) {
        new UpdateCountryAsyncTask(countryDao).execute(country);
    }

    public void delete (Country country) {
        new DeleteCountryAsyncTask(countryDao).execute(country);
    }

    public void deleteAllCountries () {
        new DeleteAllCountriesAsyncTask(countryDao).execute();
    }

    public LiveData<List<Country>> getAllCountries() {
        return allCountries;
    }
    //endregion

    // TODO: Upgrade to use Executor instead, AsyncTask is deprecated!
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
}

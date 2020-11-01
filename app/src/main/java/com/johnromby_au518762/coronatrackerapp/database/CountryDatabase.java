// Credit: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-4-repository

package com.johnromby_au518762.coronatrackerapp.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.johnromby_au518762.coronatrackerapp.utility.CsvUtil;
import com.johnromby_au518762.coronatrackerapp.R;
import com.johnromby_au518762.coronatrackerapp.model.Country;

import java.util.List;

@Database(entities = {Country.class}, version = 1)
public abstract class CountryDatabase extends RoomDatabase {
    // Debug Tag
    private static final String TAG = "CountryDatabase";
    // Singleton
    private static CountryDatabase instance;
    // Used to access our Dao (Mandatory getter for DAO)
    public abstract CountryDao countryDao();
    // List to hold countries from csv file
    private static List<Country> countries;

    // Get singleton database
    // Note: Synchronized means only one thread can access this at a time, which prevents multiple instances of the database
    public static synchronized CountryDatabase getInstance(Context context) {
        if (instance == null) {
            CsvUtil csvUtil = new CsvUtil(context, R.raw.corona_stats);
            countries = csvUtil.GetCountriesFromCsvFile();
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CountryDatabase.class, "country_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // TODO (Improvement): Upgrade to use Executor instead, AsyncTask is deprecated!
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CountryDao countryDao;

        public PopulateDbAsyncTask(CountryDatabase db) {
            countryDao = db.countryDao(); // This is only possible because onCreate is called after the database is created.
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Inserting the list of Country objects for a fresh database.
            for (Country country : countries) {
                countryDao.insert(country);
                Log.d(TAG, "Current country added to the database: " + country.getCountryName());
            }
            return null;
        }
    }
}

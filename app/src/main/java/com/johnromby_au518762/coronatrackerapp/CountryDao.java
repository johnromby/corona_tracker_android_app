// Credit: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-3-dao-roomdatabase

package com.johnromby_au518762.coronatrackerapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CountryDao {

    @Insert
    void insert(Country country);

    @Update
    void update(Country country);

    @Delete
    void delete(Country country);

    @Query("DELETE FROM country_table")
    void deleteAllCountries();

    // Inspired by: https://stackoverflow.com/questions/28249169/get-random-record-in-sqlite
    @Query("SELECT * FROM country_table ORDER BY RANDOM() LIMIT 1")
    Country getSingleRandomCountry();

    @Query("SELECT * FROM country_table ORDER BY countryName ASC ")
    LiveData<List<Country>> getAllCountries();
}

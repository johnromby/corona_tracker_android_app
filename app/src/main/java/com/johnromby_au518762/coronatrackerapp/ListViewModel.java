package com.johnromby_au518762.coronatrackerapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private CountryRepository repository;
    private LiveData<List<Country>> allCountries;

    public ListViewModel(@NonNull Application application) {
        super(application);
        repository = new CountryRepository(application);
        allCountries = repository.getAllCountries();
    }

    public void insert(String countryName) {
        repository.sendRequest(countryName);
    }

    public LiveData<List<Country>> getAllCountries() {
        return allCountries;
    }
}
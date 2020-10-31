package com.johnromby_au518762.coronatrackerapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.johnromby_au518762.coronatrackerapp.model.CountryRepository;
import com.johnromby_au518762.coronatrackerapp.model.Country;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private final CountryRepository repository;
    private final LiveData<List<Country>> allCountries;

    public ListViewModel(@NonNull Application application) {
        super(application);
        repository = CountryRepository.getInstance();
        allCountries = repository.getAllCountries();
    }

    public void insert(String countryName) {
        repository.sendRequestForDailySummery(countryName);
    }

    public LiveData<List<Country>> getAllCountries() {
        return allCountries;
    }

    public void deleteAll() {
        repository.deleteAllCountries();
    }

    public void updateSelectedCountry(int index) {
        repository.setCurrentCountry(index);
    }
}
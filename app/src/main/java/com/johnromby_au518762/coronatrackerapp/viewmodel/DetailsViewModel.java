package com.johnromby_au518762.coronatrackerapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.johnromby_au518762.coronatrackerapp.model.CountryRepository;
import com.johnromby_au518762.coronatrackerapp.model.Country;

public class DetailsViewModel extends AndroidViewModel {
    private final CountryRepository repository;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        repository = CountryRepository.getInstance();
    }

    public Country getCurrentCountry() {
        return repository.getCurrentCountry();
    }

    public void deleteCurrentCountry(Country selectedCountry) {
        repository.delete(selectedCountry);
    }
}
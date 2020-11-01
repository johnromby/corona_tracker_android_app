package com.johnromby_au518762.coronatrackerapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.johnromby_au518762.coronatrackerapp.model.CountryRepository;
import com.johnromby_au518762.coronatrackerapp.model.Country;

public class EditViewModel extends AndroidViewModel {
    private final CountryRepository repository;

    public EditViewModel(@NonNull Application application) {
        super(application);
        repository = CountryRepository.getInstance();
    }

    public Country getCurrentCountry() {
        return repository.getCurrentCountry();
    }

    public void updateSelectedCountry(Country selectedCountry) {
        repository.update(selectedCountry);
    }
}

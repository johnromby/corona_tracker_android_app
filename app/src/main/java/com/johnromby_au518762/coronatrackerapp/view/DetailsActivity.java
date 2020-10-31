package com.johnromby_au518762.coronatrackerapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.johnromby_au518762.coronatrackerapp.viewmodel.DetailsViewModel;
import com.johnromby_au518762.coronatrackerapp.R;
import com.johnromby_au518762.coronatrackerapp.model.Country;

public class DetailsActivity extends AppCompatActivity {
    // For debugging
    private static final String TAG = "DetailsActivity";

    // ViewModel
    private DetailsViewModel detailsViewModel;

    // Widgets
    private ImageView imgFlag;
    private TextView txtCountryName, txtCasesNum, txtDeathsNum, txtUserRatingNum, txtMLUserNotes;
    private Button btnBack, btnEdit;

    // Data holder
    private Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Setting up ViewModel
        detailsViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DetailsViewModel.class);
        // TODO (nice-to-have): Maybe bind on the LiveData for the selected country?
        //  This way when new data are fetched from the web api it will also show in this activity.

        // Getting current selected Country
        selectedCountry = detailsViewModel.getCurrentCountry();

        // Binding Widgets
        bindWidgets();

        // Updating view/activity
        updateView();
    }

    private void bindWidgets() {
        // Binding to Widgets
        imgFlag = findViewById(R.id.imgFlag);
        txtCountryName = findViewById(R.id.txtCountryName);
        txtCasesNum = findViewById(R.id.txtCasesNum);
        txtDeathsNum = findViewById(R.id.txtDeathsNum);
        txtUserRatingNum = findViewById(R.id.txtUserRatingNum);
        txtMLUserNotes = findViewById(R.id.txtMLUserNotes);

        // Binding to Buttons
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> openEditActivity());
    }

    private void openEditActivity() {
        startActivity(new Intent(this, EditActivity.class));
    }

    private void updateView() {
        // Activity title
        setTitle(getResources().getText(R.string.detailsActivityTitle) + ": " + selectedCountry.getCountryName());

        // TODO (DRY): Maybe move this to a utility class!?
        // Getting flag from online api
        // Credit: https://github.com/bumptech/glide
        Glide.with(this)
                .load("https://www.countryflags.io/" + selectedCountry.getCountryCode() + "/flat/64.png")
                .placeholder(R.drawable.no_image_placeholder)
                .into(imgFlag);

        txtCountryName.setText(selectedCountry.getCountryName());
        txtCasesNum.setText(selectedCountry.getNumInfectedAsString());
        txtDeathsNum.setText(selectedCountry.getNumDeathAsString());
        txtUserRatingNum.setText(selectedCountry.getUserRatingAsString());
        txtMLUserNotes.setText(selectedCountry.getUserNote());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Details Activity restarted.");
        updateView();
    }
}
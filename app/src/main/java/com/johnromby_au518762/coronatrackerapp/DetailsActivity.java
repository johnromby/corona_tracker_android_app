package com.johnromby_au518762.coronatrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";

    // Widgets:
    ImageView imgFlag;
    TextView txtCountryName, txtCasesNum, txtDeathsNum, txtUserRatingNum, txtMLUserNotes;
    Button btnBack, btnEdit;

    // Data holder
    Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Binding to widgets
        imgFlag = findViewById(R.id.imgFlag);
        txtCountryName = findViewById(R.id.txtCountryName);
        txtCasesNum = findViewById(R.id.txtCasesNum);
        txtDeathsNum = findViewById(R.id.txtDeathsNum);
        txtUserRatingNum = findViewById(R.id.txtUserRatingNum);
        txtMLUserNotes = findViewById(R.id.txtMLUserNotes);

        txtMLUserNotes.setEnabled(false);

        // Buttons
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToListActivity();
            }
        });
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditActivity();
            }
        });

        // Getting intent
        Intent intent = getIntent();
        // Using Parcelable is based on YouTube video: https://www.youtube.com/watch?v=WBbsvqSu0is
        selectedCountry = intent.getParcelableExtra(Constants.SELECTED_COUNTRY);
        // Calling method for updating the view with data from the passed object
        updateView(selectedCountry);
    }

    private void openEditActivity() {
        // TODO: Open EditActivity with passed data.
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Constants.SELECTED_COUNTRY, selectedCountry);
        startActivityForResult(intent, Constants.REQUEST_CODE_EDIT);
    }

    private void goBackToListActivity() {
        // TODO: Send data back to ListActivity if it has changed.
        finish();
    }

    private void updateView(Country country) {
        imgFlag.setImageResource(country.flagImgResId);
        txtCountryName.setText(country.countryName);
        txtCasesNum.setText("" + country.numInfected);
        txtDeathsNum.setText("" + country.numDeath);
        txtUserRatingNum.setText("" + country.userRating);
        txtMLUserNotes.setText(country.userNote);
    }
}
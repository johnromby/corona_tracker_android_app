package com.johnromby_au518762.coronatrackerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    // For debugging
    private static final String TAG = "DetailsActivity";

    // Widgets
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

        // Saving state for rotations
        // Note that edit text fields are saved automatically, so this is only for preserving rating.
        if (savedInstanceState != null) {
            selectedCountry = savedInstanceState.getParcelable(Constants.SELECTED_COUNTRY);
        }

        // Calling method for updating the view with data from the passed object
        updateView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                selectedCountry = data.getParcelableExtra(Constants.SELECTED_COUNTRY);
                updateView();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(DetailsActivity.this, R.string.cancelToast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.SELECTED_COUNTRY, selectedCountry);
    }

    private void goBackToListActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.SELECTED_COUNTRY, selectedCountry);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void openEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Constants.SELECTED_COUNTRY, selectedCountry);
        startActivityForResult(intent, Constants.REQUEST_CODE_EDIT);
    }

    private void updateView() {
        imgFlag.setImageResource(selectedCountry.flagImgResId);
        txtCountryName.setText(selectedCountry.countryName);
        txtCasesNum.setText(String.valueOf(selectedCountry.numInfected));
        txtDeathsNum.setText(String.valueOf(selectedCountry.numDeath));
        txtUserRatingNum.setText(String.valueOf(selectedCountry.userRating));
        txtMLUserNotes.setText(selectedCountry.userNote);

        // Activity title
        setTitle(getResources().getText(R.string.detailsActivityTitle) + ": " + selectedCountry.countryName);
    }
}
package com.johnromby_au518762.coronatrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    // Widgets:
    ImageView imgFlag;
    TextView txtCountryName, txtUserRatingNum, txtMLUserNotes;
    SeekBar seekBarUserRating;
    Button btnCancel, btnOk;

    // Data holder
    Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Binding to widgets
        imgFlag = findViewById(R.id.imgFlag);
        txtCountryName = findViewById(R.id.txtCountryName);
        txtUserRatingNum = findViewById(R.id.txtUserRatingNum);
        txtMLUserNotes = findViewById(R.id.txtMLUserNotes);

        // SeekBar
        seekBarUserRating = findViewById(R.id.seekBarUserRating);
        seekBarUserRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtUserRatingNum.setText(String.format("%.1f", progress * 0.1f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Buttons
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: update selectedCountry object and return it to DetailsActivity
            }
        });

        // Getting intent
        Intent intent = getIntent();
        // Using Parcelable is based on YouTube video: https://www.youtube.com/watch?v=WBbsvqSu0is
        selectedCountry = intent.getParcelableExtra(Constants.SELECTED_COUNTRY);
        // Calling method for updating the view with data from the passed object
        updateView(selectedCountry);
    }

    private void updateView(Country country) {
        imgFlag.setImageResource(country.flagImgResId);
        txtCountryName.setText(country.countryName);
        txtUserRatingNum.setText("" + country.userRating);
        txtMLUserNotes.setText(country.userNote);
    }
}
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

    // For debugging
    private static final String TAG = "EditActivity";

    // Widgets
    private ImageView imgFlag;
    private TextView txtCountryName, txtUserRatingNum, txtMLUserNotes;
    private SeekBar seekBarUserRating;

    // Data holder
    private Country selectedCountry;

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
                txtUserRatingNum.setText(String.valueOf(progress / 10f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Buttons
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToDetailsActivity();
            }
        });

        // Getting intent
        Intent intent = getIntent();
        // Using Parcelable is based on YouTube video: https://www.youtube.com/watch?v=WBbsvqSu0is
        selectedCountry = intent.getParcelableExtra(Constants.SELECTED_COUNTRY);
        // Calling method for updating the view with data from the passed object
        updateView();
    }

    private void goBackToDetailsActivity() {
/*        selectedCountry.setUserRating(Double.parseDouble(txtUserRatingNum.getText().toString()));
        selectedCountry.setUserNote(txtMLUserNotes.getText().toString());

        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.SELECTED_COUNTRY, selectedCountry);
        setResult(RESULT_OK, resultIntent);*/
        finish();
    }

    private void updateView() {
//        imgFlag.setImageResource(selectedCountry.getFlagImgResId());
        txtCountryName.setText(selectedCountry.getCountryName());
        txtUserRatingNum.setText(selectedCountry.getUserRatingAsString());
        txtMLUserNotes.setText(selectedCountry.getUserNote());

        seekBarUserRating.setProgress((int) (selectedCountry.getUserRating() * 10));

        // Activity title
        setTitle(getResources().getText(R.string.editActivityTitle) + ": " + selectedCountry.getCountryName());
    }
}
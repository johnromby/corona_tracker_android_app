package com.johnromby_au518762.coronatrackerapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.johnromby_au518762.coronatrackerapp.R;
import com.johnromby_au518762.coronatrackerapp.model.Country;
import com.johnromby_au518762.coronatrackerapp.viewmodel.DetailsViewModel;
import com.johnromby_au518762.coronatrackerapp.viewmodel.EditViewModel;

public class EditActivity extends AppCompatActivity {
    // For debugging
    private static final String TAG = "EditActivity";

    // ViewModel
    private EditViewModel editViewModel;

    // Widgets
    private ImageView imgFlag;
    private TextView txtCountryName, txtUserRatingNum, txtMLUserNotes;
    private SeekBar seekBarUserRating;
    private Button btnOk, btnCancel;

    // Data holder
    private Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Setting up ViewModel
        editViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(EditViewModel.class);

        // Getting current selected Country
        selectedCountry = editViewModel.getCurrentCountry();

        // Binding Widgets
        bindWidgets();

        // Updating view/activity
        updateView();
    }

    private void bindWidgets() {
        // Binding to widgets
        imgFlag = findViewById(R.id.imgFlag);
        txtCountryName = findViewById(R.id.txtCountryName);
        txtUserRatingNum = findViewById(R.id.txtUserRatingNum);
        txtMLUserNotes = findViewById(R.id.txtMLUserNotes);

        // Binding to SeekBar
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

        // Binding to Buttons
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> goBackToDetailsActivity());
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> finish());
    }

    private void goBackToDetailsActivity() {
        selectedCountry.setUserRating(Double.parseDouble(txtUserRatingNum.getText().toString()));
        selectedCountry.setUserNote(txtMLUserNotes.getText().toString());
        editViewModel.updateSelectedCountry(selectedCountry);
        finish();
    }

    private void updateView() {
        // Activity title
        setTitle(getResources().getText(R.string.editActivityTitle) + ": " + selectedCountry.getCountryName());

        // TODO (DRY): Maybe move this to a utility class!?
        // Getting flag from online api
        // Credit: https://github.com/bumptech/glide
        Glide.with(this)
                .load("https://www.countryflags.io/" + selectedCountry.getCountryCode() + "/flat/64.png")
                .placeholder(R.drawable.no_image_placeholder)
                .into(imgFlag);

        txtCountryName.setText(selectedCountry.getCountryName());
        txtUserRatingNum.setText(selectedCountry.getUserRatingAsString());
        txtMLUserNotes.setText(selectedCountry.getUserNote());

        seekBarUserRating.setProgress((int) (selectedCountry.getUserRating() * 10));
    }
}
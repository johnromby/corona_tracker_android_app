/*
 NOTES:
 Inspiration was drawn from the video made by Kasper Løvborg Jensen: "Demo 2: RecyclerView in action" under Lesson 3 (L3: Android UI) ITSMAP-01 fall 2020.
 Code comments are also taken from this video - although some are rewritten a bit - to personally get a better grasp on whats going on, and of course to remember various parts of the code for later review.
*/

package com.johnromby_au518762.coronatrackerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements CountryAdapter.ICountryItemClickedListener {

    // For debugging
    private static final String TAG = "ListActivity";

    // Widgets:
    private RecyclerView rcvList;
    private CountryAdapter adapter;
    private Button btnExit;

    // Data:
    private ArrayList<Country> countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Recyclerview Set-up (Adapter and Layout Manager)
        adapter = new CountryAdapter(this);
        rcvList = findViewById(R.id.rcvCountries);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        // Creating data and updating the Adapter/RecycleView
        createData();
        adapter.updateCountryList(countries);

        // Button(s) handle
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Creating the list of Country objects
    private void createData() {

        countries = new ArrayList<>();

        // Based on YouTube video: https://www.youtube.com/watch?v=i-TqNzUryn8
        // TODO: Make a class for this CSV reader, so it can be used in other situations ;)
        InputStream is = getResources().openRawResource(R.raw.corona_stats);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        try {
            while ( (line = reader.readLine()) != null ) {

                // Split data by ';'
                String[] tokens = line.split(";");

                // Read data
                Country sample = new Country(
                        tokens[0],
                        tokens[1],
                        getResources().getIdentifier(tokens[1].toLowerCase(), "drawable", getApplication().getPackageName()),
                        Integer.parseInt(tokens[2]),
                        Integer.parseInt(tokens[3]),
                        0.0,
                        ""
                );

                // Adding data to array list
                countries.add(sample);
                
                // Logging the sample data that was just created:
                Log.d(TAG, "Just created data: " + sample);
            }
        } catch (IOException e) {
                Log.wtf(TAG, "Error reading data file" + line, e);
                e.printStackTrace();
        }
    }

    // Callback when a country item is clicked in the list
    @Override
    public void onCountryClicked(int index) {
        openDetailsActivity(countries.get(index));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_DETAILS) {
            if (resultCode == RESULT_OK) {
                Country selectedCountry = data.getParcelableExtra(Constants.SELECTED_COUNTRY);
                updateArray(selectedCountry);
            }
        }
    }

    private void updateArray(Country selectedCountry) {
        for (Country stats : countries) {
            if (stats.flagImgResId == selectedCountry.flagImgResId) {
                countries.set(countries.indexOf(stats), selectedCountry);
                return;
            }
        }
    }

    private void openDetailsActivity(Country country) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.SELECTED_COUNTRY, country);

        // Using startActivityForResult() and not just startActivity() since we are expecting something back.
        // onActivityResult() will handle the callback data.
        startActivityForResult(intent, Constants.REQUEST_CODE_DETAILS);
    }
}
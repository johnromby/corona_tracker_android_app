package com.johnromby_au518762.coronatrackerapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

// TODO make this utility class generic.
public final class CsvUtil {
    private Context context;
    private int resourceIdOfCsvFile;
    private final String delimiter = ";";

    // Debug Tag
    private static final String TAG = "CsvUtil";

    public CsvUtil(Context context, int resourceIdOfCsvFile) {
        this.context = context;
        this.resourceIdOfCsvFile = resourceIdOfCsvFile;
    }

    public List<Country> GetCountriesFromCsvFile() {
        // Inspired on the YouTube video: https://www.youtube.com/watch?v=i-TqNzUryn8

        List<Country> countries = new ArrayList<>();

        // Creating the list of Country objects:
        InputStream is = context.getResources().openRawResource(resourceIdOfCsvFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        try {
            while ( (line = reader.readLine()) != null ) {

                // Split data by ';'
                String[] tokens = line.split(delimiter);

                // Read data
                Country country = new Country(
                        tokens[0],
                        tokens[1],
                        context.getResources().getIdentifier(tokens[1].toLowerCase(), "drawable", context.getPackageName()),
                        Integer.parseInt(tokens[2]),
                        Integer.parseInt(tokens[3]),
                        0.0,
                        ""
                );

                // Adding data to array list
                countries.add(country);

                // Logging the country that was just created:
                Log.d(TAG, "Just created country: " + country);
            }
        } catch (IOException e) {
            Log.wtf(TAG, "Error reading data file" + line, e);
            e.printStackTrace();
        }

        return countries;
    }
}
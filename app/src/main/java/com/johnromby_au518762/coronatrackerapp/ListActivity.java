/*
 NOTES:
 Inspiration was drawn from the video made by Kasper Løvborg Jensen: "Demo 2: RecyclerView in action" under Lesson 3 (L3: Android UI) ITSMAP-01 fall 2020.
 Code comments are also taken from this video - although some are rewritten a bit - to personally get a better grasp on whats going on, and of course to remember various parts of the code for later review.
*/

package com.johnromby_au518762.coronatrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends AppCompatActivity implements CountryAdapter.ICountryItemClickedListener {
    // ViewModel
    private ListViewModel listViewModel;

    // For debugging
    private static final String TAG = "ListActivity";

    // Widgets
    private RecyclerView rcvList;
    private CountryAdapter adapter;
    private EditText editTextSearchField;
    private Button btnAdd;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Activity title
        setTitle(getResources().getText(R.string.app_name) + " - " + getResources().getText(R.string.listActivityTitle));

        // Recyclerview Set-up (Adapter and Layout Manager)
        adapter = new CountryAdapter(this);
        rcvList = findViewById(R.id.rcvCountries);
        rcvList.setHasFixedSize(true); // This increases performance for RecyclerViews that does not change their size.
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        // Setting up ViewModel
        listViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ListViewModel.class);
        listViewModel.getAllCountries().observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                adapter.updateCountryList(countries);
            }
        });

        // Country search field
        editTextSearchField = findViewById(R.id.editTextSearchField);

        // Button(s) handle
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewModel.insert(editTextSearchField.getText().toString());
                editTextSearchField.setText("");
                // TODO Make a Toast or notification that shows if the country was added successfully or not.
//                Toast.makeText(ListActivity.this, "New country added: " + editTextSearchField.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Callback when a country item is clicked in the list
    @Override
    public void onCountryClicked(int index) {
        Toast.makeText(this, "Country with index: " + index + " clicked!", Toast.LENGTH_SHORT).show();
        // TODO: Remove Toast and open DetailsActivity here.
//        openDetailsActivity(countries.get(index));
    }
}
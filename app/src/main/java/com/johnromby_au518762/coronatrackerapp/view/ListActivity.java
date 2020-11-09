/*
 NOTES:
 Inspiration was drawn from the video made by Kasper Løvborg Jensen: "Demo 2: RecyclerView in action" under Lesson 3 (L3: Android UI) ITSMAP-01 fall 2020.
 Code comments are also taken from this video - although some are rewritten a bit - to personally get a better grasp on whats going on, and of course to remember various parts of the code for later review.
 Credit for ViewModel part goes to: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-5-viewmodel
*/

package com.johnromby_au518762.coronatrackerapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.johnromby_au518762.coronatrackerapp.view.adaptor.CountryAdapter;
import com.johnromby_au518762.coronatrackerapp.viewmodel.ListViewModel;
import com.johnromby_au518762.coronatrackerapp.R;
import com.johnromby_au518762.coronatrackerapp.service.ForegroundUpdateService;

import java.util.Timer;
import java.util.TimerTask;

public class ListActivity extends AppCompatActivity implements CountryAdapter.ICountryItemClickedListener {
    // For debugging
    private static final String TAG = "ListActivity";

    // ViewModel
    private ListViewModel listViewModel;

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

        // TODO (Improvement): Title is a tad too long for portrait layout. Fix this!
        // Activity title
        setTitle(getResources().getText(R.string.app_name) + " - " + getResources().getText(R.string.listActivityTitle));

        // ViewModel + Recyclerview Set-up (Adapter and Layout Manager)
        listActivityInit();

        // Binding Widgets
        bindWidgets();

        Intent intent = new Intent(this, ForegroundUpdateService.class);

        // Starting ForegroundService (ForegroundUpdateService) with delay to fix a crash.
        // https://stackoverflow.com/a/4562300
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //run your service
                startService(intent);
            }
        }, 2000);
    }

    private void listActivityInit() {
        // Setting up Recyclerview (Adapter and Layout Manager)
        adapter = new CountryAdapter(this);
        rcvList = findViewById(R.id.rcvCountries);
        rcvList.setHasFixedSize(true); // This increases performance for RecyclerViews that does not change their size.
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        // Setting up ViewModel
        // Credit to this ("Eventually I found this next line worked for me."): https://stackoverflow.com/questions/62226803/room-viewmodel-livedata-recyclerview-mvvm-part-7-add-note-activity-a
        // Because of deprecation mentioned here: https://stackoverflow.com/questions/53903762/viewmodelproviders-is-deprecated-in-1-1-0
        listViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ListViewModel.class);
        listViewModel.getAllCountries().observe(this, countries -> adapter.updateCountryList(countries));
    }

    private void bindWidgets() {
        // Country search field
        editTextSearchField = findViewById(R.id.editTextSearchField);

        // Button(s) handle
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewModel.insert(editTextSearchField.getText().toString());
                editTextSearchField.setText("");
                // TODO (Improvement): Make a Toast or notification of some sort, that shows if the country was added successfully or not.
//                Toast.makeText(ListActivity.this, "New country added: " + editTextSearchField.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> finish());

        // OBS. This button is hidden from view. Should only be used in engineering release.
        Button btnDeleteAll = findViewById(R.id.btnDeleteAll);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewModel.deleteAll();
                Toast.makeText(ListActivity.this, "All data is now gone!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Callback when a country item is clicked in the list
    @Override
    public void onCountryClicked(int index) {
        listViewModel.updateSelectedCountry(index);
        startActivity(new Intent(this, DetailsActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: List Activity restarted.");
        adapter.notifyDataSetChanged();
        Log.d(TAG, "onRestart: List Activity RecyclerView updated.");
    }
}
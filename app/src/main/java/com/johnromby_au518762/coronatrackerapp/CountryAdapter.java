/*
 NOTES:
 Inspiration was drawn from the video made by Kasper Løvborg Jensen: "Demo 2: RecyclerView in action" under Lesson 3 (L3: Android UI) ITSMAP-01 fall 2020.
 Code comments are also taken from this video - although some are rewritten a bit - to personally get a better grasp on whats going on, and of course to remember various parts of the code for later review.
*/

package com.johnromby_au518762.coronatrackerapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

// Custom implementation of the RecyclerView.Adaptor class that handles a list of Country objects
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private static final String TAG = "CountryAdapterDebugLog";

    // Interface for handling when the user clicks an country item in the RecycleView:
    public interface ICountryItemClickedListener {
        void onCountryClicked(int index);
    }

    // Callback interface for when the user clicks an country item in the RecycleView:
    private ICountryItemClickedListener listener;

    // Adapter data:
    private List<Country> countryList = new ArrayList<>();

    // Constructor:
    public CountryAdapter(ICountryItemClickedListener listener) {
        this.listener = listener;
    }

    // Method for updating the country list, this causes the Adapter/RecyclerView to update:
    public void updateCountryList(List<Country> countries) {
        countryList = countries;
        notifyDataSetChanged();
    }

    // Override this method to create the ViewHolder object the first time they are requested
    // Use the inflater from parent (Activity's ViewGroup) to get the view and then use view holders constructor
    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new CountryViewHolder(v, listener);
    }

    // Override this to fill in data from Country object at position into the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

        // Current position taken from which country the user clicked:
        Country selectedCountry = countryList.get(position);
        Log.d(TAG, "Selected Country is: " + selectedCountry.getCountryName());

        // Binding txtViews etc. with the data from the selected country:
        // Inspired by E20-ITSMAP L6 Demo video: "Rick and Morty Gallery with Volley and Glide"
        Glide.with(holder.imgFlag.getContext())
                .load("https://www.countryflags.io/" + selectedCountry.getCountryCode() + "/flat/64.png")
                .placeholder(R.drawable.no_image_placeholder)
                .into(holder.imgFlag);
        holder.txtCountry.setText(selectedCountry.getCountryName());
        holder.txtCasesDeaths.setText(selectedCountry.getCasesSlashDeaths());
        holder.txtRating.setText(selectedCountry.getUserRatingAsString());
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ViewHolder UI Widget references:
        ImageView imgFlag;
        TextView txtCountry, txtCasesDeaths, txtRating;

        // Custom callback interface for user actions done the ViewHolder item
        ICountryItemClickedListener listener;

        // Constructor:
        public CountryViewHolder(@NonNull View itemView, ICountryItemClickedListener countryItemClickedListener) {
            super(itemView);

            // Getting references from the layout file:
            imgFlag = itemView.findViewById(R.id.imgFlag);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtCasesDeaths = itemView.findViewById(R.id.txtCasesDeaths);
            txtRating = itemView.findViewById(R.id.txtRating);
            this.listener = countryItemClickedListener;

            // Set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        //react to user clicking the list-item (implements OnClickListener)
        @Override
        public void onClick(View v) {
            listener.onCountryClicked(getAdapterPosition());
        }
    }
}
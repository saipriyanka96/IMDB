package com.example.layout.imdb.mainwork;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.layout.imdb.R;
import com.example.layout.imdb.fragment.MovieDetails;

/**
 * Created by Pri on 10/21/2017.
 */

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setArguments(getIntent().getExtras());
        /*setArguments:Supply the construction arguments for this fragment. The arguments supplied here will be retained across fragment destroy and creation.
        getIntent():Return the intent that started this activity.
        getExtras ():Retrieves a map of extended data from the intent.
Returns Bundle	the map of all extras previously added with putExtra(), or null if none have been added.

         */
        transaction.replace(R.id.fragmentContainer2, movieDetails);
        transaction.commit();
    }
}
package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MovieDetailFragment detailFragment = new MovieDetailFragment();
            detailFragment.setArguments(extras);

            getFragmentManager().beginTransaction()
                    .add(R.id.detail_container, detailFragment)
                    .commit();
        } else {
            Log.v(LOG_TAG, "Bundle is empty");
        }
    }
}

package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements OnPosterSelectedListener {

    /**
     * For logging purposes
     */
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Are one or two panes showing
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_details_container) != null) {
            mTwoPane = true;

            if (savedInstanceState != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, createFragment(savedInstanceState),
                                getString(R.string.detail_fragment_key))
                        .commit();
            } else {
                Log.v(LOG_TAG,
                        "savedInstanceState is null. Should only happen at app startup on " +
                                "large screen layouts.");

            }
        } else {
            mTwoPane = false;
        }
    }

    /**
     * Handles clicks on movie posters
     *
     * @param bundle Information on movie poster clicked
     */
    public void onPosterSelected(Bundle bundle) {

        if (mTwoPane) {
            if(bundle != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, createFragment(bundle),
                                getString(R.string.detail_fragment_key))
                        .commit();
            }
        } else {
            startActivity(createIntent(bundle));
        }
    }

    /**
     * Returns a MovieDetailFragment
     *
     * @param bundle Arguments
     * @return MovieDetailFragment
     */
    private MovieDetailFragment createFragment(Bundle bundle) {
        MovieDetailFragment fragment = new MovieDetailFragment();

        // If there are some arguments the need to be passed on
        if (bundle != null && !bundle.isEmpty())
            fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Returns a DetailActivity
     * @param bundle Extras
     * @return DetailActivity
     */
    private Intent createIntent(Bundle bundle) {
        Intent intent = new Intent(this, DetailActivity.class);

        if(bundle != null && !bundle.isEmpty())
            intent.putExtras(bundle);

        return intent;
    }
}
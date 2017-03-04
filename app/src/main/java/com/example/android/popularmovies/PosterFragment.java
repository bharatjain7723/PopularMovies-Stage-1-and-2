package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PosterFragment extends Fragment {

    /**
     * For logging purposes
     */
    private final String LOG_TAG = PosterFragment.class.getSimpleName();
    /**
     * Listener for clicks on movie posters in GridView
     */
    private final GridView.OnItemClickListener posterClickListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MovieData movie = (MovieData) parent.getItemAtPosition(position);
            Bundle bundle = new Bundle();
            bundle.putParcelable(getResources().getString(R.string.parcel_movie), movie);

            ((OnPosterSelectedListener) getActivity()).onPosterSelected(bundle);
        }
    };
    /**
     * Presents movie posters
     */
    private GridView mGridView;
    /**
     * Holds menu items
     */
    private Menu mMenu;

    public PosterFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreatePosterFragment");
        setHasOptionsMenu(true);

        if(savedInstanceState == null) {
            Log.v(LOG_TAG, "Fragment is created with no bundle");
        } else {
            Log.v(LOG_TAG, "Fragment is created with some content in bundle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.movie_overview, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(posterClickListener);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(getString(R.string.parcel_movie))) {

            // Get Movie objects
            List<MovieData> movies = savedInstanceState
                    .getParcelableArrayList(getString(R.string.parcel_movie));

            if (movies != null && !movies.isEmpty()) {
                // Load movie objects into view
                mGridView.setAdapter(new PosterAdapter(getActivity(), movies));

                Log.v(LOG_TAG, "Got movie info from savedInstanceState");
            }
        } else {
            // Get data from the Internet
            getMoviesFromTMDb(getSortingMethod());
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "onSaveInstanceState");

        int numMovieObjects = mGridView.getCount();
        if (numMovieObjects > 0) {
            // Get Movie objects from gridview
            ArrayList<MovieData> movies = new ArrayList<>();
            for (int i = 0; i < numMovieObjects; i++) {
                movies.add((MovieData) mGridView.getItemAtPosition(i));
            }

            // Save Movie objects to bundle
            outState.putParcelableArrayList(getString(R.string.parcel_movie), movies);
        } else {
            Log.w(LOG_TAG, "onSaveInstanceState had no movie objects to put in bundle");
        }

        super.onSaveInstanceState(outState);
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu, mMenu);
//        mMenu = menu;
//        super.onCreateOptionsMenu(menu, inflater);
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                updateSharedPrefrences(getString(R.string.sort_most_popular));
                getMoviesFromTMDb(getSortingMethod());
                return true;
            case R.id.top_rated:
                updateSharedPrefrences(getString(R.string.sort_top_rated));
                getMoviesFromTMDb(getSortingMethod());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * If device has Internet the magic happens when app launches. The app will start the process
     * of collecting data from the API and present it to the user.
     * <p/>
     * If the device has no connectivity it will display a Toast explaining that app needs
     * Internet to work properly.
     *
     * @param sortMethod tmdb API method for sorting movies
     */
    private void getMoviesFromTMDb(String sortMethod) {
        if (isNetworkAvailable()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TMDbService service = retrofit.create(TMDbService.class);

            Call<MoviesWrapper> call = service.getMovies(sortMethod, BuildConfig.THE_MOVIE_DB_API_KEY);


            call.enqueue(new Callback<MoviesWrapper>() {
                @Override
                public void onResponse(Response<MoviesWrapper> response, Retrofit retrofit) {
                    Log.v(LOG_TAG, "Got response from API call");

                    // Populate GridView with results
                    mGridView.setAdapter(new PosterAdapter(getActivity(), response.body().getResults()));
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(LOG_TAG, "Something went wrong in getting data from API.", t);
                }
            });
        } else {
            // Notify the user's device has no Internet connection
            Toast.makeText(getActivity(), getString(R.string.error_need_internet),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Gets the sort method set by user from SharedPreferences. If no sort method is defined it will
     * default to sorting by popularity.
     *
     * @return Sort method from SharedPreferenced
     */
    private String getSortingMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return prefs.getString(getString(R.string.pref_sort_method_key), getString(R.string.sort_most_popular));
    }

    /**
     * Checks if there is Internet accessible.
     * Based on a stackoverflow snippet
     *
     * @return True if there is Internet. False if not.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        Log.v(LOG_TAG, "isNetworkAvailable() returns: " + (activeNetworkInfo != null &&
                activeNetworkInfo.isConnected()));

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Saves the selected sort method
     *
     * @param sortMethod Sort method to save
     */
    private void updateSharedPrefrences(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_sort_method_key), sortMethod);
        editor.apply();
    }
}

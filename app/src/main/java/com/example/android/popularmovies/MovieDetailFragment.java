package com.example.android.popularmovies;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.utilities.DateTimeUtility;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MovieDetailFragment extends Fragment {

    /**
     * For logging purposes
     */
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    /**
     * Holds trailer objects
     */
    List<Trailer> mTrailers;

    /**
     * Holds review objects
     */
    List<Review> mReviews;

    /**
     * Listener for clicks on reviews or trailers.
     */

    /**
     * TMDb API key
     */

    private final LinearLayout.OnClickListener layoutOnClickListener = new LinearLayout.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(LOG_TAG, String.valueOf(v.getId()) + " was clicked");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog dialog;

            switch (v.getId()) {
                case R.id.linear_layout_trailers:
                    // Build a list containing all trailers for the movie. When clicked it will
                    // show the trailer in the user preferred application for watching that kind
                    // of link.

                    TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), mTrailers);

                    dialog = builder.setTitle(getString(R.string.trailers))
                            .setAdapter(trailerAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Trailer trailer = mTrailers.get(which);
                                    String uri = "http://www.youtube.com/watch?v=" + trailer.getKey();

                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(uri)));
                                }
                            })
                            .create();

                    dialog.show();
                    break;

                case R.id.linear_layout_reviews:
                    // Show a dialog containing user reviews.

                    ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), mReviews);

                    dialog = builder.setTitle(getString(R.string.reviews))
                            .setAdapter(reviewAdapter, null)
                            .create();

                    dialog.show();
                    break;

                default:
                    Log.e(LOG_TAG, String.valueOf(v.getId()) + " is not a valid id");
            }
        }
    };

    /**
     * Views
     */
    private TextView mTvOriginalTitle;
    private ImageView mIvPoster;
    private TextView mTvOverView;
    private TextView mTvVoteAverage;
    private TextView mTvReleaseDate;
    private LinearLayout mLlTrailers;
    private LinearLayout mLlReviews;
    private Button mButtonAddToFavs;

    /**
     * Holds movie object used to show detail
     */
    private MovieData mMovie;

    /**
     * Constructor
     */
    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreateView");

        // Get movie information from bundle
        Bundle arguments = getArguments();
        String key = getString(R.string.parcel_movie);
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            mMovie = savedInstanceState.getParcelable(key);
        } else if (arguments != null && arguments.containsKey(key)) {
            mMovie = arguments.getParcelable(key);
        } else {
            Log.v(LOG_TAG, "Bundle is empty");
        }

        // Get information from the net
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getTrailers(retrofit);
        getReviews(retrofit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");

        // Inflate and map views
        View rootView = inflater.inflate(R.layout.movie_details, container, false);

        // Init views
        initViewElements(rootView);
        setViewElementListeners();

        // Load movie information to view
        updateDetailView();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "onSaveInstanceState");

        if (mMovie != null) {
            // Save Movie object to bundle
            outState.putParcelable(getString(R.string.parcel_movie), mMovie);
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes and maps layout elements
     *
     * @param rootView Rootview of layout
     */
    private void initViewElements(View rootView) {
        Log.v(LOG_TAG, "initViewElements");

        mTvOriginalTitle = (TextView) rootView.findViewById(R.id.textview_original_title);
        mIvPoster = (ImageView) rootView.findViewById(R.id.imageview_poster);
        mTvOverView = (TextView) rootView.findViewById(R.id.textview_overview);
        mTvVoteAverage = (TextView) rootView.findViewById(R.id.textview_vote_average);
        mTvReleaseDate = (TextView) rootView.findViewById(R.id.textview_release_date);
        mLlTrailers = (LinearLayout)
                rootView.findViewById(R.id.linear_layout_trailers);
        mLlReviews = (LinearLayout)
                rootView.findViewById(R.id.linear_layout_reviews);
        mButtonAddToFavs = (Button) rootView.findViewById(R.id.button_favorite_movie);
    }

    /**
     * Sets click listeners
     */
    private void setViewElementListeners() {
        mLlTrailers.setOnClickListener(layoutOnClickListener);
        mLlTrailers.setClickable(false);

        mLlReviews.setOnClickListener(layoutOnClickListener);
        mLlReviews.setClickable(false);

        mButtonAddToFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Favorite button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates view with information on the movie.
     */
    private void updateDetailView() {
        if (mMovie != null) {
            mTvOriginalTitle.setText(mMovie.getOriginalTitle());

            Picasso.with(getActivity())
                    .load(getString(R.string.tmdb_posterpath_w185) + mMovie.getPosterPath())
                    .resize(getResources().getInteger(R.integer.w185_width),
                            getResources().getInteger(R.integer.w185_height))
                    .error(R.drawable.not_found)
                    .placeholder(R.drawable.searching)
                    .into(mIvPoster);

            String overView = mMovie.getOverview();
            if (overView == null) {
                mTvOverView.setTypeface(null, Typeface.ITALIC);
                overView = getResources().getString(R.string.no_summary_found);
            }
            mTvOverView.setText(overView);
            mTvVoteAverage.setText(mMovie.getDetailedVoteAverage());

            // First get the release date from the object - to be used if something goes wrong with
            // getting localized release date (catch).
            String releaseDate = mMovie.getReleaseDate();
            if (releaseDate != null) {
                try {
                    releaseDate = DateTimeUtility.getLocalizedDate(getActivity(),
                            releaseDate, mMovie.getDateFormat());
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Error with parsing movie release date", e);
                }
            } else {
                mTvReleaseDate.setTypeface(null, Typeface.ITALIC);
                releaseDate = getResources().getString(R.string.no_release_date_found);
            }
            mTvReleaseDate.setText(releaseDate);
        }
    }

    /**
     * Get trailers for this movie
     *
     * @param retrofit Retrofit builder
     */
    private void getTrailers(Retrofit retrofit) {
        TrailersService service = retrofit.create(TrailersService.class);

        Call<TrailersWrapper> call = service.getTrailers(mMovie.getId(), BuildConfig.THE_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<TrailersWrapper>() {
            @Override
            public void onResponse(Response<TrailersWrapper> response, Retrofit retrofit) {
                Log.v(LOG_TAG, "Got response from API call");

                // Populate GridView with results
                mTrailers = response.body().getResults();

                // Make view clickable based on if there's any data
                if (mTrailers.size() > 0) {
                    mLlTrailers.setClickable(true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "Something went wrong in getting data from API.", t);

                Toast.makeText(getActivity(), "There was a problem getting trailer data",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * Get reviews for this movie
     *
     * @param retrofit Retrofit builder
     */
    private void getReviews(Retrofit retrofit) {
        ReviewsService service = retrofit.create(ReviewsService.class);

        Call<ReviewsWrapper> call = service.getReviews(mMovie.getId(), BuildConfig.THE_MOVIE_DB_API_KEY);


        call.enqueue(new Callback<ReviewsWrapper>() {
            @Override
            public void onResponse(Response<ReviewsWrapper> response, Retrofit retrofit) {
                Log.v(LOG_TAG, "Got response from API call");

                // Populate GridView with results
                mReviews = response.body().getResults();

                // Make view clickable based on if there's any data
                if (mReviews.size() > 0) {
                    mLlReviews.setClickable(true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "Something went wrong in getting data from API.", t);

                Toast.makeText(getActivity(), "There was a problem getting review data",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}

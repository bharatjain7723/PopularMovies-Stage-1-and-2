package com.example.android.popularmovies;

import android.os.Bundle;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

interface OnPosterSelectedListener {
    void onPosterSelected(Bundle bundle);
}

/**
 * Gets movies from themoviedb
 */
interface TMDbService {
    @GET("/3/discover/movie")
    Call<MoviesWrapper> getMovies(
            @Query("sort_by") String sortBy,
            @Query("api_key") String apiKey);
}

/**
 * Gets trailers from themoviedb
 */
interface TrailersService {
    @GET("/3/movie/{id}/videos")
    Call<TrailersWrapper> getTrailers(
            @Path("id") int id,
            @Query("api_key") String apiKey);
}

/**
 * Gets user reviews from themoviedb
 */
interface ReviewsService {
    @GET("/3/movie/{id}/reviews")
    Call<ReviewsWrapper> getReviews(
            @Path("id") int id,
            @Query("api_key") String apiKey);
}

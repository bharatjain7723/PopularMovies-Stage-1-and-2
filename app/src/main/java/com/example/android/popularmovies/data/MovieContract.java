package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    /**
     * Inner class that defines the table contents of the movie table.
     */
    public static final class MovieEntry implements BaseColumns {

        /**
         * Table name
         */
        public static final String TABLE_NAME = "movies";

        /**
         * TMDb id of movie
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Original title of movie
         */
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        /**
         * Release date of movie
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /**
         * TMDb path to poster
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /**
         * TMDb user vote average
         */
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        /**
         * TMDb user vote average
         */
        public static final String COLUMN_OVERVIEW = "overview";

        /**
         * TMDb user vote average
         */
        public static final String COLUMN_USER_FAVORITE = "user_favorite";
    }

    /**
     * Inner class that defines the table contents of the trailers table.
     */
    public static final class TrailerEntry implements BaseColumns {

        /**
         * Table name
         */
        public static final String TABLE_NAME = "trailers";

        /**
         * Foreign key: TMDb id of movie
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Key of the link
         */
        public static final String COLUMN_TRAILER_KEY = "trailer_key";
    }

    /**
     * Inner class that defines the table contents of the reviews table.
     */
    public static final class ReviewEntry implements BaseColumns {

        /**
         * Table name
         */
        public static final String TABLE_NAME = "reviews";

        /**
         * Foreign key: TMDb id of movie
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Author of review
         */
        public static final String COLUMN_AUTHOR = "author";

        /**
         * Content of review
         */
        public static final String COLUMN_CONTENT = "content";
    }
}

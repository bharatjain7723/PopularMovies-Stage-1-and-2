package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movies.db";
    // Increment if change in DB schema
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL" +
                        " );";

        final String SQL_CREATE_TRAILERS_TABLE =
                "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                        MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL," +

                        // Foreign key referencing movie table
                        " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                        MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") );";

        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
                        MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL," +

                        // Foreign key referencing movie table
                        " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                        MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") );";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}

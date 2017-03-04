package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class PosterAdapter extends BaseAdapter {

    /**
     * For logging purposes
     */
    private final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private final Context mContext;
    private final List<MovieData> mMovies;

    /**
     * Constructor
     *  @param context Application context
     * @param movies  Movie array
     */
    public PosterAdapter(Context context, List<MovieData> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getCount() {
        if (mMovies == null || mMovies.isEmpty()) {
            return -1;
        }

        return mMovies.size();
    }

    @Override
    public MovieData getItem(int position) {
        if (mMovies == null || mMovies.isEmpty()) {
            return null;
        }

        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;

        // Will be null if it's not recycled. Will initialize ImageView if new.
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.poster_with_title, null);
        }

        imageView = (ImageView) convertView.findViewById(R.id.poster);
        textView = (TextView) convertView.findViewById(R.id.poster_title);

        Picasso.with(mContext)
                .load(mContext.getString(R.string.tmdb_posterpath_w185) + mMovies.get(position)
                        .getPosterPath())
                .resize(mContext.getResources().getInteger(R.integer.w185_width),
                        mContext.getResources().getInteger(R.integer.w185_height))
                .error(R.drawable.not_found)
                .placeholder(R.drawable.searching)
                .into(imageView);

        textView.setText(mMovies.get(position).getOriginalTitle());

        return convertView;
    }
}

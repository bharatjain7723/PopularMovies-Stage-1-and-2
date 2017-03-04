package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/*
 * Custom adapter for populating GridView.
 */
class TrailerAdapter extends BaseAdapter {

    /**
     * For logging purposes
     */
    private final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private final Context mContext;
    private final List<Trailer> mTrailers;

    /**
     * Constructor
     *
     * @param context  Application context
     * @param trailers Movie array
     */
    public TrailerAdapter(Context context, List<Trailer> trailers) {
        mContext = context;
        mTrailers = trailers;
    }

    @Override
    public int getCount() {
        if (mTrailers == null || mTrailers.isEmpty()) {
            return -1;
        }

        return mTrailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        if (mTrailers == null || mTrailers.isEmpty()) {
            return null;
        }

        return mTrailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        // Will be null if it's not recycled. Will initialize ImageView if new.
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_listitem, null);
        }

        textView = (TextView) convertView.findViewById(R.id.trailer_name);

        textView.setText(mTrailers.get(position).getName());

        return convertView;
    }
}

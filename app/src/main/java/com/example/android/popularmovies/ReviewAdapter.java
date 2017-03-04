package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class ReviewAdapter extends BaseAdapter {

    /**
     * For logging purposes
     */
    private final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private final Context mContext;
    private final List<Review> mReviews;

    /**
     * Constructor
     *
     * @param context Application context
     * @param reviews Movie array
     */
    public ReviewAdapter(Context context, List<Review> reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @Override
    public int getCount() {
        if (mReviews == null || mReviews.isEmpty()) {
            return -1;
        }

        return mReviews.size();
    }

    @Override
    public Review getItem(int position) {
        if (mReviews == null || mReviews.isEmpty()) {
            return null;
        }

        return mReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvReviewer;
        TextView tvReview;

        // Will be null if it's not recycled. Will initialize ImageView if new.
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.review_listitem, null);
        }

        tvReviewer = (TextView) convertView.findViewById(R.id.reviewer);
        tvReviewer.setText(mReviews.get(position).getAuthor());

        tvReview = (TextView) convertView.findViewById(R.id.review);
        tvReview.setText(mReviews.get(position).getContent());

        return convertView;
    }
}

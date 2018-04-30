package com.example.android.officehours;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Recycler View displays a scrolling list of elements based on large data sets.
 * The cells are displayed more easily using this View.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.OHListViewHolder> {
    private List<OHCell> mOfficeHoursCell;

    public static class OHListViewHolder extends RecyclerView.ViewHolder {
        private View ohFeedView;

        public OHListViewHolder(View v) {
            super(v);
            ohFeedView = v;
        }
    }

    public RecyclerViewAdapter(List<OHCell> officeHoursCell) {
        mOfficeHoursCell = officeHoursCell;
    }

    @Override
    public OHListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_oh_feed, parent, false);

        return new OHListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OHListViewHolder holder, int position) {
        final OHCell ohCell = mOfficeHoursCell.get(position);
        ((TextView) holder.ohFeedView.findViewById(R.id.textViewCourseName)).setText(ohCell.getCourseName());
        ((TextView) holder.ohFeedView.findViewById(R.id.textViewInstructor)).setText(ohCell.getInstructor());
        ((TextView) holder.ohFeedView.findViewById(R.id.textViewCourseNo)).setText(ohCell.getCourseNo());

        if (ohCell.getAvailability()) {
            ((TextView) holder.ohFeedView.findViewById(R.id.textViewAvailability)).setText("Available");
        } else {
            ((TextView) holder.ohFeedView.findViewById(R.id.textViewAvailability)).setText("Not Available");
        }

        holder.ohFeedView.setOnClickListener(new Listener(position, mOfficeHoursCell.get(position)));
    }

    class Listener implements View.OnClickListener {
        int position;
        OHCell ohCell;

        Listener(int position, OHCell ohCell) {
            this.position = position;
            this.ohCell = ohCell;
        }

        @Override
        public void onClick(View v) {
            if (ohCell.isFavorite()) {
                ohCell.setIsFavorite(false);
                Log.i("Favorite", ohCell.isFavorite() + "");
            } else {
                ohCell.setIsFavorite(true);
                Log.i("Favorite", ohCell.isFavorite() + "");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mOfficeHoursCell.size();
    }
}

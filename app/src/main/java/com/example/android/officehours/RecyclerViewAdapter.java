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
        public TextView courseName, instructorName, courseNo, availability;
        private View ohFeedView;

        public OHListViewHolder(View v) {
            super(v);
            ohFeedView = v;
            courseName = v.findViewById(R.id.textViewCourseName);
            instructorName = v.findViewById(R.id.textViewInstructor);
            courseNo = v.findViewById(R.id.textViewCourseNo);
            availability = v.findViewById(R.id.textViewAvailability);
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
        holder.courseName.setText(ohCell.getCourseName());
        holder.instructorName.setText(ohCell.getInstructor());
        holder.courseNo.setText(ohCell.getCourseNo());

        if (ohCell.getAvailability()) {
            holder.availability.setText("Available");
        } else {
            holder.availability.setText("Not Available");
        }

        holder.ohFeedView.setOnClickListener(new Listener(position, mOfficeHoursCell.get(position)));
    }

    //TODO: Implement a long press on an item so it can favorite it. Possibly refresh recyclerView too.
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

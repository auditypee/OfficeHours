package com.example.android.officehours;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Recycler View displays a scrolling list of elements based on large data sets.
 * The cells are displayed more easily using this View.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.OHListViewHolder> {
    private List<OHCell> mOfficeHoursCell;
    private OnItemLongClicked onClick;

    public static class OHListViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName, instructorName, courseNo, availability;
        public ImageView favoriteFlag;
        private View ohFeedView;

        public OHListViewHolder(View v) {
            super(v);
            ohFeedView = v.findViewById(R.id.cardViewItem);
            courseName = v.findViewById(R.id.textViewCourseName);
            instructorName = v.findViewById(R.id.textViewInstructor);
            courseNo = v.findViewById(R.id.textViewCourseNo);
            availability = v.findViewById(R.id.textViewAvailability);
            favoriteFlag = v.findViewById(R.id.favoriteFlag);
        }
    }

    /***********************************************************************************************
     *
     * @param officeHoursCell
     **********************************************************************************************/
    public RecyclerViewAdapter(List<OHCell> officeHoursCell) {
        mOfficeHoursCell = officeHoursCell;
    }

    @Override
    public OHListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_oh_feed, parent, false);

        return new OHListViewHolder(v);
    }

    /***********************************************************************************************
     *
     * @param holder
     * @param position
     **********************************************************************************************/
    @Override
    public void onBindViewHolder(OHListViewHolder holder, final int position) {
        final OHCell ohCell = mOfficeHoursCell.get(position);
        holder.courseName.setText(ohCell.getCourseName());
        holder.instructorName.setText(ohCell.getInstructor());
        holder.courseNo.setText(ohCell.getCourseNo());

        if (ohCell.getAvailability()) {
            holder.availability.setText("Available");
        } else {
            holder.availability.setText("Not Available");
        }

        if (ohCell.isFavorite()) {
            holder.favoriteFlag.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteFlag.setVisibility(View.INVISIBLE);
        }

        holder.ohFeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click", "Short Click");
            }
        });
        holder.ohFeedView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClick.onItemLongClick(position, mOfficeHoursCell.get(position));
                return true;
            }
        });
    }

    /***********************************************************************************************
     * Creates an interface that allows the functionality of a long click on the activity.
     * Passes on the OHCell and position clicked on.
     **********************************************************************************************/
    public interface OnItemLongClicked {
        void onItemLongClick(int position, OHCell ohCell);
    }

    public void setOnClick(OnItemLongClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return mOfficeHoursCell.size();
    }
}

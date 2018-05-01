package com.example.android.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.Menu.NONE;

/**
 * Created by Audi PC on 4/17/2018.
 */

public class OfficeHoursList extends AppCompatActivity implements RecyclerViewAdapter.OnItemLongClicked {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private String department;
    private String instructor;
    private DatabaseManager databaseManager;
    private ArrayList<OHCell> coursesList;
    private Parcelable mRecyclerViewState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_hours_list);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseManager = new DatabaseManager(this);

        getBundle();
        setTitle(instructor + "s for " + department);

        // gets the coursesList based on which instructor option was chosen
        if (instructor.equals("Teaching Assistant")) {
            coursesList = databaseManager.selectAllTA();
        } else {
            coursesList = databaseManager.selectAllInstructor();
        }
        // list is sorted by favorite by default
        sortCourses(coursesList, 0);

        updateList();
    }

    // Creates menu options to sort the data
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(NONE, 0, NONE, "Favorite (Default)");
        menu.add(NONE, 1, NONE, "Course Name");
        menu.add(NONE, 2, NONE, "Instructor");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sortCourses(coursesList, item.getItemId());
        updateList();

        return super.onOptionsItemSelected(item);
    }

    /***********************************************************************************************
     * Guarantees that the user will not their place in the list when updating the data.
     **********************************************************************************************/
    private void updateList() {
        mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();

        mAdapter = new RecyclerViewAdapter(coursesList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClick(this);

        mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewState);
    }

    /***********************************************************************************************
     * Takes the information from the previous activity (chosen department and instructor) and
     * initializes the two strings with it.
     **********************************************************************************************/
    private void getBundle() {
        Intent intent = getIntent();
        department = intent.getStringExtra("selected_department");
        instructor = intent.getStringExtra("selected_instructor");
    }

    /***********************************************************************************************
     * Updates the chosen course to be favorited or not.
     *
     * @param position Position of the item to be favorited
     * @param ohCell The holder for the item
     **********************************************************************************************/
    @Override
    public void onItemLongClick(int position, OHCell ohCell) {
        if (ohCell.isFavorite()) {
            ohCell.setIsFavorite(false);
            databaseManager.updateUnFavorite(ohCell.getCourseID());
        } else {
            ohCell.setIsFavorite(true);
            databaseManager.updateFavorite(ohCell.getCourseID());
        }
        updateList();
    }

    /***********************************************************************************************
     * Sorts the coursesList based on given option.
     *
     * @param ohCells a list to sort
     * @param option what to sort by
     *               0 - Favorite
     *               1 - Instructor Name
     *               2 - Course Name
     **********************************************************************************************/
    private void sortCourses(List<OHCell> ohCells, final int option) {
        Collections.sort(ohCells, new Comparator<OHCell>() {
            @Override
            public int compare(OHCell lhs, OHCell rhs) {
                int compared = 0;
                switch (option) {
                    case 0:
                        int lhsFav = lhs.isFavorite() ? 1 : 0;
                        int rhsFav = rhs.isFavorite() ? 1 : 0;
                        compared = rhsFav - lhsFav;
                        break;
                    case 1:
                        compared = lhs.getCourseName().toUpperCase().compareTo(rhs.getCourseName().toUpperCase());
                        break;
                    case 2:
                        compared = lhs.getInstructor().compareTo(rhs.getInstructor());
                        break;
                }

                return compared;
            }
        });
    }
}

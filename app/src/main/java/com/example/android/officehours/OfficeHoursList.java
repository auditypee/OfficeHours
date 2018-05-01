package com.example.android.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import java.util.ArrayList;

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

        updateList();
    }

    // TODO: Implement this
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void updateList() {
        mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        if (instructor.equals("Teaching Assistant")) {
            coursesList = databaseManager.selectAllTA();
        } else {
            coursesList = databaseManager.selectAllInstructor();
        }

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
}

package com.example.android.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Audi PC on 4/17/2018.
 */

public class OfficeHoursList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private String department;
    private String instructor;

    private DatabaseManager databaseManager;

    private ArrayList<OHCell> coursesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_hours_list);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseManager = new DatabaseManager(this);

        getBundle();
        setTitle(instructor + "s for " + department);

        if (instructor.equals("Teaching Assistant")) {
            coursesList = databaseManager.selectAllTA();
        } else {
            coursesList = databaseManager.selectAllInstructor();
        }

        mRecyclerView.setAdapter(new RecyclerViewAdapter(coursesList));
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
}

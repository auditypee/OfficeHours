package com.example.android.officehours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private Spinner departmentSpinner;
    private RadioGroup instructorRG;
    private RadioButton taRB;
    private RadioButton instructorRB;
    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        // initializes the spinner to the listed departments
        departmentSpinner = findViewById(R.id.spinnerDepartment);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.department_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);
        departmentSpinner.setSelection(0);

        // initializes the radio buttons
        instructorRG = findViewById(R.id.radioGroupTAI);
        instructorRB = findViewById(R.id.radioButtonInstructor);
        taRB = findViewById(R.id.radioButtonTA);

        // initializes the button
        checkButton = findViewById(R.id.buttonCheck);
        checkButton.setOnClickListener(mCheck);

        // creates the database
        DatabaseManager db = new DatabaseManager(this);
    }

    /***********************************************************************************************
     * If the check button is clicked, it reads the user's choices and initializes the next
     * activity based on those choices.
     **********************************************************************************************/
    public View.OnClickListener mCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkOfficeHours();
        }
    };

    private void checkOfficeHours() {
        String selectedDepartment = departmentSpinner.getSelectedItem().toString();
        String selectedRG = ((RadioButton) findViewById(instructorRG.getCheckedRadioButtonId())).getText().toString();

        Intent intent = new Intent(this, OfficeHoursList.class);
        intent.putExtra("selected_department", selectedDepartment);
        intent.putExtra("selected_instructor", selectedRG);
        startActivity(intent);
    }
}

package com.example.android.officehours;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Manages the Database and all of its content.
 * Creates the tables for the database and populates them.
 * Has calling methods to display some of its contents.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private Context mContext;
    private JSONObject myObj;
    private static final String DATABASENAME = "officeHoursDB";
    private static final int DBVERSION = 1;

    private static final String TBLINSTRUCTORS = "instructors";
    private static final String TBLINSTOH = "instOfficeHours";

    private static final String TBLCOURSES = "courses";

    private static final String TBLTA = "ta";
    private static final String TBLTAOH = "taOfficeHours";

    public DatabaseManager(Context context) {
        super(context, DATABASENAME, null, DBVERSION);
        mContext = context;

        // guarantees onCreate to be called
        SQLiteDatabase db = getWritableDatabase();
    }

    /***********************************************************************************************
     * Instantiates the database with the instructors, courses, and ta.
     * Downloads the JSON file when the app is first started.
     *
     * @param db
     **********************************************************************************************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        createInstructorTable(db);
        createInstOHTable(db);

        createCourseTable(db);

        createTATable(db);
        createTAOHTable(db);

        new DownloadJson().execute();
        Toast.makeText(mContext, "Database onCreate", Toast.LENGTH_SHORT).show();
    }


    /***********************************************************************************************
     * Creates the table to hold the instructor information.
     *
     * OFFICE HOURS TABLE
     * instructorID     PK  int
     * instName             text
     * instOfficeRoom       text
     * instEmail            text
     *
     * @param db
     **********************************************************************************************/
    public void createInstructorTable(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TBLINSTRUCTORS + " (";
        sqlCreate += "instructorID INTEGER PRIMARY KEY, ";
        sqlCreate += "instName TEXT, ";
        sqlCreate += "instOfficeRoom TEXT";
        sqlCreate += ");";

        db.execSQL(sqlCreate);
    }

    public void insertInstructorTable(int id, String name, String officeRoom) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlInsert = "INSERT INTO " + TBLINSTRUCTORS + " VALUES(";
        sqlInsert += id + ", '" +
                name + "', '" +
                officeRoom + "')";

        db.execSQL(sqlInsert);
        db.close();
    }


    /***********************************************************************************************
     * Creates the table to hold the office hours for a specific instructor.
     * References instructor as a foreign key.
     *
     * OFFICE HOURS TABLE
     * officeDay          text
     * officeHour         text
     * officeHour2        text
     * instructorID   FK  int
     *
     * @param db
     **********************************************************************************************/
    private void createInstOHTable(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TBLINSTOH + " (";
        sqlCreate += "instOfficeDay TEXT, ";
        sqlCreate += "instOfficeHour TEXT, ";
        sqlCreate += "instOfficeHour2 TEXT, "; // in case there's more than one office hour for that specific day
        sqlCreate += "instructorID INTEGER, ";
        sqlCreate += "FOREIGN KEY(instructorID) REFERENCES " + TBLINSTRUCTORS + "(instructorID)";
        sqlCreate += ");";

        db.execSQL(sqlCreate);
    }

    private void insertInstOHTable(String officeDay, String officeHour, String officeHour2, int instID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlInsert = "INSERT INTO " + TBLINSTOH + " VALUES(";
        sqlInsert += "'" +
                officeDay + "', '" +
                officeHour + "', '" +
                officeHour2 + "', " +
                instID + ")";

        db.execSQL(sqlInsert);
        db.close();
    }


    /***********************************************************************************************
     * Creates the table to hold the courses.
     * References instructor as a foreign key.
     *
     * COURSES TABLE
     * courseID       PK  int
     * courseName         text
     * courseRoom         text
     * courseDays         text
     * courseHours        text
     * courseLocation     text
     * favorite           boolean
     * instructorID   FK  int
     *
     * @param db
     **********************************************************************************************/
    private void createCourseTable(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TBLCOURSES + " (";
        sqlCreate += "courseID INTEGER PRIMARY KEY, ";
        sqlCreate += "courseName TEXT, ";
        sqlCreate += "courseNumber TEXT, ";
        sqlCreate += "courseRoom TEXT, ";
        sqlCreate += "courseDays TEXT, ";
        sqlCreate += "courseHours TEXT, ";
        sqlCreate += "favorite INT, ";
        sqlCreate += "instructorID INTEGER, ";
        sqlCreate += "FOREIGN KEY(instructorID) REFERENCES " + TBLINSTRUCTORS + "(instructorID)";
        sqlCreate += ");";

        db.execSQL(sqlCreate);
    }

    private void insertCourseTable(int id, String name, String number, String room,
                                   String days, String hours, int instructorID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlInsert = "INSERT INTO " + TBLCOURSES + " VALUES(";
        sqlInsert += id + ", '" +
                name + "', '" +
                number + "', '" +
                room + "', '" +
                days + "', '" +
                hours + "', " +
                0 + ", " +
                instructorID + ")";

        db.execSQL(sqlInsert);
        db.close();
    }


    /***********************************************************************************************
     * Creates the table to hold the teacher assistant information.
     * References instructor as a foreign key.
     *
     * OFFICE HOURS TABLE
     * taID             PK  int
     * taName               text
     * taOfficeRoom         text
     * courseID         FK  int
     *
     * @param db
     **********************************************************************************************/
    private void createTATable(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TBLTA + " (";
        sqlCreate += "taID INTEGER PRIMARY KEY, ";
        sqlCreate += "taName TEXT, ";
        sqlCreate += "taOfficeRoom TEXT, ";
        sqlCreate += "courseID INTEGER, ";
        sqlCreate += "FOREIGN KEY(courseID) REFERENCES " + TBLCOURSES + "(courseID)";
        sqlCreate += ");";

        db.execSQL(sqlCreate);
    }

    private void insertTATable(int id, String name, String officeRoom, int courseID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlInsert = "INSERT INTO " + TBLTA + " VALUES(";
        sqlInsert += id + ", '" +
                name + "', '" +
                officeRoom + "', " +
                courseID + ")";

        db.execSQL(sqlInsert);
        db.close();
    }

    /***********************************************************************************************
     * Creates the table to hold the office hours for a specific instructor.
     * References instructor as a foreign key.
     *
     * OFFICE HOURS TABLE
     * officeDay          text
     * officeHour         text
     * officeHour2        text
     * taID           FK  int
     *
     * @param db
     **********************************************************************************************/
    private void createTAOHTable(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TBLTAOH + " (";
        sqlCreate += "taOfficeDay TEXT, ";
        sqlCreate += "taOfficeHour TEXT, ";
        sqlCreate += "taOfficeHour2 TEXT, "; // in case there's more than one office hour for that specific day
        sqlCreate += "taID INTEGER, ";
        sqlCreate += "FOREIGN KEY(taID) REFERENCES " + TBLTA + "(taID)";
        sqlCreate += ");";

        db.execSQL(sqlCreate);
    }

    private void insertTAOHTable(String officeDay, String officeHour, String officeHour2, int taID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlInsert = "INSERT INTO " + TBLTAOH + " VALUES(";
        sqlInsert += "'" +
                officeDay + "', '" +
                officeHour + "', '" +
                officeHour2 + "', " +
                taID + ")";

        db.execSQL(sqlInsert);
        db.close();
    }

    /***********************************************************************************************
     * Returns a list of the courses with their respective TAs
     *
     * @return
     **********************************************************************************************/
    public ArrayList<OHCell> selectAllTA() {
        SQLiteDatabase db = this.getWritableDatabase();

        // take the data from the Courses table and TA table
        String sqlSelectCourses = "SELECT t.taID, c.courseID, c.courseName, c.courseNumber, t.taName " +
                "FROM " + TBLTA + " t, " + TBLCOURSES + " c " +
                "WHERE t.courseID = c.courseID";
        // take the data from the TAOfficeHours table
        String sqlOfficeDays = "SELECT o.taID, o.taOfficeDay || '#' || o.taOfficeHour || '|' || o.taOfficeHour2 " +
                "FROM " + TBLTAOH + " o";

        // parses through the queries
        Cursor cursor = db.rawQuery(sqlSelectCourses, null);
        Cursor cursor1 = db.rawQuery(sqlOfficeDays, null);
        ArrayList<OHCell> courseList = new ArrayList<>();

        // go through
        while (cursor.moveToNext()) {
            Integer i = Integer.parseInt(cursor.getString(0));
            ArrayList<String> officeDays = new ArrayList<>();

            // go through the rows of TA Office Days to add to an array
            while (cursor1.moveToNext()) {
                Integer n = Integer.parseInt(cursor1.getString(0));
                // if the OfficeDaysTable data has the same taID as the TA table, add it to the officeDays array
                if (i.equals(n))
                    officeDays.add(cursor1.getString(1));
            }
            // resets the cursor position for the officeDays
            cursor1.moveToFirst();

            // create an OHCell object that contains the data from the database
            OHCell currCell = new OHCell(
                    i,
                    (Integer.parseInt(cursor.getString(1))),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    officeDays);

            courseList.add(currCell);
        }
        // close cursor and db to prevent leaks
        cursor.close();
        cursor1.close();
        db.close();

        return courseList;
    }

    /***********************************************************************************************
     * Returns a list of the courses with their respective Instructor
     *
     * @return
     **********************************************************************************************/
    public ArrayList<OHCell> selectAllInstructor() {
        SQLiteDatabase db = this.getWritableDatabase();

        //int id, int courseID, String courseName, String courseNo, String instructor, ArrayList<String> officeDays
        // take the data from the Courses table and Instructor table
        String sqlSelectCourses = "SELECT i.instructorID, c.courseID, c.courseName, c.courseNumber, i.instName " +
                "FROM " + TBLINSTRUCTORS + " i, " + TBLCOURSES + " c " +
                "WHERE i.instructorID = c.instructorID";
        // take the data from the InstructorOfficeHours table
        String sqlOfficeDays = "SELECT o.instructorID, o.instOfficeDay || '#' || o.instOfficeHour || '|' || o.instOfficeHour2 " +
                "FROM " + TBLINSTOH + " o";

        // parses through the queries
        Cursor cursor = db.rawQuery(sqlSelectCourses, null);
        Cursor cursor1 = db.rawQuery(sqlOfficeDays, null);
        ArrayList<OHCell> courseList = new ArrayList<>();

        // go through the rows of Courses and their instructors
        while (cursor.moveToNext()) {
            Integer i = Integer.parseInt(cursor.getString(0));
            ArrayList<String> officeDays = new ArrayList<>();

            // go through the rows of Instructor Office Days to add to an array
            while (cursor1.moveToNext()) {
                Integer n = Integer.parseInt(cursor1.getString(0));
                // if the OfficeDaysTable data has the same instructorID as the Instructor table, add it to the officeDays array
                if (i.equals(n))
                    officeDays.add(cursor1.getString(1));
            }
            // resets the cursor position for the officeDays
            cursor1.moveToFirst();

            // create an OHCell object that contains the data from the database
            OHCell currCell = new OHCell(
                    i,
                    (Integer.parseInt(cursor.getString(1))),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    officeDays);

            courseList.add(currCell);
        }

        // close cursor and db to prevent leaks
        cursor.close();
        cursor1.close();
        db.close();

        return courseList;
    }

    public void selectAllItems(int courseID) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /***********************************************************************************************
     * Converts the JSON file, combined.json, into a String to be used for the database.
     *
     * @return The JSON file turned into a String
     **********************************************************************************************/
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("combined.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    /***********************************************************************************************
     * Downloads the JSON files from the assets folder and inserts them into the database.
     * Goes through instructor.json, course.json, and ta.json for their data.
     * Should be run only once after the app is installed because of the onCreate.
     **********************************************************************************************/
    private class DownloadJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                myObj = new JSONObject(loadJSONFromAsset());
            } catch (JSONException je) {
                je.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                // gets the array of instructors from the json file
                JSONArray instructorArray = myObj.getJSONArray("instructor");

                for (int i = 0; i < instructorArray.length(); i++) {
                    JSONObject tempObj = instructorArray.getJSONObject(i);

                    int instructorID = tempObj.getInt("instructorID");
                    String name = tempObj.getString("name");
                    String officeRoom = tempObj.getString("office_room");

                    // insert the data into the instructor table
                    insertInstructorTable(instructorID, name, officeRoom);

                    JSONArray officeDaysArray = tempObj.getJSONArray("office_days");
                    for (int j = 0; j < officeDaysArray.length(); j++) {
                        JSONObject officeDayObj = officeDaysArray.getJSONObject(j);

                        String officeDay = officeDayObj.getString("office_day");
                        String officeHours = officeDayObj.getString("office_hours");
                        String officeHoursExtra = officeDayObj.getString("office_hours_extra");

                        // insert the data into the office hours table with the instructorID as the foreign key
                        if (!officeDay.isEmpty())
                            insertInstOHTable(officeDay, officeHours, officeHoursExtra, instructorID);
                    }
                }

                // gets the array of courses from the json file
                JSONArray coursesArray = myObj.getJSONArray("course");

                for (int i = 0; i < coursesArray.length(); i++) {
                    JSONObject tempObj = coursesArray.getJSONObject(i);

                    int courseID = tempObj.getInt("courseID");
                    String courseName = tempObj.getString("name");
                    String courseNumber = tempObj.getString("number");
                    String courseRoom = tempObj.getString("room");
                    String courseDays = tempObj.getString("day");
                    String courseHours = tempObj.getString("hours");
                    int instructorID = tempObj.getInt("instructorID");

                    insertCourseTable(courseID, courseName, courseNumber, courseRoom,
                            courseDays, courseHours, instructorID);
                }

                // gets the array of tas from the json file
                JSONArray taArray = myObj.getJSONArray("ta");

                for (int i = 0; i < taArray.length(); i++) {
                    JSONObject tempObj = taArray.getJSONObject(i);

                    int taID = tempObj.getInt("taID");
                    String taName = tempObj.getString("name");
                    String taOfficeRoom = tempObj.getString("office_room");
                    int courseID = tempObj.getInt("courseID");

                    insertTATable(taID, taName, taOfficeRoom, courseID);

                    JSONArray officeDaysArray = tempObj.getJSONArray("office_days");
                    for (int j = 0; j < officeDaysArray.length(); j++) {
                        JSONObject officeDayObj = officeDaysArray.getJSONObject(j);

                        String officeDay = officeDayObj.getString("office_day");
                        String officeHours = officeDayObj.getString("office_hours");
                        String officeHoursExtra = officeDayObj.getString("office_hours_extra");

                        // insert the data into the office hours table with the instructorID as the foreign key
                        if (!officeDay.isEmpty())
                            insertTAOHTable(officeDay, officeHours, officeHoursExtra, taID);
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }
}

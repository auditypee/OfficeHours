package com.example.android.officehours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Audi PC on 4/22/2018.
 */

public class OHCell {
    private int id;
    private int courseID;
    private String courseName;
    private String courseNo;
    private String instructor;
    private boolean favorite = false;
    private ArrayList<String> officeDays;

    private static final String TAG = "OHCell";

    public OHCell(int id, int courseID, String courseName, String courseNo, String instructor, int favorite, ArrayList<String> officeDays) {
        this.id = id;
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseNo = courseNo;
        this.instructor = instructor;
        this.officeDays = officeDays;

        if (favorite != 0)
            this.favorite = true;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getOfficeDays() {
        String res = "";

        for (String s : officeDays)
            res += s + ", ";
        return res;
    }

    public void setIsFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /***********************************************************************************************
     * Goes through the array of the officeDays and checks if one of those days/hours are true
     *
     * @return Boolean of if current datetime coincides with the officeDays and officeHours
     **********************************************************************************************/
    public boolean getAvailability() {
        boolean isAvailable = false;

        for (int i = 0; i < officeDays.size(); i++) {
            if (checkAvailability(officeDays.get(i))) {
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }

    /***********************************************************************************************
     * Parses through a given string to get the days and start and end times.
     *
     * @param dayHour Will have the format: day#HH:mma- end:time|start:time - end:time
     * @return Boolean of if current time is inside the time frame
     **********************************************************************************************/
    public boolean checkAvailability(String dayHour) {
        String day = dayHour.substring(0, dayHour.indexOf("#")).trim(); // gets the day

        String officeHour = dayHour.substring(dayHour.indexOf('#') + 1, dayHour.indexOf('|')).trim(); // gets the first office hour
        String start = officeHour.substring(0, officeHour.indexOf(" -")); // gets start time of first office hour
        String end = officeHour.substring(officeHour.indexOf("- ") + 1); // gets end time of first office hour
        boolean oh1 = compareTimes(day, start, end); // checks if within time range and day

        officeHour = dayHour.substring(dayHour.indexOf("|") + 1).trim();

        // gets the second office hour (if any) -- reused variables on this
        boolean oh2 = false;
        if (!officeHour.isEmpty()) {
            start = officeHour.substring(0, officeHour.indexOf(" -")); // gets start time of second office hour
            end = officeHour.substring(officeHour.indexOf("- ") + 1); // gets end time of second office hour
            oh2 = compareTimes(day, start, end); // checks if within time range and day
        }

        // if either time range holds the current time and date
        return oh1 || oh2;
    }

    /***********************************************************************************************
     * Given a time range, check if the current time is within that range.
     *
     * @param day The current day of the week (officeDay)
     * @param start The start of the time range (officeHour)
     * @param end The end of the time range (officeHour)
     * @return Boolean on whether or not if current time is within the time range
     **********************************************************************************************/
    private boolean compareTimes(String day, String start, String end) {
        boolean isBetween = false;

        try {
            // converts given String to 24hour format
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.US);
            Date time1 = timeFormat.parse(start);
            Date time2 = timeFormat.parse(end);

            // gets the current time and day of the week
            Calendar now = Calendar.getInstance();

            // converts am and pm to readable format
            String ampm;
            if (now.get(Calendar.AM_PM) == Calendar.PM)
                ampm = "pm";
            else
                ampm = "am";

            // takes the current time
            String d = now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE) + ampm;
            Date date = timeFormat.parse(d);

            // takes the current day of the week
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
            String dayOfWeek = dateFormat.format(now.getTime());

            // checks if the current time is between the time range and if current day coincides with officeDay
            if (time1.before(date) && time2.after(date) && day.equals(dayOfWeek))
                isBetween = true;
        } catch (ParseException pe) {
            // to catch parsing errors
            pe.printStackTrace();
        }

        return isBetween;
    }


}

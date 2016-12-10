package com.example.android.justjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;

import java.util.concurrent.TimeUnit;

import static com.example.android.justjava.DisplayClass.EXTRA_MESSAGE_THREE;

public class DisplayTask extends AppCompatActivity {

    String taskname;

    String classname;

    TextView textView;

    TextView elapsedTimeView;

    TextView predictedTimeView;

    Button startButton;

    Button stopButton;

    TaskDbHelper mDbHelper;

    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);

        //Receives intent from MainActivity and sets the classname equal to the received EXTRA_MESSAGE
        Intent intent = getIntent();

        //Finds the text view that displays classname
        textView = (TextView) findViewById(R.id.info);
        //sets the classname to the string recieved from mainActivity
        //taskname = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        String[] taskClassArray = intent.getStringArrayExtra(EXTRA_MESSAGE_THREE);

        taskname = taskClassArray[0];

        classname = taskClassArray[1];

        //sets text to taskname
        textView.setText(classname + ": " + taskname);

        mDbHelper = new TaskDbHelper(this);

        elapsedTimeView = (TextView) findViewById(R.id.time_spent);

        predictedTimeView = (TextView) findViewById(R.id.predicted);

        startButton = (Button) findViewById(R.id.startButton);

        stopButton = (Button) findViewById(R.id.stopButton);

        displayData();

        if (checkActive())
        {
            startButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            stopButton.setVisibility(View.INVISIBLE);
        }

        //stopButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //display all classes upon starting the activity
        //displayData();
    }

    public void displayData() {
        //class getAllClasses from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME,
                TimerContract.TimerEntry.COLUMN_PREDICTED_TIME};


        String selection = TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ? and " +
                TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ?";

        String[] selectionArgs = {taskname, classname};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,    // The table to query
                projection,                             // Returns all columns
                selection,                              // filter the class and task name if the class and task name are not equal to (taskname and class)
                selectionArgs,                          //
                null,                                   // Don't group the rows
                null,                                   // Don't filter by row groups
                null);                                  // The sort order


        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            return;
        }

        /*

        The cursor now holds all rows with class name and task equal to those specified.


         */

        //moves to first row
        cursor.moveToFirst();


        int elapsedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME);

        int predictedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_PREDICTED_TIME);

        //gets the value from elapsed time as long
        long elapsedTime = cursor.getLong(elapsedTimeColumnIndex);

        double predictedTime = cursor.getDouble(predictedTimeColumnIndex);

        double seconds = (double)elapsedTime;

        seconds = seconds / 3600;

        elapsedTimeView.setText(Double.toString(seconds) + " hours");

        predictedTimeView.setText(Double.toString(predictedTime) + " hours");
        //(Double.toString(seconds) + " hours"
    }

    public void changeStartTime(View view)
    {
        //changes the time in the database
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();

        values.put(TimerContract.TimerEntry.COLUMN_START_TIME, timeSeconds);
        values.put(TimerContract.TimerEntry.COLUMN_ACTIVE, "ACTIVE/STARTED");

        // Which row to update, based on the title
        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";
        String[] selectionArgs = {classname, taskname};

        int count = db.update(
                TimerContract.TimerEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        startButton.setVisibility(View.INVISIBLE);

        stopButton.setVisibility(View.VISIBLE);
    }

    public void changeStopTime(View view)
    {
        //changes the time in the database
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                //TimerEntry._ID,
                //TimerEntry.COLUMN_CLASS_NAME,
                //TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME };

        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {classname, taskname};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause////
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //figures out the index for the column
        cursor.moveToFirst();

        int startTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_START_TIME);
        int elapsedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME);

        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        long startTime = cursor.getLong(startTimeColumnIndex);

        long elapsedTime = cursor.getLong(elapsedTimeColumnIndex);


        elapsedTime += (timeSeconds - startTime);

        // New value for one column
        ContentValues values = new ContentValues();

        values.put(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);
        values.put(TimerContract.TimerEntry.COLUMN_ACTIVE, "ACTIVE/STOPPED");

        // Which row to update, based on the title
        //String newselection = TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
        // TimerEntry.COLUMN_TASK_NAME + " = ?";

        // String[] selectionArgs = {Class, Task};

        int count = db.update(
                TimerContract.TimerEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        startButton.setVisibility(View.VISIBLE);

        stopButton.setVisibility(View.INVISIBLE);

        displayData();
    }

    public void finishTask(View view)
    {
        if (checkActive())
        {
            changeStopTime(view);
        }
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                //TimerEntry._ID,
                //TimerEntry.COLUMN_CLASS_NAME,
                //TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME };

        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {classname, taskname};

        ContentValues values = new ContentValues();

        values.put(TimerContract.TimerEntry.COLUMN_ACTIVE, "INACTIVE");

        int count = db.update(
                TimerContract.TimerEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        finish();
    }

    public void deleteTask(View view)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String whereClause = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] whereArgs = new String[] { classname, taskname };

        db.delete(TimerContract.TimerEntry.TABLE_NAME, whereClause, whereArgs);

        finish();
    }

    public boolean checkActive() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                //TimerEntry._ID,
                //TimerEntry.COLUMN_CLASS_NAME,
                //TimerEntry.COLUMN_TASK_NAME,
                //TimerContract.TimerEntry.COLUMN_START_TIME,
                //TimerContract.TimerEntry.COLUMN_ELAPSED_TIME
                TimerContract.TimerEntry.COLUMN_ACTIVE};

        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {classname, taskname};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause////
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //figures out the index for the column
        cursor.moveToFirst();

        int activeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_ACTIVE);

        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        String active = cursor.getString(activeColumnIndex);

        if (active.equals("ACTIVE/STARTED"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}

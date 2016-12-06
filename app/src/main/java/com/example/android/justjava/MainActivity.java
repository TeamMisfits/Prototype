package com.example.android.justjava;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.justjava.data.TimerContract.TimerEntry;
import com.example.android.justjava.data.TaskDbHelper;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    // List<String> classes = new ArrayList<String>();

    //Enter the classes name
    private EditText mClassNameEditText;

    //Enter Task Name
    private EditText mTaskNameEditText;

   // private EditText mStartTimeEditText;

   // private EditText mEndTimeEditText;

    //Enter lecture name
    private EditText mLectureNameEditText;

    /****************************** ON CREATE **************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Find all relevant views that we will need to read user input from


        /*
        mClassNameEditText = (EditText) findViewById(R.id.edit_class_name);
        mTaskNameEditText = (EditText) findViewById(R.id.edit_task_name);
        //mStartTimeEditText = (EditText) findViewById(R.id.edit_start_time);
        //mEndTimeEditText = (EditText) findViewById(R.id.edit_end_time);
        mLectureNameEditText = (EditText) findViewById(R.id.edit_lecture_name);
        */
    }

    public void createClass(View view) {

        String classNameString = mClassNameEditText.getText().toString().trim();

        String taskNameString = mTaskNameEditText.getText().toString().trim();

       // String startTimeString = mStartTimeEditText.getText().toString().trim();

       // String endTimeString = mEndTimeEditText.getText().toString().trim();

        // Create database helper
        TaskDbHelper mDbHelper = new TaskDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        long startTime = 0;
        long elapsedTime = 0;


        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(TimerEntry.COLUMN_CLASS_NAME, classNameString);
        values.put(TimerEntry.COLUMN_TASK_NAME, taskNameString);
        values.put(TimerEntry.COLUMN_START_TIME, startTime);
        values.put(TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(TimerEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving task", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Task saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

/*
    public void submitLecture(View view) {

        String lectureNameString = mLectureNameEditText.getText().toString().trim();

        String taskNameString = "Lecture";

        long startTime = 0;

        long elapsedTime = 0;

        // Create database helper
        TaskDbHelper mDbHelper = new TaskDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(TimerEntry.COLUMN_CLASS_NAME, lectureNameString);
        values.put(TimerEntry.COLUMN_TASK_NAME, taskNameString);
        values.put(TimerEntry.COLUMN_START_TIME, startTime);
        values.put(TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(TimerEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving lecture", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Lecture saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    public void switchActivity(View view) {

        Intent intent = new Intent(this, Main2Activity.class);

        startActivity(intent);
    }
    */
}
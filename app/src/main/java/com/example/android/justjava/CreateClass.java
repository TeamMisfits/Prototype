package com.example.android.justjava;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;

public class CreateClass extends AppCompatActivity {

    //defines the edit text view
    private EditText mClassNameEditText;
    //defines the database helper
    TaskDbHelper mDbHelper;

    //Elements to be stored
    String classNameString;
    String taskNameString;
    long startTime;
    long elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        //identifies the edit text view
        mClassNameEditText = (EditText) findViewById(R.id.getClassName);

        // Create database helper
        mDbHelper = new TaskDbHelper(this);
    }

    public void saveClass(View view) {

        //Initialize the info to be placed in the new class
        classNameString = mClassNameEditText.getText().toString().trim();
        taskNameString = "Lecture";  //taskNameString as null means it is a class
        startTime = 0;
        elapsedTime = 0;

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and attributes from the editor are the values.
        ContentValues values = new ContentValues();

        //places values into the database
        values.put(TimerContract.TimerEntry.COLUMN_CLASS_NAME, classNameString);
        values.put(TimerContract.TimerEntry.COLUMN_TASK_NAME, taskNameString);
        values.put(TimerContract.TimerEntry.COLUMN_START_TIME, startTime);
        values.put(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);

        // Insert a new row in the database, returning the ID of that new row.
        long newRowId = db.insert(TimerContract.TimerEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Created Class: " + classNameString, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}

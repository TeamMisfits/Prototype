package com.example.android.justjava;

import android.content.ContentValues;
import android.database.Cursor;
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
    double projectedTime;
    String active;

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
        taskNameString = "CLASS";  //taskNameString as null means it is a class
        startTime = 0;
        elapsedTime = 0;
        projectedTime = 0;
        active = "ACTIVE";

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and attributes from the editor are the values.
        ContentValues values = new ContentValues();

        //Error Checks Input
        boolean result = isDuplicateClass(classNameString);

        if (!result) {
            //places values into the database
            values.put(TimerContract.TimerEntry.COLUMN_CLASS_NAME, classNameString);
            values.put(TimerContract.TimerEntry.COLUMN_TASK_NAME, taskNameString);
            values.put(TimerContract.TimerEntry.COLUMN_START_TIME, startTime);
            values.put(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);
            values.put(TimerContract.TimerEntry.COLUMN_PREDICTED_TIME, projectedTime);
            values.put(TimerContract.TimerEntry.COLUMN_ACTIVE, active);


            // Insert a new row in the database, returning the ID of that new row.
            long newRowId = db.insert(TimerContract.TimerEntry.TABLE_NAME, null, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newRowId == -1) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
            }
            else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, "Created Class: " + classNameString, Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        else if (result){
            Toast.makeText(this,  classNameString + " Already Exists!", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isDuplicateClass(String inputClassName) {


        //gets a SQLite Database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //determines which columns to return
        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME
        };

        //which rows to return
        String selection = TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        //arguments to filter rows by
        String[] selectionArgs = {"CLASS"};

        //querys the database using the parameters defined above
        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,    // The table to query
                projection,                             // The columns to return
                selection,                              // The columns for the WHERE clause////
                selectionArgs,                          // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by row groups
                null);                                  // The sort order

        //Error checking
        if (cursor == null)
        {
            return false;
        }
        if (cursor.getCount() == 0)
        {
            return false;
        }

        //moves cursor to firstrow
        cursor.moveToFirst();

        //indexes specified column
        int classColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_CLASS_NAME);

        //for all the rows, if the inputClassName equals a name of an existing class return false.
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String className = cursor.getString(classColumnIndex);

            if(inputClassName.equals(className))
                return true;
        }

        return false;
    }

}

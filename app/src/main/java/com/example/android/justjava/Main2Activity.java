package com.example.android.justjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract.TimerEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    //helper allowing access to data base
    private TaskDbHelper mDbHelper;

    public static Map<String, List<String>> ClassTaskMap;

    /**
     * EditText field to enter the class's name
     */
    private EditText mClassNameEditText;

    /**
     * EditText field to enter the task's name
     */
    private EditText mTaskNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //instantiate the subclass of SQLIteOpenHelper for current activity
        mDbHelper = new TaskDbHelper(this);

        ClassTaskMap = new HashMap<String, List<String>>();

        mClassNameEditText = (EditText) findViewById(R.id.choose_class_name);

        mTaskNameEditText = (EditText) findViewById(R.id.choose_task_name);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getDatabaseInfo();
    }


    //displays information on the screen
    private void getDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TimerEntry._ID,
                TimerEntry.COLUMN_CLASS_NAME,
                TimerEntry.COLUMN_TASK_NAME,
                TimerEntry.COLUMN_START_TIME,
                TimerEntry.COLUMN_ELAPSED_TIME };

        // Perform a query on the pets table
        Cursor cursor = db.query(
                TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //figures out the index for the column
        int classnameColumnIndex = cursor.getColumnIndex(TimerEntry.COLUMN_CLASS_NAME);
        int tasknameColumnIndex = cursor.getColumnIndex(TimerEntry.COLUMN_TASK_NAME);

        // Iterate through all the returned rows in the cursor
        while (cursor.moveToNext()) {
            // Use that index to extract the String or Int value of the word
            // at the current row the cursor is on.
            String currentClassName = cursor.getString(classnameColumnIndex);
            String currentTaskName = cursor.getString(tasknameColumnIndex);

            if (ClassTaskMap.containsKey(currentClassName))
            {
                ClassTaskMap.get(currentClassName).add(currentTaskName);
            }
            else
            {
                ClassTaskMap.put(currentClassName, new ArrayList<String>());
            }
        }

    }

    public void startTime(View view) {

        String classNameString = mClassNameEditText.getText().toString().trim();

        String taskNameString = mTaskNameEditText.getText().toString().trim();

        if (!ClassTaskMap.containsKey(classNameString) || !ClassTaskMap.get(classNameString).contains(taskNameString))
        {
            Toast.makeText(this, "Class or Task is not valid", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Successfully started time", Toast.LENGTH_SHORT).show();

            changeStartTime(classNameString, taskNameString);
        }

    }

    private void changeStartTime(String Class, String Task)
    {
        //changes the time in the database
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();

        values.put(TimerEntry.COLUMN_START_TIME, timeSeconds);

        // Which row to update, based on the title
        String selection = TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerEntry.COLUMN_TASK_NAME + " = ?";
        String[] selectionArgs = {Class, Task};

        int count = db.update(
                TimerEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public void stopTime(View view) {

        String classNameString = mClassNameEditText.getText().toString().trim();

        String taskNameString = mTaskNameEditText.getText().toString().trim();

        if (!ClassTaskMap.containsKey(classNameString) || !ClassTaskMap.get(classNameString).contains(taskNameString))
        {
            Toast.makeText(this, "Class or Task is not valid", Toast.LENGTH_SHORT).show();
        }
        else
        {
            changeStopTime(classNameString, taskNameString);

            Toast.makeText(this, "Successfully stopped time", Toast.LENGTH_SHORT).show();
        }

    }

    private void changeStopTime(String Class, String Task)
    {
        //changes the time in the database
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
               //TimerEntry._ID,
                //TimerEntry.COLUMN_CLASS_NAME,
                //TimerEntry.COLUMN_TASK_NAME,
                TimerEntry.COLUMN_START_TIME,
                TimerEntry.COLUMN_ELAPSED_TIME };

        String selection = TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {Class, Task};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause////
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //figures out the index for the column
        cursor.moveToFirst();

        int startTimeColumnIndex = cursor.getColumnIndex(TimerEntry.COLUMN_START_TIME);
        int elapsedTimeColumnIndex = cursor.getColumnIndex(TimerEntry.COLUMN_ELAPSED_TIME);

        // Use that index to extract the String or Int value of the word
        // at the current row the cursor is on.
        long startTime = cursor.getLong(startTimeColumnIndex);

        long elapsedTime = cursor.getLong(elapsedTimeColumnIndex);


        elapsedTime += (timeSeconds - startTime);

        // New value for one column
        ContentValues values = new ContentValues();

        values.put(TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);

        // Which row to update, based on the title
        //String newselection = TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
               // TimerEntry.COLUMN_TASK_NAME + " = ?";

       // String[] selectionArgs = {Class, Task};

        int count = db.update(
                TimerEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void GoToStats(View view) {

        Intent intent = new Intent(this, Main3Activity.class);

        startActivity(intent);
    }

}

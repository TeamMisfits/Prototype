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

    private EditText mClassNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);


        mClassNameEditText = (EditText) findViewById(R.id.getClassName);

    }

    public void saveClass(View view) {

        String lectureNameString = mClassNameEditText.getText().toString().trim();

        String taskNameString = null;

        long startTime = 0;

        long elapsedTime = 0;

        // Create database helper
        TaskDbHelper mDbHelper = new TaskDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(TimerContract.TimerEntry.COLUMN_CLASS_NAME, lectureNameString);
        values.put(TimerContract.TimerEntry.COLUMN_TASK_NAME, taskNameString);
        values.put(TimerContract.TimerEntry.COLUMN_START_TIME, startTime);
        values.put(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME, elapsedTime);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(TimerContract.TimerEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }


        finish();
    }

}

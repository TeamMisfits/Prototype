package com.example.android.justjava;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main3);

        mDbHelper = new TaskDbHelper(this);

        ClassTaskMap = Main2Activity.ClassTaskMap;

        mClassNameEditText = (EditText) findViewById(R.id.write_class_name);

        mTaskNameEditText = (EditText) findViewById(R.id.write_task_name);

    }

    public void viewStats(View view) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String classNameString = mClassNameEditText.getText().toString().trim();

        String taskNameString = mTaskNameEditText.getText().toString().trim();

        if (!ClassTaskMap.containsKey(classNameString) || !ClassTaskMap.get(classNameString).contains(taskNameString))
        {
            Toast.makeText(this, "Class or Task is not valid", Toast.LENGTH_SHORT).show();
        }
        else {

            String[] projection = {
                    //TimerEntry._ID,
                    //TimerEntry.COLUMN_CLASS_NAME,
                    //TimerEntry.COLUMN_TASK_NAME,
                    // TimerContract.TimerEntry.COLUMN_START_TIME,
                    TimerContract.TimerEntry.COLUMN_ELAPSED_TIME};

            String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and " +
                    TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

            String[] selectionArgs = {classNameString, taskNameString};
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

            // int startTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_START_TIME);
            int elapsedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME);

            long elapsedTime = cursor.getLong(elapsedTimeColumnIndex);

           double seconds = (double)elapsedTime;

            seconds /= 3600;

            final TextView textViewToChange = (TextView) findViewById(R.id.statView);

            textViewToChange.setText(Double.toString(seconds) + " hours");

            textViewToChange.setVisibility(View.VISIBLE);
        }

    }
}

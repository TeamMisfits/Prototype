package com.example.android.justjava;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;

public class DisplayTask extends AppCompatActivity {

    String taskname;

    TextView textView;

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
        taskname = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //sets text to taskname
        textView.setText(taskname);

        mDbHelper = new TaskDbHelper(this);
    }


    public void displayData() {
        //class getAllClasses from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME};


        String selection = TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {taskname};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause////
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order


        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            return;
        }



        //cursor.moveToFirst();


        /*
        //Places results in a string
        String[] columns = new String[]{
                TimerContract.TimerEntry.COLUMN_START_TIME,
        };

        //binds the data to the text view that holds the class name
        int[] boundTo = new int[]{
                R.id.class_name,
        };


        //displays in listView using simpleCursorAdapter
        simpleCursorAdapter = new android.widget.SimpleCursorAdapter(this,
                R.layout.layout,
                cursor,
                columns,
                boundTo,
                0);
        listView.setAdapter(simpleCursorAdapter);
        */
    }

}

package com.example.android.justjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;



import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract.TimerEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    //helper allowing access to data base
    private TaskDbHelper mDbHelper;

    private SimpleCursorAdapter simpleCursorAdapter;

    ListView listView;

    //public static Map<String, List<String>> ClassTaskMap;








    /******************************
     * ON CREATE
     **************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDbHelper = new TaskDbHelper(this);

        //ClassTaskMap = new HashMap<String, List<String>>();

        //mClassNameEditText = (EditText) findViewById(R.id.choose_class_name);
        //mTaskNameEditText = (EditText) findViewById(R.id.choose_task_name);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        mDbHelper = new TaskDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayClasses();
    }

    public void createClass(View view) {

        Intent intent = new Intent(this, CreateClass.class);

        startActivity(intent);

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
                TimerEntry.COLUMN_ELAPSED_TIME};

        // Perform a query on the table
        Cursor cursor = db.query(
                TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order


        /*
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
         */


    }

    public void selectClass() {
        //TODO 1
    }

    private void displayClasses() {

            Cursor cursor = mDbHelper.getAllClasses();
            if (cursor == null)
            {
                return;
            }
            if (cursor.getCount() == 0)
            {
                return;
            }
            String[] columns = new String[] {
                    TimerEntry.COLUMN_CLASS_NAME,
            };
            int[] boundTo = new int[] {
                    R.id.class_name,
            };
            simpleCursorAdapter = new android.widget.SimpleCursorAdapter(this,
                    R.layout.layout,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listView.setAdapter(simpleCursorAdapter);
    }


}

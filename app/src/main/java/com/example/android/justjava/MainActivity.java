package com.example.android.justjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;



import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;
import com.example.android.justjava.data.TimerContract.TimerEntry;


public class MainActivity extends AppCompatActivity {

    //Message that is passed when clicking a listView item. This is classname
    public final static String EXTRA_MESSAGE = "com.example.androidexample";

    //Database helper
    private TaskDbHelper mDbHelper;

    //cursor adapter, allows us to put database info into listview
    private SimpleCursorAdapter simpleCursorAdapter;

    //instantiates list view
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //identifies the listView from XML File
        listView = (ListView) findViewById(R.id.list);
        //creates a new database helper
        mDbHelper = new TaskDbHelper(this);

        //onClickListener used to see if the user pressed a list item
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {

                        //Create intent to send to DisplayClass
                        Intent intent = new Intent(MainActivity.this, DisplayClass.class);

                        //Finds the text that holds the class name in the listView
                        TextView textView = (TextView) view.findViewById(R.id.class_name);
                        String text = textView.getText().toString();

                        //adds it to the intent and sends
                        intent.putExtra(EXTRA_MESSAGE, text);
                        startActivity(intent);
                    }
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();

        //display all classes upon starting the activity
        displayClasses();
    }

    public void createClass(View view) {

        //simple enough. go to create class task
        Intent intent = new Intent(this, CreateClass.class);
        startActivity(intent);

    }

    public void displayData(View view){

        //simple enough. go to display data task
        Intent intent = new Intent(this, DisplayData.class);
        startActivity(intent);
    }

    private void displayClasses() {

            //gets a SQLite Database
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            //determines which columns to return
            String[] projection = {
                    TimerContract.TimerEntry._ID,
                    TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                    TimerContract.TimerEntry.COLUMN_TASK_NAME};
                    //TimerContract.TimerEntry.COLUMN_START_TIME,
                    //TimerContract.TimerEntry.COLUMN_ELAPSED_TIME};

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
                return;
            }
            if (cursor.getCount() == 0)
            {
                return;
            }

            //which column from the cursor will be used for the adapter
            String[] columns = new String[] {TimerEntry.COLUMN_CLASS_NAME,};

            //which view to bind the data to for the adapter
            int[] boundTo = new int[] {R.id.class_name,};

            //displays in listView using simpleCursorAdapter
            simpleCursorAdapter = new android.widget.SimpleCursorAdapter(this,
                    R.layout.layout,
                    cursor,
                    columns,
                    boundTo,
                    0);

            listView.setAdapter(simpleCursorAdapter);
    }
}

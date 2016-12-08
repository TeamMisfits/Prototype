package com.example.android.justjava;

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
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;

public class DisplayClass extends AppCompatActivity {

    //Sends classname to createTask so that it knows what to set as classname
    public final static String EXTRA_MESSAGE_TWO = "com.example.androidexample";
    //sends taskname to displayTask so that it can associate information with that row
    public final static String EXTRA_MESSAGE_THREE = "com.example.androidexample";

    //cursor adapter, allows us to put database info into listview
    private SimpleCursorAdapter simpleCursorAdapter;

    //Instantiates text view to display class name
    TextView textView;
    //Instantiates string to hold classname
    String classname;
    //Database helper
    TaskDbHelper mDbHelper;
    //instantiates list view
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class);

        //Receives intent from MainActivity and sets the classname equal to the received EXTRA_MESSAGE
        Intent intent = getIntent();

        //finds the list where tasks will be displayed
        listView = (ListView) findViewById(R.id.list);
        //Create a new TaskDbHelper
        mDbHelper = new TaskDbHelper(this);
        //finds the text view where the class name will be displayed
        textView = (TextView) findViewById(R.id.class_title);

        //recieves string from MainActivity and sets to variable classname
        classname = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //sets text of the found textview to classname
        textView.setText(classname);


        //onClickListener used to see if the user pressed a list item
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {

                        Intent intent = new Intent(DisplayClass.this, DisplayTask.class);

                        String selectedClass = listView.getItemAtPosition(position).toString();

                        //Finds the text that holds the task name in the listView
                        TextView textView = (TextView) view.findViewById(R.id.class_name);
                        String text = textView.getText().toString();
                        intent.putExtra(EXTRA_MESSAGE_THREE, text);
                        startActivity(intent);
                    }
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        //display all classes upon starting the activity
        displayTasks();
    }

    public void deleteClass(View view){

        //Calls deleteClass function from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String whereClause = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ?";

        String[] whereArgs = new String[] { classname };

        db.delete(TimerContract.TimerEntry.TABLE_NAME, whereClause, whereArgs);

        //Outputs a toast message
        Toast.makeText(this, classname + " Deleted", Toast.LENGTH_SHORT).show();
        //Returns
        finish();
    }


    public void createTask(View view){

        Intent intent = new Intent(DisplayClass.this, CreateTask.class);

        //pass the classname so that a task can also have the class name in its row
        intent.putExtra(EXTRA_MESSAGE_TWO, classname);
        startActivity(intent);
    }


    private void displayTasks() {

        //class getAllClasses from TaskDBHelper
       SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME};


        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ?";

        String[] selectionArgs = {classname};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause////
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //Error checking
        if (cursor == null)
        {
            return;
        }
        if (cursor.getCount() == 0)
        {
            return;
        }

        //cursor.moveToFirst();

        //Places results in a string
        String[] columns = new String[] {
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
        };

        //binds the data to the text view that holds the class name
        int[] boundTo = new int[] {
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
    }


}

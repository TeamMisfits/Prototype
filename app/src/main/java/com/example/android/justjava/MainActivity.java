package com.example.android.justjava;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;



import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract.TimerEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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

        //identifies the listView
        listView = (ListView) findViewById(R.id.list);
        //creates a new database helper
        mDbHelper = new TaskDbHelper(this);

        //onClickListener used to see if the user pressed a list item
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {

                        Intent intent = new Intent(MainActivity.this, DisplayClass.class);

                        String selectedClass = listView.getItemAtPosition(position).toString();

                        //Finds the text that holds the class name in the listView
                        TextView textView = (TextView) view.findViewById(R.id.class_name);
                        String text = textView.getText().toString();
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

    private void displayClasses() {

            //class getAllClasses from TaskDBHelper
            Cursor cursor = mDbHelper.getAllItems();

            //Error checking
            if (cursor == null)
            {
                return;
            }
            if (cursor.getCount() == 0)
            {
                return;
            }

            //Places results in a string
            String[] columns = new String[] {
                    TimerEntry.COLUMN_CLASS_NAME,
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

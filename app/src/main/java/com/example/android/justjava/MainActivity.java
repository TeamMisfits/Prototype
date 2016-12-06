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

    public final static String EXTRA_MESSAGE = "com.example.androidexample";

    //helper allowing access to data base
    private TaskDbHelper mDbHelper;

    private SimpleCursorAdapter simpleCursorAdapter;

    ListView listView;



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

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    //onClickListener used to see if the user pressed a list item
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {

                        Intent intent = new Intent(MainActivity.this, DisplayClass.class);

                        String selectedClass = listView.getItemAtPosition(position).toString();


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
        displayClasses();
    }

    public void createClass(View view) {

        Intent intent = new Intent(this, CreateClass.class);

        startActivity(intent);

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

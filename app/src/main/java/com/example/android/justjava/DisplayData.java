package com.example.android.justjava;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayData extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.androidexample";

    private String[] yData;

    private String[] xData;

    PieChart pieChart;

    TaskDbHelper mDbHelper;

    SimpleCursorAdapter simpleCursorAdapter;

    Spinner spin;

    Button button;

    Cursor cursor;

    int selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        final Intent intent = getIntent();

        spin = (Spinner) findViewById(R.id.spinner);
        mDbHelper = new TaskDbHelper(this);

        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleAlpha(0);

        setupSpinner();
        getClassNamesData();
        getElapsedTimeData();
        addDataSet(pieChart);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                //Retrieves information for the toast display
                String classSubstring = h.toString().substring(14,15);
                int pos1 = Integer.parseInt(classSubstring);

                for (int i = 0; i < xData.length; i++) {
                    if (xData[i].equals(classSubstring)) {
                        pos1 = i;
                        break;
                    }
                }

                String className = xData[pos1];
                String hours = yData[pos1];
                Toast.makeText(DisplayData.this, "Class: " + className + "\n" + "Hours: " + hours, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //change variable that says which data set to go to on ClassData
                        selection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        button = (Button) findViewById(R.id.view_button);
        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                try {
                    Intent intent = new Intent(getApplicationContext(), ClassData.class);
                    String className = xData[selection];
                    intent.putExtra(EXTRA_MESSAGE, className);
                    startActivity(intent);
                }
                catch (Exception ex){
                    return;
                }
            }
        });

    }

    private void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        //Error check to see if there is any data
        if (xData == null || yData == null)
        {
            yEntrys.add(new PieEntry(0));
        }
        else {

            for (int i = 0; i < yData.length; i++) {
                if(Float.parseFloat(yData[i]) > 0)
                    yEntrys.add(new PieEntry(Float.parseFloat(yData[i]), xData[i]));
                else {
                    //do nothing
                }
            }

        }


        //Create data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Class Hours");
        pieDataSet.setSliceSpace(0);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setValueFormatter(new DefaultValueFormatter(3));




        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }


    /******************************* GETS NAMES OF CLASSES FROM DB FOR SPINNER**********************************/


    public void setupSpinner() {

        //class getAllClasses from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME,
                TimerContract.TimerEntry.COLUMN_PREDICTED_TIME};


        String selection = TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {"CLASS"};
        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                selection,                  // The columns for the WHERE clause////
                selectionArgs,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        String[] columns = new String[]{TimerContract.TimerEntry.COLUMN_CLASS_NAME,};


        //binds the data to the text view that holds the class name
        int[] boundTo = new int[] {
                android.R.id.text1,
        };


        simpleCursorAdapter = new android.widget.SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                cursor,
                columns,
                boundTo, 0);

        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(simpleCursorAdapter);

    }

    public void getClassNamesData(){
        //class getAllClasses from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME,
                TimerContract.TimerEntry.COLUMN_PREDICTED_TIME};

        //look in the following rows (for next action)
        String selection = TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ?";

        String[] selectionArgs = {"CLASS"};

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,    // The table to query
                projection,                             // Returns all columns
                selection,                              // filter the class and task name if the class and task name are not equal to (taskname and class)
                selectionArgs,                          //
                null,                                   // Don't group the rows
                null,                                   // Don't filter by row groups
                null);                                  // The sort order


        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            return;
        }


        //moves to first row
        cursor.moveToFirst();

        //column to index through
        int classNamesColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_CLASS_NAME);

        //initialize array list to hold class names after iteration through cursor
        ArrayList<String> xDataList = new ArrayList<>();

        //iterate through cursor and add class names to arraylist
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String classNames = cursor.getString(classNamesColumnIndex);
            xDataList.add(classNames);
        }

        //convert the arraylist into a string array for piechart usage
        xData = xDataList.toArray(new String[xDataList.size()]);
    }


    public void getElapsedTimeData(){
        //class getAllClasses from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME,
                TimerContract.TimerEntry.COLUMN_PREDICTED_TIME,
                TimerContract.TimerEntry.COLUMN_ACTIVE};

        //look in the following rows (for next action)
        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and "
                + TimerContract.TimerEntry.COLUMN_ELAPSED_TIME + " != ?";

        //Arraylist to hold results from query to get data
        ArrayList<String> yDataList = new ArrayList<>();


        if (xData != null) {
            for (int i = 0; i < xData.length; i++) {

                //check above rows to see if they have the following criteria
                String[] selectionArgs = {xData[i], "0"};

                cursor = db.query(
                        TimerContract.TimerEntry.TABLE_NAME,    // The table to query
                        projection,                             // Returns all columns
                        selection,                              // filter the class and task name if the class and task name are not equal to (taskname and class)
                        selectionArgs,                          //
                        null,                                   // Don't group the rows
                        null,                                   // Don't filter by row groups
                        null);                                  // The sort order


                //moves to first row
                cursor.moveToFirst();

                //looking through
                int elapsedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME);

                Float elapsedTime = new Float(0);

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Float currentElapsedTime = cursor.getFloat(elapsedTimeColumnIndex);
                    elapsedTime += currentElapsedTime;
                }


                elapsedTime /= 3600;
                String elapsedTimeTruncated = String.format("%.3f", elapsedTime);
                yDataList.add(elapsedTimeTruncated);

            }

            yData = yDataList.toArray(new String[yDataList.size()]);
        }
    }

}

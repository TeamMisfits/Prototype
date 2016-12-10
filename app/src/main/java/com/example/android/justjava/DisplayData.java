package com.example.android.justjava;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayData extends AppCompatActivity {

    private float[] yData = {25.4f, 10.6f};

    private String[] xData = {"EK301", "EC327"};

    PieChart pieChart;

    TaskDbHelper mDbHelper;

    SimpleCursorAdapter simpleCursorAdapter;

    Spinner spin;

    Button button;

    ArrayList<String> classname;

    ArrayList<String> timespent;

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
        addDataSet(pieChart);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                //Retrieves information for the toast display
                int pos1 = e.toString().indexOf("(sum): ");
                String hours = e.toString().substring(pos1 + 7);

                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == Float.parseFloat(hours)) {
                        pos1 = i;
                        break;
                    }
                }

                String task = xData[pos1];
                Toast.makeText(DisplayData.this, "Class: " + task + "\n" + "Hours: " + hours, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                /*
                switch (position) {
                    case 0:
                        Toast.makeText(parent.getContext(), xData[position] , Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(parent.getContext(), xData[position], Toast.LENGTH_SHORT).show();
                        break;
                   case 2:
                        Toast.makeText(parent.getContext(), xData[position], Toast.LENGTH_SHORT).show();
                        break;
                }
                */
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
                Intent intent = new Intent(getApplicationContext(), ClassData.class);
                startActivity(intent);
            }
        });

    }

    private void addDataSet(PieChart pieChart) {

        //TODO pass data in hours to yData as a String[] and pass class names to xData as String[]
        //declare globally to allow for passing of class names from spinner

        //String[] xData = (String[]) classname.toArray(new String[classname.size()]);
        //String[] yData = (String[]) timespent.toArray(new String[timespent.size()]);

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry((yData[i])));
        }

        for (int i = 0; i < xData.length; i++) {
            xEntrys.add((xData[i]));
        }

        //Create data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Class Hours");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.WHITE);
        colors.add(Color.LTGRAY);
        colors.add(Color.DKGRAY);
        colors.add(Color.TRANSPARENT);

        pieDataSet.setColors(colors);

        /*
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
    */

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

        //Error checking
        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            return;
        }

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

    private void getData(){

        //class getAllClasses from TaskDBHelper
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TimerContract.TimerEntry._ID,
                TimerContract.TimerEntry.COLUMN_CLASS_NAME,
                TimerContract.TimerEntry.COLUMN_TASK_NAME,
                TimerContract.TimerEntry.COLUMN_START_TIME,
                TimerContract.TimerEntry.COLUMN_ELAPSED_TIME,
                TimerContract.TimerEntry.COLUMN_PREDICTED_TIME};

        // Perform a query on the pets table

        Cursor cursor = db.query(
                TimerContract.TimerEntry.TABLE_NAME,    // The table to query
                projection,                             // Returns all columns
                null,                              // filter the class and task name if the class and task name are not equal to (taskname and class)
                null,                          //
                null,                                   // Don't group the rows
                null,                                   // Don't filter by row groups
                null);                                  // The sort order


        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            return;
        }


        /*
        //moves to first row
        cursor.moveToFirst();


        int elapsedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_ELAPSED_TIME);

        int predictedTimeColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_PREDICTED_TIME);

        //gets the value from elapsed time as long
        long elapsedTime = cursor.getLong(elapsedTimeColumnIndex);

        double predictedTime = cursor.getDouble(predictedTimeColumnIndex);

        double seconds = (double)elapsedTime;

        seconds = seconds / 3600;

        elapsedTimeView.setText(Double.toString(seconds) + " hours");

        predictedTimeView.setText(Double.toString(predictedTime) + " hours");
        */
    }

}

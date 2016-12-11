package com.example.android.justjava;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;
import com.example.android.justjava.data.TimerContract;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassData extends AppCompatActivity {

    private String[] yData;

    private String[] xData;

    TaskDbHelper mDbHelper;

    PieChart pieChart;

    Cursor cursor;

    String className;

    TextView class_name;

    //used for labels
    ArrayList<String> xDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_data);

        final Intent intent = getIntent();

        className = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        class_name = (TextView) findViewById(R.id.class_name);

        class_name.setText(className);

        mDbHelper = new TaskDbHelper(this);

        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleAlpha(0);


        getTaskNamesData();
        getElapsedTimeData();
        addDataSet(pieChart);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {


                //Retrieves information for the toast display
                String taskSubstring = h.toString().substring(14,15);
                int pos1 = Integer.parseInt(taskSubstring);

                /*
                for (int i = 0; i < xData.length; i++) {
                    if (xData[i].equals(taskSubstring)) {
                        pos1 = i;
                        break;
                    }
                }
                */

                String task = xData[pos1];
                String hours = yData[pos1];
                Toast.makeText(ClassData.this, "Task: " + task + "\n" + "Hours: " + hours, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> Entrys = new ArrayList<>();

        //Error check to see if there is any data
        if (xData == null || yData == null)
        {
            Entrys.add(new PieEntry(0));
        }
        else {

            for (int i = 0; i < yData.length; i++) {
                if ((Float.parseFloat(yData[i]) > 0))
                    Entrys.add(new PieEntry(Float.parseFloat(yData[i]), xData[i]));
                else{
                    //do nothing
                }
            }
        }


        //Create data set
        PieDataSet pieDataSet = new PieDataSet(Entrys, "Class Hours");

        pieDataSet.setSliceSpace(0);
        pieDataSet.setValueTextSize(10);

        pieDataSet.setValueFormatter(new DefaultValueFormatter(3));

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.setDescription("");
        pieChart.invalidate();

    }


    public void getTaskNamesData(){

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
        String selection = TimerContract.TimerEntry.COLUMN_CLASS_NAME + " = ? and "
                + TimerContract.TimerEntry.COLUMN_TASK_NAME + " != ?";

        String[] selectionArgs = {className, "CLASS"};

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
        int taskNamesColumnIndex = cursor.getColumnIndex(TimerContract.TimerEntry.COLUMN_TASK_NAME);

        //initialize array list to hold class names after iteration through cursor
        xDataList = new ArrayList<>();

        //iterate through cursor and add class names to arraylist
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String taskNames = cursor.getString(taskNamesColumnIndex);
            xDataList.add(taskNames);
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
        String selection = TimerContract.TimerEntry.COLUMN_TASK_NAME + " = ? ";

        //Arraylist to hold results from query to get data
        ArrayList<String> yDataList = new ArrayList<>();


        if (xData != null) {
            for (int i = 0; i < xData.length; i++) {

                //check above rows to see if they have the following criteria
                String[] selectionArgs = {xData[i]};

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

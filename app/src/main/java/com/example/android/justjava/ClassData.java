package com.example.android.justjava;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassData extends AppCompatActivity {

    private float[] yData = {25.4f, 10.6f, 66.7f, 33.4f};

    private String[] xData = {"PA4", "Project", "PA3", "HW4"};

    TaskDbHelper mDbHelper;

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_data);

        final Intent intent = getIntent();

        mDbHelper = new TaskDbHelper(this);

        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleAlpha(0);

        //getData();
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
                Toast.makeText(ClassData.this, "Class: " + task + "\n" + "Hours: " + hours, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDataSet(PieChart pieChart) {

        //TODO pass data in hours to yData as a String[] and pass task names to xData as String[]

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i]));
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


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    public void getData() {

        //TODO retrieve time of multiple tasks and placed into a String[] for yData and task names in xData



    }
}

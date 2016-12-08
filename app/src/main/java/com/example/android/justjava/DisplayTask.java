package com.example.android.justjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayTask extends AppCompatActivity {

    String taskname;

    TextView textView;

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
    }
}

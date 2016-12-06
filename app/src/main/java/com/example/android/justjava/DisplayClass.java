package com.example.android.justjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.data.TaskDbHelper;

public class DisplayClass extends AppCompatActivity {

    TextView textView;

    String classname;

    TaskDbHelper dbHandler;

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class);

        //Receives intent from MainActivity and sets the classname equal to the received EXTRA_MESSAGE
        Intent intent = getIntent();

        classname = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        textView = (TextView) findViewById(R.id.class_title);

        textView.setText(classname);

        //Create a new TaskDbHelper
        dbHandler = new TaskDbHelper(this);

    }

    public void deleteClass(View view){

        dbHandler.deleteClass(classname);
        Toast.makeText(this, classname + " Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }


}

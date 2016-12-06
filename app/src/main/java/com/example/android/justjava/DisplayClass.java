package com.example.android.justjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayClass extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_class);

        Intent intent = getIntent();

        String text = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        textView = (TextView) findViewById(R.id.class_title);

        textView.setText(text);

    }
}

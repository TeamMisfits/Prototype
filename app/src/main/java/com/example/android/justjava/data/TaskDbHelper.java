package com.example.android.justjava.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.justjava.data.TimerContract.TimerEntry;

/**
 * Created by Owner on 12/3/2016.
 */

public class TaskDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TaskDbHelper.class.getSimpleName();

    //name of the database file
    private static final String DATABASE_NAME = "schedule1.db";

    //Database version

    private static final int DATABASE_VERSION = 1;

    //creates instance of TaskDbHelper

    public TaskDbHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    //called when database is first made

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //string used to create data base
        String SQL_CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TimerEntry.TABLE_NAME + " ("
                + TimerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TimerEntry.COLUMN_CLASS_NAME + " TEXT NOT NULL, "
                + TimerEntry.COLUMN_TASK_NAME + " TEXT, "
                + TimerEntry.COLUMN_START_TIME + " INTEGER, "
                + TimerEntry.COLUMN_ELAPSED_TIME + " INTEGER);";

        //actually creates data base
        db.execSQL(SQL_CREATE_SCHEDULE_TABLE);
    }

    //function to update database when necessary
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //database currently up to date. Doesnt do anythign
    }
}

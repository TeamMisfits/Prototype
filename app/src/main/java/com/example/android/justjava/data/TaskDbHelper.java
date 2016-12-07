package com.example.android.justjava.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.justjava.data.TimerContract.TimerEntry;

import java.sql.Time;

public class TaskDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TaskDbHelper.class.getSimpleName();

    //Database version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "schedule1.db";

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
        db.execSQL("DROP TABLE IF EXISTS " + TimerEntry.TABLE_NAME);
        onCreate(db);
    }


    /**
     * Gets all Products in the Database and returns a cursor of them.
     * If there are no items in the database then the cursor returns null
     *
     * @return A Cursor of all products or null
     */
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TimerEntry.TABLE_NAME, new String[] {TimerEntry._ID, TimerEntry.COLUMN_CLASS_NAME, TimerEntry.COLUMN_TASK_NAME, TimerEntry.COLUMN_START_TIME, TimerEntry.COLUMN_ELAPSED_TIME}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        else
        {
            return null;
        }

    }

    /**
     * This function delete's a product in TABLE_PRODUCTS based on the ID of the product retrieved
     * by it's product name.
     * @param classname The name of the product to delete
     * @return True if deleted false otherwise.
     */
    public void deleteClass(String classname) {
        String q = "SELECT * FROM " + TimerEntry.TABLE_NAME + " WHERE " + TimerEntry.COLUMN_CLASS_NAME
                + " = \"" + classname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            db.delete(TimerEntry.TABLE_NAME, TimerEntry._ID + " = ?",
                    new String[] { String.valueOf(Integer.parseInt(cursor.getString(0)))});

            cursor.close();
        }
        db.close();
    }

}

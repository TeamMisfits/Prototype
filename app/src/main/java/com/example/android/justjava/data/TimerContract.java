package com.example.android.justjava.data;

/**
 * Created by Owner on 12/3/2016.
 */

import android.provider.BaseColumns;


public final class TimerContract {

    //private constructor for timer contract to prevent user instantiation
    private TimerContract(){}

    //Inner class within TimerContract. Defines constants for data base table
    public static final class TimerEntry implements BaseColumns{

        //name of the data base
        public final static String TABLE_NAME = "tasks";

        //unique ID number for each task

        public final static String _ID = BaseColumns._ID;

        //name of the class

        public final static String COLUMN_CLASS_NAME = "className";

        //name of the task

        public final static String COLUMN_TASK_NAME = "taskName";

        //start time for the task

        public final static String COLUMN_START_TIME = "startTime";

        //end time for the task

        public final static String COLUMN_ELAPSED_TIME = "totalTime";
    }
}

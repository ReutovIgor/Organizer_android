package com.example.ruireutov.organiser.DatabaseWorkers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    //Database name
    private static final String DATABASE_NAME = "organiser_db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database tables
    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PRIORITIES = "priorities_table";
    private static final String TABLE_TASKS = "tasks_table";

    //Database common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAILS = "details";

    //Database Tasks table column names
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_PRIORITY_ID = "priority_id";


    //Database Create tables queries
    private static final String TABLE_CATEGORIES_CREATE = "create table "
            + TABLE_CATEGORIES + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null);";

    private static final String TABLE_PRIORITIES_CREATE = "create table "
            + TABLE_PRIORITIES + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null);";

    private static final String TABLE_TASKS_CREATE = "create table "
            + TABLE_TASKS + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "
            + KEY_DETAILS + " text not null, "
            + KEY_START + " datetime not null, "
            + KEY_END + " datetime not null, "
            + KEY_STATUS + " boolean not null, "
            + KEY_CATEGORY_ID + " integer not null "
            + KEY_PRIORITY_ID + " integer not null "
            + " FOREIGN KEY ( " + KEY_CATEGORY_ID + " ) REFERENCES " + TABLE_CATEGORIES + " ( "+ KEY_ID + " ) "
            + " FOREIGN KEY ( " + KEY_PRIORITY_ID + " ) REFERENCES " + TABLE_PRIORITIES + " ( "+ KEY_ID + " ) "
            + " ); ";




//    private static final String TASK_TABLE_CREATE = "create table "
//            + TASK_TABLE + " ("
//            + TASK_ID + " integer primary key autoincrement, "
//            + TASK_TITLE + " text not null, "
//            + TASK_NOTES + " text not null, "
//            + TASK_DATE_TIME + " text not null,"
//            + TASK_CAT + " integer,"
//            + " FOREIGN KEY ("+TASK_CAT+") REFERENCES "+CAT_TABLE+"("+CAT_ID+"));";
    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORIES_CREATE);
        db.execSQL(TABLE_PRIORITIES_CREATE);
        db.execSQL(TABLE_TASKS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no upgrades for now =)
    }
}

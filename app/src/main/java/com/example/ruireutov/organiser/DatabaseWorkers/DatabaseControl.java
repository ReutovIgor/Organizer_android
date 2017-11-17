package com.example.ruireutov.organiser.DatabaseWorkers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public class DatabaseControl {
    private static DatabaseControl instance;
    private DatabaseHelper dbHelper;

    //Database name
    private static final String DATABASE_NAME = "organiser_db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database tables
    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PRIORITIES = "priorities_table";
    private static final String TABLE_TASKS = "tasks_table";

    //Database common column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DETAILS = "details";

    //Database Tasks table column names
    public static final String KEY_START = "startTime";
    public static final String KEY_END = "endTime";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_PRIORITY_ID = "priority_id";


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
            + KEY_DETAILS + " text, "
            + KEY_START + " datetime not null, "
            + KEY_END + " datetime not null, "
            + KEY_STATUS + " boolean not null, "
            + KEY_CATEGORY_ID + " integer not null, "
            + KEY_PRIORITY_ID + " integer not null, "
            + " FOREIGN KEY ( " + KEY_CATEGORY_ID + " ) REFERENCES " + TABLE_CATEGORIES + " ( "+ KEY_ID + " ) "
            + " FOREIGN KEY ( " + KEY_PRIORITY_ID + " ) REFERENCES " + TABLE_PRIORITIES + " ( "+ KEY_ID + " ) "
            + " ); ";

    public static synchronized DatabaseControl getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseControl(context.getApplicationContext());
        }
        return instance;
    }
    private DatabaseControl(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public Cursor get(String tables, String[] columns) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor c;
        c = db.query(tables, columns, null, null, null, null, null);
        db.close();
        return c;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION + 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String s = "DROP TABLE IF EXISTS ";
            db.execSQL(s + TABLE_TASKS);
            db.execSQL(s + TABLE_CATEGORIES);
            db.execSQL(s + TABLE_PRIORITIES);

            db.execSQL(TABLE_CATEGORIES_CREATE);
            db.execSQL(TABLE_PRIORITIES_CREATE);
            db.execSQL(TABLE_TASKS_CREATE);

            //temporary data
            //Priorities data
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, "Low");
            db.insert(TABLE_PRIORITIES, null, cv);
            cv.clear();
            cv.put(KEY_NAME, "Normal");
            long id1 = db.insert(TABLE_PRIORITIES, null, cv);
            cv.clear();
            cv.put(KEY_NAME, "High");
            db.insert(TABLE_PRIORITIES, null, cv);

            //Category data
            cv.clear();
            cv.put(KEY_NAME, "Low");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();
            cv.put(KEY_NAME, "Normal");
            long id2 = db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();
            cv.put(KEY_NAME, "High");
            db.insert(TABLE_CATEGORIES, null, cv);

            //Tasks data
            cv.clear();
            cv.put(KEY_NAME, "Task1");
            cv.put(KEY_DETAILS, "Task details");
            cv.put(KEY_START, "2018-12-12T10:00");
            cv.put(KEY_END, "2018-12-12T12:00");
            cv.put(KEY_STATUS, 0);
            cv.put(KEY_CATEGORY_ID, id2);
            cv.put(KEY_PRIORITY_ID, id1);

            long id3 = db.insert(TABLE_TASKS, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            this.onCreate(db);
        }
    }
}



package com.example.ruireutov.organiser.DatabaseWorkers;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public class DatabaseControl {
    //Database name
    private static final String DATABASE_NAME = "organiser_db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database tables
    private static final String TABLE_GROUPS = "groups_table";
    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PRIORITIES = "priorities_table";
    private static final String TABLE_TASKS = "tasks_table";

    //Database common column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DETAILS = "details";

    //Database Main screen group names
    public static final String KEY_GROUP_NAME = "group_name";

    //Database Tasks table column names
    public static final String KEY_START = "startTime";
    public static final String KEY_END = "endTime";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_PRIORITY_ID = "priority_id";


    //Database Create tables queries
    private static final String TABLE_GROUPS_CREATE = "create table "
            + TABLE_GROUPS + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "
            + KEY_GROUP_NAME + " text not null);";
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

    private static final String GET_TO_DO_LIST_TABLE = TABLE_TASKS +
            " LEFT JOIN " + TABLE_CATEGORIES + " on " + TABLE_TASKS + "." + KEY_CATEGORY_ID + "=" + TABLE_CATEGORIES + "." + KEY_ID +
            " LEFT JOIN " + TABLE_PRIORITIES + " on " + TABLE_TASKS + "." + KEY_PRIORITY_ID + "=" + TABLE_PRIORITIES + "." + KEY_ID;
//    private static final String[] GET_TO_DO_LIST_TABLE_COLUMNS = {};


    private Context context;
    private static DatabaseControl instance;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int connectionCount;
    private HashMap<String, Cursor> storedValues;

    public static synchronized DatabaseControl getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseControl(context.getApplicationContext());
        }
        return instance;
    }
    private DatabaseControl(Context context) {
        this.connectionCount = 0;
        this.context = context;

        this.storedValues = new HashMap<>();
        //TODO add new values when needed
        this.storedValues.put("ToDoList", null);

        this.dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        this.connectionCount++;
        if(this.db == null) {
            this.db = this.dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        this.connectionCount--;
        if(this.connectionCount == 0) {
            this.dbHelper.close();
            this.db = null;
        }
    }

    public Cursor getToDoList() {
        String[] columns = {
                TABLE_TASKS + "." + KEY_ID + " AS _id",
                TABLE_TASKS + "." + KEY_NAME + " AS Name",
                TABLE_TASKS + "." + KEY_DETAILS + " AS Details",
                TABLE_TASKS + "." + KEY_START + " AS Start",
                TABLE_TASKS + "." + KEY_END + " AS End",
                TABLE_TASKS + "." + KEY_STATUS + " AS Status",
                TABLE_CATEGORIES + "." + KEY_NAME + " AS Category",
                TABLE_PRIORITIES + "." + KEY_NAME + " AS Priority"
        };
        Cursor c;
        try {
            c = this.db.query(GET_TO_DO_LIST_TABLE, columns, null, null, null, null, null);
        } catch(Exception e) {
            return null;
        }

        return c;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String s = "DROP TABLE IF EXISTS ";
            db.execSQL(s + TABLE_GROUPS);
            db.execSQL(s + TABLE_TASKS);
            db.execSQL(s + TABLE_CATEGORIES);
            db.execSQL(s + TABLE_PRIORITIES);

            db.execSQL(TABLE_GROUPS_CREATE);
            db.execSQL(TABLE_CATEGORIES_CREATE);
            db.execSQL(TABLE_PRIORITIES_CREATE);
            db.execSQL(TABLE_TASKS_CREATE);

            //temporary data
            //Priorities data
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, TABLE_TASKS);
            cv.put(KEY_GROUP_NAME, "To do list");
            long id = db.insert(TABLE_GROUPS, null, cv);

            cv.clear();
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

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //super.onDowngrade(db, oldVersion, newVersion);
            this.onCreate(db);
        }
    }
}



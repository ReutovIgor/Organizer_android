package com.example.ruireutov.organiser.DatabaseWorkers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ruireutov.organiser.TaskDetailsData;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public class DatabaseControl {
    //Database name
    private static final String DATABASE_NAME = "organiser_db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database tables
    private static final String TABLE_STATUSES = "statuses_table";
    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PRIORITIES = "priorities_table";
    private static final String TABLE_TASKS = "tasks_table";

    //Database common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_COLOR = "color";

    //Database Priority table column names
    private  static final String KEY_MARK = "mark";

    //Database Category table column names
    private static final String KEY_ICON = "icon";

    //Database Tasks table column names
    private static final String KEY_START = "startTime";
    private static final String KEY_END = "endTime";
    private static final String KEY_STATUS_ID = "status_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_PRIORITY_ID = "priority_id";


    //Database Create tables queries
    private static final String TABLE_STATUSES_CREATE = "create table "
            + TABLE_STATUSES + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null);";

    private static final String TABLE_CATEGORIES_CREATE = "create table "
            + TABLE_CATEGORIES + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null,"
            + KEY_ICON + " text not null,"
            + KEY_COLOR + " text not null);";

    private static final String TABLE_PRIORITIES_CREATE = "create table "
            + TABLE_PRIORITIES + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null,"
            + KEY_MARK + " text not null,"
            + KEY_COLOR + " text not null);";

    private static final String TABLE_TASKS_CREATE = "create table "
            + TABLE_TASKS + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "
            + KEY_DETAILS + " text, "
            + KEY_START + " text, "
            + KEY_END + " text, "
            + KEY_STATUS_ID + " boolean not null, "
            + KEY_CATEGORY_ID + " integer not null, "
            + KEY_PRIORITY_ID + " integer not null, "
            + " FOREIGN KEY ( " + KEY_CATEGORY_ID + " ) REFERENCES " + TABLE_CATEGORIES + " ( "+ KEY_ID + " ) "
            + " FOREIGN KEY ( " + KEY_PRIORITY_ID + " ) REFERENCES " + TABLE_PRIORITIES + " ( "+ KEY_ID + " ) "
            + " ); ";

    private static final String GET_TO_DO_LIST_TABLE = TABLE_TASKS +
            " LEFT JOIN " + TABLE_STATUSES   + " on " + TABLE_TASKS + "." + KEY_STATUS_ID   + "=" + TABLE_STATUSES   + "." + KEY_ID +
            " LEFT JOIN " + TABLE_CATEGORIES + " on " + TABLE_TASKS + "." + KEY_CATEGORY_ID + "=" + TABLE_CATEGORIES + "." + KEY_ID +
            " LEFT JOIN " + TABLE_PRIORITIES + " on " + TABLE_TASKS + "." + KEY_PRIORITY_ID + "=" + TABLE_PRIORITIES + "." + KEY_ID;

    private static DatabaseControl instance;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int connectionCount;

    public static synchronized DatabaseControl getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseControl(context.getApplicationContext());
        }
        return instance;
    }
    private DatabaseControl(Context context) {
        this.connectionCount = 0;
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

    public Cursor getTasks() {
        String[] columns = {
                TABLE_TASKS + "." + KEY_ID + " AS _id",
                TABLE_TASKS + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_NAME,
                TABLE_TASKS + "." + KEY_DETAILS + " AS " + DatabaseDefines.TASK_LIST_DETAILS,
                TABLE_TASKS + "." + KEY_START + " AS " + DatabaseDefines.TASK_LIST_START,
                TABLE_TASKS + "." + KEY_END + " AS " + DatabaseDefines.TASK_LIST_END,
                TABLE_STATUSES + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_STATUS,
                TABLE_CATEGORIES + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_CATEGORY,
                TABLE_CATEGORIES + "." + KEY_COLOR + " AS " + DatabaseDefines.TASK_LIST_CATEGORY_COLOR,
                TABLE_CATEGORIES + "." + KEY_ICON + " AS " + DatabaseDefines.TASK_LIST_CATEGORY_ICON,
                TABLE_PRIORITIES + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_PRIORITY,
                TABLE_PRIORITIES + "." + KEY_MARK + " AS " + DatabaseDefines.TASK_LIST_PRIORITY_MARK,
                TABLE_PRIORITIES + "." + KEY_COLOR + " AS " + DatabaseDefines.TASK_LIST_PRIORITY_COLOR,
        };
        Cursor c;
        try {
            c = this.db.query(GET_TO_DO_LIST_TABLE, columns, null, null, null, null, null);
        } catch(Exception e) {
            return null;
        }

        return c;
    }

    public void addTask(TaskDetailsData taskData) {
        //get Status ID
        Cursor sId = this.db.query(TABLE_STATUSES, null, KEY_NAME + " = ?", new String[] {taskData.getStatus()}, null, null, null);
        sId.moveToFirst();
        String statusId = sId.getString(sId.getColumnIndex(KEY_ID));

        //get Category ID
        Cursor cId = this.db.query(TABLE_CATEGORIES, null, KEY_NAME + " = ?", new String[] {taskData.getCategory()}, null, null, null);
        cId.moveToFirst();
        String categoryId = cId.getString(cId.getColumnIndex(KEY_ID));

        //get Priority ID
        Cursor pId = this.db.query(TABLE_PRIORITIES, null, KEY_NAME + " = ?", new String[] {taskData.getPriority()}, null, null, null);
        pId.moveToFirst();
        String priorityId = pId.getString(pId.getColumnIndex(KEY_ID));

        //insert new task
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, taskData.getName());
        cv.put(KEY_STATUS_ID, statusId);
        cv.put(KEY_START, taskData.getDateFrom());
        cv.put(KEY_END, taskData.getDateTo());
        cv.put(KEY_CATEGORY_ID, categoryId);
        cv.put(KEY_PRIORITY_ID, priorityId);
        cv.put(KEY_DETAILS, taskData.getName());

        db.insert(TABLE_TASKS, null, cv);
    }

    public Cursor getCategories() {
        String[] columns = {
                KEY_ID + " AS _id",
                KEY_NAME + " AS " + DatabaseDefines.CATEGORIES_NAME,
                KEY_COLOR + " AS " + DatabaseDefines.CATEGORIES_COLOR,
                KEY_ICON + " AS " + DatabaseDefines.CATEGORIES_ICON
        };
        Cursor c  = this.db.query(TABLE_CATEGORIES, columns, null, null, null, null, null);
        return c;
    }

    public Cursor getPriorities() {
        String[] columns = {
                KEY_ID + " AS _id",
                KEY_NAME + " AS " + DatabaseDefines.PRIORITIES_NAME,
                KEY_MARK + " AS " + DatabaseDefines.PRIORITIES_MARK,
                KEY_COLOR + " AS " + DatabaseDefines.PRIORITIES_COLOR
        };
        Cursor c  = this.db.query(TABLE_PRIORITIES, columns, null, null, null, null, null);
        return c;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String s = "DROP TABLE IF EXISTS ";
            db.execSQL(s + TABLE_STATUSES);
            db.execSQL(s + TABLE_TASKS);
            db.execSQL(s + TABLE_CATEGORIES);
            db.execSQL(s + TABLE_PRIORITIES);

            db.execSQL(TABLE_STATUSES_CREATE);
            db.execSQL(TABLE_CATEGORIES_CREATE);
            db.execSQL(TABLE_PRIORITIES_CREATE);
            db.execSQL(TABLE_TASKS_CREATE);

            //initial data for Organiser tasks
            //Priorities data
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, DatabaseDefines.STATUS_IN_PROGRESS);
            db.insert(TABLE_STATUSES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, DatabaseDefines.STATUS_FAILED);
            db.insert(TABLE_STATUSES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, DatabaseDefines.STATUS_FINISHED);
            db.insert(TABLE_STATUSES, null, cv);
            cv.clear();

            //Priority date
            cv.put(KEY_NAME, "High");
            cv.put(KEY_MARK, "!!!");
            cv.put(KEY_COLOR, "#FF3500");
            db.insert(TABLE_PRIORITIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Normal");
            cv.put(KEY_MARK, "!!");
            cv.put(KEY_COLOR, "#FF9900");
            db.insert(TABLE_PRIORITIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Low");
            cv.put(KEY_MARK, "!");
            cv.put(KEY_COLOR, "#FFD600");
            db.insert(TABLE_PRIORITIES, null, cv);
            cv.clear();

            //Category data
            cv.put(KEY_NAME, "Default");
            cv.put(KEY_COLOR, "#FFB873");
            cv.put(KEY_ICON, "default_task_category");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Favourite");
            cv.put(KEY_COLOR, "#F2FD3F");
            cv.put(KEY_ICON, "favourite_task_category");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Home");
            cv.put(KEY_COLOR, "#A9F16C");
            cv.put(KEY_ICON, "home_task_category");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Shopping");
            cv.put(KEY_COLOR, "#7373D9");
            cv.put(KEY_ICON, "shopping_task_category");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Travel");
            cv.put(KEY_COLOR, "#61B4CF");
            cv.put(KEY_ICON, "travel_task_category");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();

            cv.put(KEY_NAME, "Work");
            cv.put(KEY_COLOR, "#FF7373");
            cv.put(KEY_ICON, "work_task_category");
            db.insert(TABLE_CATEGORIES, null, cv);
            cv.clear();
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



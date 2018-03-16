package com.example.ruireutov.organiser.databaseWorkers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ruireutov.organiser.task.TaskDetailsData;
import com.example.ruireutov.organiser.task.filters.ITaskListFilter;
import com.example.ruireutov.organiser.task.filters.TasksFilter;

import java.util.HashMap;

public class DatabaseControl {
    //Database name
    private static final String DATABASE_NAME = "organiser_db";

    //Database version
    private static final int DATABASE_VERSION = 3;

    //Database tables
    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PRIORITIES = "priorities_table";
    private static final String TABLE_TASKS = "tasks_table";

    //Database common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COLOR = "color";

    //Database Priority table column names
    private  static final String KEY_MARK = "mark";

    //Database Category table column names
    private static final String KEY_ICON = "icon";

    //Database Tasks table column names
    private static final String KEY_START = "startTime";
    private static final String KEY_END = "endTime";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_PRIORITY_ID = "priority_id";
    private static final String KEY_DETAILS = "details";


    //Database Create tables queries
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
            + KEY_START + " text not null, "
            + KEY_END + " text, "
            + KEY_STATUS + " integer not null, "
            + KEY_CATEGORY_ID + " integer not null, "
            + KEY_PRIORITY_ID + " integer not null, "
            + " FOREIGN KEY ( " + KEY_CATEGORY_ID + " ) REFERENCES " + TABLE_CATEGORIES + " ( "+ KEY_ID + " ) "
            + " FOREIGN KEY ( " + KEY_PRIORITY_ID + " ) REFERENCES " + TABLE_PRIORITIES + " ( "+ KEY_ID + " ) "
            + " ); ";

    private static final String GET_TASK_LIST_TABLE = TABLE_TASKS +
            " LEFT JOIN " + TABLE_CATEGORIES + " on " + TABLE_TASKS + "." + KEY_CATEGORY_ID + "=" + TABLE_CATEGORIES + "." + KEY_ID +
            " LEFT JOIN " + TABLE_PRIORITIES + " on " + TABLE_TASKS + "." + KEY_PRIORITY_ID + "=" + TABLE_PRIORITIES + "." + KEY_ID;

    private static final String GET_CATEGORIES_FILTERS_TABLE = TABLE_CATEGORIES +
            " LEFT JOIN " + TABLE_TASKS + " on " + TABLE_TASKS + "." + KEY_CATEGORY_ID + "=" + TABLE_CATEGORIES + "." + KEY_ID +
            " LEFT JOIN " + TABLE_PRIORITIES + " on " + TABLE_TASKS + "." + KEY_PRIORITY_ID + "=" + TABLE_PRIORITIES + "." + KEY_ID;

    private static final String GET_PRIORITIES_FILTERS_TABLE = TABLE_PRIORITIES +
            " LEFT JOIN " + TABLE_TASKS + " on " + TABLE_TASKS + "." + KEY_PRIORITY_ID + "=" + TABLE_PRIORITIES + "." + KEY_ID +
            " LEFT JOIN " + TABLE_CATEGORIES + " on " + TABLE_TASKS + "." + KEY_CATEGORY_ID + "=" + TABLE_CATEGORIES + "." + KEY_ID;

    private static DatabaseControl instance;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private HashMap<String, String> taskListFilterMapping;
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

        this.taskListFilterMapping = new HashMap<>();
        this.taskListFilterMapping.put(TasksFilter.TASK_FILTER_TIME_END, KEY_END);
        this.taskListFilterMapping.put(TasksFilter.TASK_FILTER_STATUS, KEY_STATUS);
        this.taskListFilterMapping.put(TasksFilter.TASK_FILTER_TIME_START, KEY_START);
        this.taskListFilterMapping.put(TasksFilter.TASK_FILTER_CATEGORY, TABLE_CATEGORIES + "." + KEY_NAME);
        this.taskListFilterMapping.put(TasksFilter.TASK_FILTER_PRIORITY, TABLE_PRIORITIES + "." + KEY_NAME);
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

    public Cursor getTasks(ITaskListFilter filter) {
        String[] columns = {
                TABLE_TASKS + "." + KEY_ID + " AS " + DatabaseDefines.TASK_LIST_ID,
                TABLE_TASKS + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_NAME,
                TABLE_TASKS + "." + KEY_DETAILS + " AS " + DatabaseDefines.TASK_LIST_DETAILS,
                TABLE_TASKS + "." + KEY_START + " AS " + DatabaseDefines.TASK_LIST_START,
                TABLE_TASKS + "." + KEY_END + " AS " + DatabaseDefines.TASK_LIST_END,
                TABLE_TASKS + "." + KEY_STATUS + " AS " + DatabaseDefines.TASK_LIST_STATUS,
                TABLE_CATEGORIES + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_CATEGORY,
                TABLE_CATEGORIES + "." + KEY_COLOR + " AS " + DatabaseDefines.TASK_LIST_CATEGORY_COLOR,
                TABLE_CATEGORIES + "." + KEY_ICON + " AS " + DatabaseDefines.TASK_LIST_CATEGORY_ICON,
                TABLE_PRIORITIES + "." + KEY_NAME + " AS " + DatabaseDefines.TASK_LIST_PRIORITY,
                TABLE_PRIORITIES + "." + KEY_MARK + " AS " + DatabaseDefines.TASK_LIST_PRIORITY_MARK,
                TABLE_PRIORITIES + "." + KEY_COLOR + " AS " + DatabaseDefines.TASK_LIST_PRIORITY_COLOR,
        };
        Cursor c;
        try {
            filter.generateQueryParams(this.taskListFilterMapping);
            String where = filter.getWhere();
            String[] selection = filter.getSelection();
            c = this.db.query(GET_TASK_LIST_TABLE, columns, where, selection, null, null, null);
        } catch(Exception e) {
            return null;
        }

        return c;
    }

    public void addTask(TaskDetailsData taskData) {
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
        cv.put(KEY_STATUS, taskData.getStatus());
        cv.put(KEY_START, taskData.getDateStartString("yyyy-MM-DD HH:mm"));
        cv.put(KEY_END, taskData.getDateDueString("yyyy-MM-DD HH:mm"));
        cv.put(KEY_CATEGORY_ID, categoryId);
        cv.put(KEY_PRIORITY_ID, priorityId);
        cv.put(KEY_DETAILS, taskData.getDetails());

        db.insert(TABLE_TASKS, null, cv);
    }

    public void updateTask(TaskDetailsData taskData) {
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
        cv.put(KEY_STATUS, taskData.getStatus());
        cv.put(KEY_START, taskData.getDateStartString("yyyy-MM-DD HH:mm"));
        cv.put(KEY_END, taskData.getDateDueString("yyyy-MM-DD HH:mm"));
        cv.put(KEY_CATEGORY_ID, categoryId);
        cv.put(KEY_PRIORITY_ID, priorityId);
        cv.put(KEY_DETAILS, taskData.getDetails());

        db.update(TABLE_TASKS, cv, KEY_ID + " = ?", new String[] {Long.toString(taskData.getId())});
    }

    public void closeTask(long id) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, DatabaseDefines.TASK_STATUS_COMPLETED);
        db.update(TABLE_TASKS, cv, KEY_ID + " = ?", new String[] {Long.toString(id)});
    }

    public void removeTask(long id) {
        db.delete(TABLE_TASKS, KEY_ID + " = ?", new String[] {Long.toString(id)});
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

    public Cursor getCategoriesFilter(ITaskListFilter filter) {
        String[] columns = {
                TABLE_CATEGORIES + "." + KEY_ID + " AS _id",
                TABLE_CATEGORIES + "." + KEY_NAME + " AS " + DatabaseDefines.FILTER_NAME,
                "COUNT(" + TABLE_TASKS + "." + KEY_ID + ") AS " + DatabaseDefines.FILTER_COUNT
        };
        Cursor c;
        try {
            filter.generateQueryParams(this.taskListFilterMapping);
            String where = filter.getWhere();
            String[] selection = filter.getSelection();
            String groupBy = TABLE_CATEGORIES + "." + KEY_NAME;
            c = this.db.query(GET_CATEGORIES_FILTERS_TABLE, columns, where, selection, groupBy, null, null);
        } catch(Exception e) {
            return null;
        }

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

    public Cursor getPrioritiesFilter(ITaskListFilter filter) {
        String[] columns = {
                TABLE_PRIORITIES + "." + KEY_ID + " AS _id",
                TABLE_PRIORITIES + "." + KEY_NAME + " AS " + DatabaseDefines.FILTER_NAME,
                "COUNT(" + TABLE_TASKS + "." + KEY_ID + ") AS " + DatabaseDefines.FILTER_COUNT
        };
        Cursor c;
        try {
            filter.generateQueryParams(this.taskListFilterMapping);
            String where = filter.getWhere();
            String[] selection = filter.getSelection();
            String groupBy = TABLE_PRIORITIES + "." + KEY_NAME;
            c = this.db.query(GET_PRIORITIES_FILTERS_TABLE, columns, where, selection, groupBy , null, null);
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
            db.execSQL(s + TABLE_TASKS);
            db.execSQL(s + TABLE_CATEGORIES);
            db.execSQL(s + TABLE_PRIORITIES);

            db.execSQL(TABLE_CATEGORIES_CREATE);
            db.execSQL(TABLE_PRIORITIES_CREATE);
            db.execSQL(TABLE_TASKS_CREATE);

            //initial data for Organiser tasks
            //Priorities data
            ContentValues cv = new ContentValues();
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



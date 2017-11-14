package com.example.ruireutov.organiser.DatabaseWorkers;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public class TasksDatabaseWorker implements IDatabaseWorker{
    private DatabaseHelper dbHelper;

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


    public TasksDatabaseWorker(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public Cursor get(String[] vals, String sorting) {
        this.dbHelper.getReadableDatabase();
        Cursor c = null;
        String table = TABLE_TASKS
                + " LEFT JOIN " + TABLE_CATEGORIES + " ON " + TABLE_TASKS + "." + KEY_CATEGORY_ID + " = " + TABLE_CATEGORIES+ "." + KEY_ID
                + " LEFT JOIN " + TABLE_PRIORITIES + " ON " + TABLE_TASKS + "." + KEY_PRIORITY_ID + " = " + TABLE_PRIORITIES+ "." + KEY_ID;

        //TODO add column parser in the future
        String columns[] = {
                TABLE_TASKS + "." + KEY_NAME + " AS Name ",
                TABLE_TASKS + "." + KEY_DETAILS + " AS Details ",
                TABLE_TASKS + "." + KEY_START + " AS StartDate ",
                TABLE_TASKS + "." + KEY_END + " AS EndDate ",
                TABLE_TASKS + "." + KEY_STATUS + " AS Status ",
                TABLE_CATEGORIES + "." + KEY_NAME + " AS Category ",
                TABLE_PRIORITIES + "." + KEY_NAME + " AS Priority "
        };
        this.dbHelper.close();
        return null;
    }

    @Override
    public void insert(String[] params) {

    }

    @Override
    public void update(int id, String[] params) {

    }

    @Override
    public void delete(int id) {

    }
}

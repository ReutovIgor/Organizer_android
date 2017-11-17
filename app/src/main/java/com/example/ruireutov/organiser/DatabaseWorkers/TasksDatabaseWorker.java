package com.example.ruireutov.organiser.DatabaseWorkers;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public class TasksDatabaseWorker implements IDatabaseWorker{
    //private DatabaseHelper dbHelper;

    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PRIORITIES = "priorities_table";
    private static final String TABLE_TASKS = "tasks_table";

    //Database common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DETAILS = "details";

    //Database Tasks table column names
    private static final String KEY_START = "startTime";
    private static final String KEY_END = "endTime";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_PRIORITY_ID = "priority_id";


    public TasksDatabaseWorker(Context context) {
        //this.dbHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public Cursor get(String[] vals, String sorting) {
        Cursor c = null;
        String table = TABLE_TASKS
                + " LEFT OUTER JOIN " + TABLE_CATEGORIES + " ON " + TABLE_TASKS + "." + KEY_CATEGORY_ID + " = " + TABLE_CATEGORIES+ "." + KEY_ID
                + " LEFT OUTER JOIN " + TABLE_PRIORITIES + " ON " + TABLE_TASKS + "." + KEY_PRIORITY_ID + " = " + TABLE_PRIORITIES+ "." + KEY_ID;

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
        //c = this.dbHelper.get(table, columns);
        //c = db.query(table, columns, null, null, null, null, null);
//        if(c != null) {
//            if(c.moveToFirst()) {
//                String str = "";
//                for (String cn : c.getColumnNames()) {
//                    str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
//                }
//            }
//        }

        return c;
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

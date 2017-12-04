package com.example.ruireutov.organiser;

import android.database.Cursor;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

import java.io.Serializable;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public class TaskDetailsData implements Serializable{
    public static final String TASK_DETAILS_NAME = "TaskData";
    private String name;
    private String status;
    private String priority;
    private String category;
    private String details;

    public TaskDetailsData(Cursor cursor) {
        this.name = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TODO_LIST_NAME) );
        this.status = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TODO_LIST_STATUS) );
        this.priority = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TODO_LIST_PRIORITY) );
        this.category = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TODO_LIST_CATEGORY) );
        this.details = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TODO_LIST_DETAILS) );
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public String getCategory() {
        return category;
    }
}

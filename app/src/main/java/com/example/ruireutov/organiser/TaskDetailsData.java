package com.example.ruireutov.organiser;

import android.database.Cursor;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public class TaskDetailsData implements Serializable{
    public static final String TASK_DETAILS_NAME = "TaskData";
    private String name;
    private String dateFrom;
    private String dateTo;
    private String status;
    private String priority;
    private String priorityMark;
    private String priorityColor;
    private String category;
    private String categoryIcon;
    private String categoryColor;
    private String details;

    public TaskDetailsData(Cursor cursor) {
        this.name           = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME) );
        this.status         = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_STATUS) );
        this.dateFrom       = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_START) );
        this.dateTo         = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_END) );
        this.priority       = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY) );
        this.priorityMark   = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_MARK) );
        this.priorityColor  = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_COLOR) );
        this.category       = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY) );
        this.categoryIcon   = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON) );
        this.categoryColor  = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR) );
        this.details        = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_DETAILS) );
    }

    public TaskDetailsData(HashMap<String, String> taskDetails) {
        this.name           = taskDetails.get(DatabaseDefines.TASK_LIST_NAME);
        this.status         = taskDetails.get(DatabaseDefines.TASK_LIST_STATUS);
        this.dateFrom       = taskDetails.get(DatabaseDefines.TASK_LIST_START);
        this.dateTo         = taskDetails.get(DatabaseDefines.TASK_LIST_END);
        this.priority       = taskDetails.get(DatabaseDefines.TASK_LIST_PRIORITY);
        this.priorityColor  = taskDetails.get(DatabaseDefines.TASK_LIST_PRIORITY_COLOR);
        this.category       = taskDetails.get(DatabaseDefines.TASK_LIST_CATEGORY);
        this.categoryColor  = taskDetails.get(DatabaseDefines.TASK_LIST_CATEGORY_COLOR);
        this.details        = taskDetails.get(DatabaseDefines.TASK_LIST_DETAILS);
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getPriority() {
        return priority;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public String getDetails() {
        return details;
    }
}

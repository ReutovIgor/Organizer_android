package com.example.ruireutov.organiser;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Log;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public class TaskDetailsData implements Serializable{
    public static final String TASK_DETAILS_NAME = "TaskData";
    private long id;

    private String name;

    private int status;

    private Date dateStart;
    private Date dateDue;

    private String priority;
    private String priorityMark;
    private String priorityColor;

    private String category;
    private String categoryIcon;
    private String categoryColor;

    private String details;

    private boolean timed;

    public TaskDetailsData() {}

    public TaskDetailsData(Cursor cursor) {
        this.id             = cursor.getLong(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_ID));
        this.name           = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME));
        this.status         = cursor.getInt(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_STATUS));
        this.priority       = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY));
        this.priorityMark   = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_MARK));
        this.priorityColor  = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_COLOR));
        this.category       = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY));
        this.categoryIcon   = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON));
        this.categoryColor  = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR));
        this.details        = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_DETAILS));

        String dateStrStart  = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_START));
        String dateStrEnd  = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_END));
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat fromDb = new SimpleDateFormat("yyyy-MM-DD HH:mm");
            this.dateStart = fromDb.parse(dateStrStart);
            if(dateStrEnd.length() > 0 ) {
                this.dateDue = fromDb.parse(dateStrEnd);
                this.timed = true;
            } else {
                this.dateDue = null;
                this.timed = false;
            }
        } catch (ParseException e) {
            Log.e("TaskDetailsData", "TaskDetails error date parsing error: " + e.toString());
            //TODO: add error handling
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public String getDateStartString(String pattern) {
        if(pattern.length() > 0){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(this.dateStart);

        } else {
            return SimpleDateFormat.getDateTimeInstance().format(this.dateStart);
        }
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public String getDateDueString(String pattern) {
        if(pattern.length() > 0){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(this.dateDue);

        } else {
            return SimpleDateFormat.getDateTimeInstance().format(this.dateDue);
        }
    }

    public void setDateDue(Date dateDue) {
        this.timed = dateDue == null ? false : true;
        this.dateDue = dateDue;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriorityMark() {
        return priorityMark;
    }

    public void setPriorityMark(String priorityMark) {
        this.priorityMark = priorityMark;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean hasDeadline() { return this.timed; }
}

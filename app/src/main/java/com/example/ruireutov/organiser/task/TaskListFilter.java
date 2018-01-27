package com.example.ruireutov.organiser.task;

import android.annotation.SuppressLint;
import android.util.ArraySet;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TaskListFilter {
    public  static  final String TASK_FILTER_STATUS = "status";
    public  static  final String TASK_FILTER_OVERDUE = "overdue";
    public  static  final String TASK_FILTER_TIME_END = "timeEnd";
    public  static  final String TASK_FILTER_CATEGORY = "category";
    public  static  final String TASK_FILTER_PRIORITY = "priority";
    public  static  final String TASK_FILTER_TIME_START = "timeStart";

    private String where;
    private ArrayList<String> selection;
    private boolean overdue;
    private boolean completed;
    private String timeStart;
    private String timeEnd;
    private ArraySet<String> categories;
    private ArraySet<String> priorities;

    public TaskListFilter(boolean overdue, boolean completed, String timeStart, String timeEnd, ArraySet<String> categories, ArraySet<String> priorities) {
        this.overdue = overdue;
        this.completed = completed;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.categories = categories;
        this.priorities = priorities;
    }

    public void generateQueryParams(HashMap<String, String> map) {
        this.where = "";
        this.selection = new ArrayList<>();
        this.parseTimeEndFilters(map.get(TaskListFilter.TASK_FILTER_TIME_END));
        this.parseCompletedFilters(map.get(TaskListFilter.TASK_FILTER_STATUS));
    }

    private void parseTimeEndFilters(String key) {
        String concatStr = "";
        if(this.overdue && this.timeEnd == "") {
            return;
        } else if(this.where.length() > 0 ) {
            concatStr = "AND";
        }
        String timeSelection = "";
        if(!this.overdue) {
            timeSelection +=  key + " > ? ";
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DatabaseDefines.DB_DATE_TIME_FORMAT);
            String dateStr = dateFormatter.format(calendar.getTime());
            this.selection.add(dateStr);
        }

        if(this.timeEnd.length() > 0) {

        }

        //this.where += timeSelection.length() > 2 ? concatStr + timeSelection : "";
        this.where += concatStr + " ( " + timeSelection + " ) ";
    }

    private void parseCompletedFilters(String key) {
        String concatStr = "";
        if(this.completed) {
            return;
        } else if(this.where.length() > 0 ) {
            concatStr = "AND";
        }

        this.where += concatStr + " ( " + key + " != ? ) ";
        this.selection.add(Integer.toString(DatabaseDefines.TASK_STATUS_COMPLETED));
    }

    public String getWhere() {
        return this.where;
    }

    public String[] getSelection() {
        return this.selection.toArray(new String[this.selection.size()]);
    }
}

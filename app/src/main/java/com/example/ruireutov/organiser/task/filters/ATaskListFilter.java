package com.example.ruireutov.organiser.task.filters;

import android.annotation.SuppressLint;
import android.util.ArraySet;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

public abstract class ATaskListFilter implements ITaskListFilter {
    public  static  final String TASK_FILTER_STATUS = "status";
    //public  static  final String TASK_FILTER_OVERDUE = "overdue";
    public  static  final String TASK_FILTER_TIME_END = "timeEnd";
    public  static  final String TASK_FILTER_CATEGORY = "category";
    public  static  final String TASK_FILTER_PRIORITY = "priority";
    public  static  final String TASK_FILTER_TIME_START = "timeStart";

    protected String where;
    protected ArrayList<String> selection;
    protected boolean overdue;
    protected boolean completed;
    protected String timeStart;
    protected String timeEnd;
    protected Set<String> categories;
    protected Set<String> priorities;

    public ATaskListFilter(boolean overdue, boolean completed, String timeStart, String timeEnd, Set<String> categories, Set<String> priorities) {
        this.overdue = overdue;
        this.completed = completed;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.categories = categories;
        this.priorities = priorities;
    }

    protected void parseTimeEndFilters(String key) {
        String concatStr = "";
        if(this.overdue && Objects.equals(this.timeEnd, "")) {
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

        this.where += concatStr + " ( " + timeSelection + " ) ";
    }

    protected void parseCompletedFilters(String key) {
        String concatStr = "";
        if(this.completed) {
            return;
        } else if(this.where.length() > 0 ) {
            concatStr = "AND";
        }

        this.where += concatStr + " ( " + key + " != ? ) ";
        this.selection.add(Integer.toString(DatabaseDefines.TASK_STATUS_COMPLETED));
    }

    protected void parseCategoriesFilters(String key) {

    }

    protected void parsePrioritiesFilters(String key) {

    }

    @Override
    public String getWhere() {
        return this.where;
    }

    @Override
    public String[] getSelection() {
        return this.selection.toArray(new String[this.selection.size()]);
    }
}

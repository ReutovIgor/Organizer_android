package com.example.ruireutov.organiser.task.filters;

import android.annotation.SuppressLint;

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

    StringBuilder where;
    ArrayList<String> selection;
    private boolean overdue;
    private boolean completed;
    private String timeStart;
    private String timeEnd;
    private Set<String> categories;
    private Set<String> priorities;

    ATaskListFilter(boolean overdue, boolean completed, String timeStart, String timeEnd, Set<String> categories, Set<String> priorities) {
        this.where = new StringBuilder();
        this.overdue = overdue;
        this.completed = completed;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.categories = categories;
        this.priorities = priorities;
    }

    void parseTimeEndFilters(String key) {
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

        this.where.append(concatStr).append(" ( ").append(timeSelection).append(" ) ");
    }

    void parseCompletedFilters(String key) {
        String concatStr = "";
        if(this.completed) {
            return;
        } else if(this.where.length() > 0 ) {
            concatStr = "AND";
        }

        this.where.append(concatStr).append(" ( ").append(key).append(" != ? ) ");
        this.selection.add(Integer.toString(DatabaseDefines.TASK_STATUS_COMPLETED));
    }

    void parseCategoriesFilters(String key) {
        String concatStr = "";
        if(this.categories.size() == 0) {
            return;
        } else if(this.where.length() > 0 ) {
            concatStr = "AND";
        }

        String concat = "";
        StringBuilder categorySelection = new StringBuilder();
        for (String item : this.categories) {
            categorySelection.append(concat).append(key).append(" = ? ");
            concat = "OR ";
            this.selection.add(item);
        }

        this.where.append(concatStr).append(" ( ").append(categorySelection.toString()).append(" ) ");
    }

    void parsePrioritiesFilters(String key) {
        String concatStr = "";
        if(this.priorities.size() == 0) {
            return;
        } else if(this.where.length() > 0 ) {
            concatStr = "AND";
        }

        String concat = "";
        StringBuilder prioritySelection = new StringBuilder();
        for (String item : this.priorities) {
            prioritySelection.append(concat).append(key).append(" = ? ");
            concat = "OR ";
            this.selection.add(item);
        }

        this.where.append(concatStr).append(" ( ").append(prioritySelection.toString()).append(" ) ");
    }

    @Override
    public String getWhere() {
        return this.where.toString();
    }

    @Override
    public String[] getSelection() {
        return this.selection.toArray(new String[this.selection.size()]);
    }
}

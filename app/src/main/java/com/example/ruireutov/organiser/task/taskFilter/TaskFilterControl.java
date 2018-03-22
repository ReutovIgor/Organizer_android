package com.example.ruireutov.organiser.task.taskFilter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.filters.CategoriesFilter;
import com.example.ruireutov.organiser.task.filters.ITaskListFilter;
import com.example.ruireutov.organiser.task.filters.PrioritiesFilter;
import com.example.ruireutov.organiser.task.main.ITaskActivityTaskFilterControl;

public class TaskFilterControl implements ITaskFilterControl {

    private ITaskFilterUIControl uiControl;
    private ITaskActivityTaskFilterControl taskActivityControl;
    private DatabaseControl dbControl;
    private FilterData filterData;

    TaskFilterControl(Context context, ITaskFilterUIControl uiControl, ITaskActivityTaskFilterControl taskActivityControl) {
        this.uiControl = uiControl;
        this.taskActivityControl = taskActivityControl;

        this.dbControl = DatabaseControl.getInstance(context);
        this.dbControl.open();

        this.filterData = new FilterData(this.uiControl.getSharedPreferences());
        this.getCategories();
        this.uiControl.updateCategorySelection(this.filterData.getCategories());
        this.getPriorities();
        this.uiControl.updatePrioritySelection(this.filterData.getPriorities());

        this.getTaskFilters();
    }

    private void getPriorities() {
        Cursor c = dbControl.getPriorities();
        if(c != null) {
            this.uiControl.fillPriorities(c);
        }
    }

    private void getPriorityFilters() {
        ITaskListFilter filter = new PrioritiesFilter(
                this.filterData.showOverdueTasks(),
                this.filterData.showCompletedTasks(),
                this.filterData.getTaskStartDateStr(),
                this.filterData.getTaskEndDateStr(),
                this.filterData.getCategories(),
                this.filterData.getPriorities()
        );

        Cursor c = dbControl.getPrioritiesFilter(filter);
        if(c != null) {
            this.uiControl.updatePriorityFilters(c);
        }
    }

    private void getCategories() {
        Cursor c = dbControl.getCategories();
        if(c != null) {
            this.uiControl.fillCategories(c);
        }
    }

    private void getCategoryFilters() {
        ITaskListFilter filter = new CategoriesFilter(
                this.filterData.showOverdueTasks(),
                this.filterData.showCompletedTasks(),
                this.filterData.getTaskStartDateStr(),
                this.filterData.getTaskEndDateStr(),
                this.filterData.getCategories(),
                this.filterData.getPriorities()
        );

        Cursor c = dbControl.getCategoriesFilter(filter);
        if(c != null) {
            this.uiControl.updateCategoryFilters(c);
        }
    }

    @Override
    public void getTaskFilters() {
        this.uiControl.updateEndByDate(this.filterData.getTaskEndDate());
        this.uiControl.updateShowOverdue(this.filterData.showOverdueTasks());
        this.uiControl.updateShowCompleted(this.filterData.showCompletedTasks());
        this.getCategoryFilters();
        this.getPriorityFilters();
    }

    @Override
    public void getNewCategories() {
        this.getCategories();
    }

    @Override
    public void setShowOverdue(boolean state) {
        this.filterData.setShowOverdue(state);
        //this.uiControl.updateShowOverdue(this.filterData.showOverdueTasks());
        this.getPriorityFilters();
        this.getCategoryFilters();
    }

    @Override
    public void setShowCompleted(boolean state) {
        this.filterData.setShowCompleted(state);
        //this.uiControl.updateShowCompleted(this.filterData.showCompletedTasks());
        this.getPriorityFilters();
        this.getCategoryFilters();
    }

    @Override
    public void setEndByDate(Date date) {
        this.filterData.setTaskEndDate(date);
        this.getPriorityFilters();
        this.getCategoryFilters();
    }

    @Override
    public void addCategory(String name) {
        this.filterData.addCategory(name);
        this.getPriorityFilters();
    }

    @Override
    public void removeCategory(String name) {
        this.filterData.removeCategory(name);
        this.getPriorityFilters();
    }

    @Override
    public void addPriority(String name) {
        this.filterData.addPriority(name);
        this.getCategoryFilters();
    }

    @Override
    public void removePriority(String name) {
        this.filterData.removePriority(name);
        this.getCategoryFilters();
    }

    @Override
    public void saveNewFilters() {
        this.filterData.saveFilters(this.uiControl.getSharedPreferences());
        this.taskActivityControl.showFiltersApplied(this.filterData.filterApplied());
        this.taskActivityControl.onTaskListUpdate();
        this.taskActivityControl.showTaskList();
    }

    @Override
    public void removeNewFilters() {
        this.filterData.getFilters(this.uiControl.getSharedPreferences());
        this.getTaskFilters();
        this.taskActivityControl.showTaskList();
    }

    @Override
    public void resetFilters() {
        this.filterData.resetFilters();
        this.getTaskFilters();
    }

    private class FilterData {
        boolean showOverdue;
        boolean showCompleted;
        String taskStartDate;
        String taskEndDate;
        Set<String> selectedCategories;
        Set<String> selectedPriorities;
        SimpleDateFormat dateFormat;

        FilterData(SharedPreferences preferences) {
            this.dateFormat = new SimpleDateFormat(DatabaseDefines.DB_DATE_TIME_FORMAT);
            this.getFilters(preferences);
        }

        void getFilters(SharedPreferences preferences) {
            this.showOverdue = preferences.getBoolean(TaskDefines.SHOW_OVERDUE, false);
            this.showCompleted = preferences.getBoolean(TaskDefines.SHOW_COMPLETED, false);
            this.taskStartDate = preferences.getString(TaskDefines.TASK_STARTS, "");
            this.taskEndDate = preferences.getString(TaskDefines.TASK_ENDS, "");
            this.selectedCategories = preferences.getStringSet(TaskDefines.SELECTED_CATEGORIES, new HashSet<String>());
            this.selectedPriorities = preferences.getStringSet(TaskDefines.SELECTED_PRIORITIES, new HashSet<String>());
        }

        void resetFilters() {
            this.showOverdue = false;
            this.showCompleted = false;
            this.taskStartDate = "";
            this.taskEndDate = "";
            this.selectedCategories.clear();
            this.selectedPriorities.clear();
        }

        void saveFilters(SharedPreferences preferences) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(TaskDefines.SHOW_OVERDUE, this.showOverdue);
            editor.putBoolean(TaskDefines.SHOW_COMPLETED, this.showCompleted);
            editor.putString(TaskDefines.TASK_STARTS, this.taskStartDate);
            editor.putString(TaskDefines.TASK_ENDS, this.taskEndDate);
            editor.putStringSet(TaskDefines.SELECTED_PRIORITIES, this.selectedPriorities);
            editor.putStringSet(TaskDefines.SELECTED_CATEGORIES, this.selectedCategories);
            editor.apply();
        }

        boolean showOverdueTasks() {
            return this.showOverdue;
        }

        void setShowOverdue(boolean state) {
            this.showOverdue = state;
        }

        boolean showCompletedTasks() {
            return this.showCompleted;
        }

        void setShowCompleted(boolean state) {
            this.showCompleted = state;
        }

        Date getTaskStartDate() {
            try {
                return this.dateFormat.parse(this.taskStartDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        String getTaskStartDateStr() {
            return this.taskStartDate;
        }

        void setTaskStartDate(Date taskStartDate) {
            this.taskStartDate = this.dateFormat.format(taskStartDate);
        }

        Date getTaskEndDate() {
            try {
                return this.dateFormat.parse(this.taskEndDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        String getTaskEndDateStr() {
            return this.taskEndDate;
        }

        void setTaskEndDate(Date taskEndDate) {
            Calendar c = Calendar.getInstance();
            c.setTime(taskEndDate);
            c.set(Calendar.HOUR, 23);
            c.set(Calendar.MINUTE, 59);
            this.taskEndDate = this.dateFormat.format(c.getTime());
        }

        Set<String> getCategories() {
            return selectedCategories;
        }

        void addCategory(String name) {
            if(!this.selectedCategories.contains(name)) {
                this.selectedCategories.add(name);
            }
        }

        void removeCategory(String name) {
            this.selectedCategories.remove(name);
        }

        Set<String> getPriorities() {
            return selectedPriorities;
        }

        void addPriority(String name) {
            if(!this.selectedPriorities.contains(name)) {
                this.selectedPriorities.add(name);
            }
        }

        void removePriority(String name) {
            this.selectedPriorities.remove(name);
        }

        boolean filterApplied() {
            return !(!this.showOverdue &&
                    !this.showCompleted &&
                    this.taskStartDate.length() == 0 &&
                    this.taskEndDate.length() == 0 &&
                    this.selectedPriorities.size() == 0 &&
                    this.selectedCategories.size() == 0);
        }
    }
}

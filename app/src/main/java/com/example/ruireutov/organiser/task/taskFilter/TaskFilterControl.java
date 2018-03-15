package com.example.ruireutov.organiser.task.taskFilter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.HashSet;
import java.util.Set;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseControl;
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

        this.getTaskFilters();
    }

    private void getPriorityFilters() {
        ITaskListFilter filter = new PrioritiesFilter(
                this.filterData.showOverdueTasks(),
                this.filterData.showCompletedTasks(),
                this.filterData.getTaskStartDate(),
                this.filterData.getTaskEndDate(),
                this.filterData.getCategories(),
                this.filterData.getPriorities()
        );

        Cursor c = dbControl.getPrioritiesFilter(filter);
        if(c != null) {
            this.uiControl.updatePriorityFilters(c);
        }
    }

    private void getCategoryFilters() {
        ITaskListFilter filter = new CategoriesFilter(
                this.filterData.showOverdueTasks(),
                this.filterData.showCompletedTasks(),
                this.filterData.getTaskStartDate(),
                this.filterData.getTaskEndDate(),
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
        this.filterData = new FilterData(this.uiControl.getSharedPreferences());
        this.uiControl.updateShowOverdue(this.filterData.showOverdueTasks());
        this.uiControl.updateShowCompleted(this.filterData.showCompletedTasks());
        this.getCategoryFilters();
        this.getPriorityFilters();
    }

    @Override
    public void setShowOverdue(boolean state) {
        this.filterData.setShowOverdue(state);
    }

    @Override
    public void setShowCompleted(boolean state) {
        this.filterData.setShowCompleted(state);
    }

    @Override
    public void setEndByDate(String date) {
        this.filterData.setTaskEndDate(date);
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
        SharedPreferences preferences = this.uiControl.getSharedPreferences();
        Set<String> selectedPriorities = this.uiControl.getSelectedPriorities();
        Set<String> selectedCategories = this.uiControl.getSelectedCategories();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(TaskDefines.SELECTED_PRIORITIES, selectedPriorities);
        editor.putStringSet(TaskDefines.SELECTED_CATEGORIES, selectedCategories);
        editor.apply();

        this.taskActivityControl.onTaskListUpdate();
    }

    @Override
    public void hideFilters() {
        this.taskActivityControl.showTaskList();
    }

    private class FilterData {
        boolean showOverdue;
        boolean showCompleted;
        String taskStartDate;
        String taskEndDate;
        Set<String> selectedCategories;
        Set<String> selectedPriorities;

        FilterData(SharedPreferences preferences) {
            this.showOverdue = preferences.getBoolean(TaskDefines.SHOW_OVERDUE, false);
            this.showCompleted = preferences.getBoolean(TaskDefines.SHOW_COMPLETED, false);
            this.taskStartDate = preferences.getString(TaskDefines.TASK_STARTS, "");
            this.taskEndDate = preferences.getString(TaskDefines.TASK_ENDS, "");
            this.selectedCategories = preferences.getStringSet(TaskDefines.SELECTED_CATEGORIES, new HashSet<String>());
            this.selectedPriorities = preferences.getStringSet(TaskDefines.SELECTED_PRIORITIES, new HashSet<String>());
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

        String getTaskStartDate() {
            return taskStartDate;
        }

        void setTaskStartDate(String taskStartDate) {
            this.taskStartDate = taskStartDate;
        }

        String getTaskEndDate() {
            return taskEndDate;
        }

        void setTaskEndDate(String taskEndDate) {
            this.taskEndDate = taskEndDate;
        }

        Set<String> getCategories() {
            return selectedCategories;
        }

        void addCategory(String name) {
            this.selectedCategories.add(name);
        }

        void removeCategory(String name) {
            this.selectedCategories.remove(name);
        }

        Set<String> getPriorities() {
            return selectedPriorities;
        }

        void addPriority(String name) {
            this.selectedPriorities.add(name);
        }

        void removePriority(String name) {
            this.selectedPriorities.remove(name);
        }
    }
}

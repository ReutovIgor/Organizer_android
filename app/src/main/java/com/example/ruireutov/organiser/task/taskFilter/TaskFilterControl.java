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

    public TaskFilterControl(Context context, ITaskFilterUIControl uiControl, ITaskActivityTaskFilterControl taskActivityControl) {
        this.uiControl = uiControl;
        this.taskActivityControl = taskActivityControl;

        this.dbControl = DatabaseControl.getInstance(context);
        this.dbControl.open();
    }

    @Override
    public void getPriorityFilters(SharedPreferences preferences) {
        ITaskListFilter filter = new PrioritiesFilter(
            preferences.getBoolean(TaskDefines.SHOW_OVERDUE, false),
            preferences.getBoolean(TaskDefines.SHOW_COMPLETED, false),
            preferences.getString(TaskDefines.TASK_STARTS, ""),
            preferences.getString(TaskDefines.TASK_ENDS, ""),
            preferences.getStringSet(TaskDefines.SELECTED_CATEGORIES, new HashSet<String>()),
            preferences.getStringSet(TaskDefines.SELECTED_PRIORITIES, new HashSet<String>())
        );

        Cursor c = dbControl.getPrioritiesFilter(filter);
        if(c != null) {
            this.uiControl.updatePriorityFilters(c);
        }
    }

    @Override
    public void getCategoryFilters(SharedPreferences preferences) {
        ITaskListFilter filter = new CategoriesFilter(
            preferences.getBoolean(TaskDefines.SHOW_OVERDUE, false),
            preferences.getBoolean(TaskDefines.SHOW_COMPLETED, false),
            preferences.getString(TaskDefines.TASK_STARTS, ""),
            preferences.getString(TaskDefines.TASK_ENDS, ""),
            preferences.getStringSet(TaskDefines.SELECTED_CATEGORIES, new HashSet<String>()),
            preferences.getStringSet(TaskDefines.SELECTED_PRIORITIES, new HashSet<String>())
        );

        Cursor c = dbControl.getCategoriesFilter(filter);
        if(c != null) {
            this.uiControl.updateCategoryFilters(c);
        }
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
}

package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.Set;


public interface ITaskFilterUIControl {
    void updateShowOverdue(boolean state);
    void updateShowCompleted(boolean state);
    void fillCategories(Cursor c, Set<String> selectedItems);
    void updateCategoryFilters(Cursor c);
    void fillPriorities(Cursor c, Set<String> selectedItems);
    void updatePriorityFilters(Cursor c);
    SharedPreferences getSharedPreferences();
}

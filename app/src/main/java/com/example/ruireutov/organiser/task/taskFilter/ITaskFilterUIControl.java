package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.Set;

/**
 * Created by ruireutov on 05-Mar-18.
 */

public interface ITaskFilterUIControl {
    void updatePriorityFilters(Cursor c);
    void updateCategoryFilters(Cursor c);
    SharedPreferences getSharedPreferences();
    Set<String> getSelectedPriorities();
    Set<String> getSelectedCategories();
}

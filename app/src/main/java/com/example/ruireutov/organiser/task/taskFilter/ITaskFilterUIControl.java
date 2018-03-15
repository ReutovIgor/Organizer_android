package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.Set;

/**
 * Created by ruireutov on 05-Mar-18.
 */

public interface ITaskFilterUIControl {
    void updateShowOverdue(boolean state);
    void updateShowCompleted(boolean state);
    void updateCategoryFilters(Cursor c);
    void updatePriorityFilters(Cursor c);
    SharedPreferences getSharedPreferences();
    Set<String> getSelectedPriorities();
    Set<String> getSelectedCategories();
}

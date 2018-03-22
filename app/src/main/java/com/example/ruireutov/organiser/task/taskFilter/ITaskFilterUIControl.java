package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.Date;
import java.util.Set;


public interface ITaskFilterUIControl {
    void updateShowOverdue(boolean state);
    void updateShowCompleted(boolean state);
    void fillCategories(Cursor c);
    void updateEndByDate(Date date);
    void updateCategoryFilters(Cursor c);
    void updateCategorySelection(Set<String> selectedItems);
    void fillPriorities(Cursor c);
    void updatePriorityFilters(Cursor c);
    void updatePrioritySelection(Set<String> selectedItems);
    SharedPreferences getSharedPreferences();
}

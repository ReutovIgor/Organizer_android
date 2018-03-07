package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;


public interface ITaskFilterControl {
    void getPriorityFilters(SharedPreferences settings);
    void getCategoryFilters(SharedPreferences settings);
    void saveNewFilters();
    void hideFilters();
}

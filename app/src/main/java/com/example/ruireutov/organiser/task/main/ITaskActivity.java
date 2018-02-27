package com.example.ruireutov.organiser.task.main;

import com.example.ruireutov.organiser.task.TaskDetailsData;

/**
 * Created by ruireutov on 01-Feb-18.
 */

public interface ITaskActivity {
    void onTaskListUpdate();
    void showDetails(TaskDetailsData data);
    void showTaskList();
    void showTaskCreation();
    void showFilters();
    void showCategories();
}

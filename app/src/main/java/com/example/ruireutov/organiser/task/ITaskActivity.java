package com.example.ruireutov.organiser.task;

/**
 * Created by ruireutov on 01-Feb-18.
 */

public interface ITaskActivity {
    void showDetails(TaskDetailsData data);
    void showTaskList();
    void showTaskCreation();
    void showFilters();
    void showCategories();
}

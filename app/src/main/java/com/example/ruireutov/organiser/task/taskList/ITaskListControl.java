package com.example.ruireutov.organiser.task.taskList;

import com.example.ruireutov.organiser.task.filters.TasksFilter;

public interface ITaskListControl {
    void newTask();
    void showDetails(int position);
    void onDestroy();
    void getTaskList(TasksFilter filter);
}

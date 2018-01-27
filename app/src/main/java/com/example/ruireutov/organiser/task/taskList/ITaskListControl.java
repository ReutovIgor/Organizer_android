package com.example.ruireutov.organiser.task.taskList;

import com.example.ruireutov.organiser.task.TaskListFilter;

import java.util.HashMap;

public interface ITaskListControl {
    void newTask();
    void showDetails(int position);
    void onDestroy();
    void getTaskList(TaskListFilter filter);
}

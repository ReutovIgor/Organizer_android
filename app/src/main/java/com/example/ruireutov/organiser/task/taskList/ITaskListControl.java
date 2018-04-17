package com.example.ruireutov.organiser.task.taskList;

import com.example.ruireutov.organiser.task.TaskDetailsData;

public interface ITaskListControl {
    void newTask();
    void showDetails(TaskDetailsData data);
    void closeTask(TaskDetailsData data, int pos);
    void removeTask(TaskDetailsData data, int pos);
    void onDestroy();
    void getTaskList();
}

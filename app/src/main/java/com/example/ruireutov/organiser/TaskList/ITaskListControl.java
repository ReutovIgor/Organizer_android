package com.example.ruireutov.organiser.TaskList;

/**
 * Created by ruireutov on 29-Nov-17.
 */

public interface ITaskListControl {
    void newTask();
    void showDetails(int position);
    void onDestroy();
    void getTaskList();
}

package com.example.ruireutov.organiser.TaskList;

import android.content.SharedPreferences;

public interface ITaskListControl {
    void newTask();
    void showDetails(int position);
    void onDestroy();
    void getTaskList(SharedPreferences prefs);
}

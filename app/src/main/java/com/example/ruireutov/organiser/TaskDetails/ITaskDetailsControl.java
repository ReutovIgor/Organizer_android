package com.example.ruireutov.organiser.TaskDetails;

import android.content.Intent;

import com.example.ruireutov.organiser.TaskDetailsData;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public interface ITaskDetailsControl {
    void parseIntentData(Intent intent);
    void addTask(TaskDetailsData data);
    void update(TaskDetailsData data);
    void onDestroy();
}

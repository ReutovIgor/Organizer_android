package com.example.ruireutov.organiser.TaskDetails;

import android.database.Cursor;

import com.example.ruireutov.organiser.TaskDetailsData;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public interface ITaskDetailsUIControl {
    void showTaskDetails(TaskDetailsData data);
    void showTaskCreation();
    void fillCategories(Cursor cursor);
    void fillPriorities(Cursor cursor);
    void toggleDataEdit(boolean blocked);
}

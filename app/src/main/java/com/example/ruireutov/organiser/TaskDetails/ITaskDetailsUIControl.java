package com.example.ruireutov.organiser.TaskDetails;

import com.example.ruireutov.organiser.TaskDetailsData;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public interface ITaskDetailsUIControl {
    void showTaskDetails(TaskDetailsData data);
    void showTaskCreation();
    void toggleDataEdit(boolean blocked);
}

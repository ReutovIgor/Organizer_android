package com.example.ruireutov.organiser.TaskDetails;

import android.content.Context;
import android.content.Intent;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.TaskDetailsData;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public class TaskDetailsControl implements ITaskDetailsControl{
    private Context context;
    private ITaskDetailsUIControl uiControl;
    private DatabaseControl dbControl;

    public TaskDetailsControl(Context context) {
        this.context = context;
        this.uiControl = (ITaskDetailsUIControl) context;
        this.dbControl = DatabaseControl.getInstance(this.context);
        dbControl.open();
    }

    public void parseIntentData(Intent intent) {
        if(intent.hasExtra(TaskDetailsData.TASK_DETAILS_NAME)) {
            TaskDetailsData taskData = (TaskDetailsData) intent.getSerializableExtra(TaskDetailsData.TASK_DETAILS_NAME);
            this.uiControl.showTaskDetails(taskData);
        } else {
            this.uiControl.showTaskCreation();
        }
    }

    public void addTask(TaskDetailsData data) {

    }

    public void update(TaskDetailsData data) {

    }

    public void onDestroy() {
        this.dbControl.close();
    }
}

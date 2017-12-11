package com.example.ruireutov.organiser.TaskDetails;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.TaskDetailsData;

public class TaskDetailsControl implements ITaskDetailsControl{
    private Context context;
    private ITaskDetailsUIControl uiControl;
    private DatabaseControl dbControl;

    public TaskDetailsControl(Context context) {
        this.context = context;
        this.uiControl = (ITaskDetailsUIControl) context;
        this.dbControl = DatabaseControl.getInstance(this.context);
        dbControl.open();
        this.uiControl.fillPriorities(this.dbControl.getPriorities());
        this.uiControl.fillCategories(this.dbControl.getCategories());
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
        this.dbControl.addTask(data);
    }

    public void update(TaskDetailsData data) {

    }

    public void onDestroy() {
        this.dbControl.close();
    }
}

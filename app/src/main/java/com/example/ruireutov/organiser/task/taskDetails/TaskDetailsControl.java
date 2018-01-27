package com.example.ruireutov.organiser.task.taskDetails;

import android.content.Context;
import android.content.Intent;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.task.TaskDetailsData;
import com.example.ruireutov.organiser.task.taskList.TaskListActivity;

import java.util.Calendar;
import java.util.Date;

public class TaskDetailsControl implements ITaskDetailsControl{
    private Context context;
    private ITaskDetailsUIControl uiControl;
    private DatabaseControl dbControl;
    private TaskDetailsData taskDetailsData;

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
            this.taskDetailsData = (TaskDetailsData) intent.getSerializableExtra(TaskDetailsData.TASK_DETAILS_NAME);
            this.uiControl.showTaskDetails(this.taskDetailsData);
        } else {
            this.taskDetailsData = new TaskDetailsData();
            this.uiControl.showTaskCreation();
        }
    }

    public void addTask() {
        this.taskDetailsData.setDateStart(Calendar.getInstance().getTime());
        this.taskDetailsData.setStatus(DatabaseDefines.TASK_STATUS_IN_PROGRESS);
        this.dbControl.addTask(this.taskDetailsData);
        Intent intent = new Intent(this.context, TaskListActivity.class);
        this.context.startActivity(intent);
    }

    public void updateTask() {
        this.dbControl.updateTask(this.taskDetailsData);
        Intent intent = new Intent(this.context, TaskListActivity.class);
        this.context.startActivity(intent);
    }

    public void closeTask() {
        this.dbControl.closeTask(this.taskDetailsData.getId());
        Intent intent = new Intent(this.context, TaskListActivity.class);
        this.context.startActivity(intent);
    }

    public void deleteTask() {
        this.dbControl.removeTask(this.taskDetailsData.getId());
        Intent intent = new Intent(this.context, TaskListActivity.class);
        this.context.startActivity(intent);
    }

    @Override
    public void setName(String str) {
        this.taskDetailsData.setName(str);
    }

    @Override
    public void setDueDate(Date date) {
        this.taskDetailsData.setDateDue(date);
    }

    @Override
    public void setDetails(String str) {
        this.taskDetailsData.setDetails(str);
    }

    @Override
    public void setCategory(String str) {
        this.taskDetailsData.setCategory(str);
    }

    @Override
    public void setPriority(String str) {
        this.taskDetailsData.setPriority(str);
    }

    public void onDestroy() {
        this.dbControl.close();
    }
}

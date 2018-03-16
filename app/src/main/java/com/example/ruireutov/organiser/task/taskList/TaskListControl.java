package com.example.ruireutov.organiser.task.taskList;

import android.content.Context;
import android.database.Cursor;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.task.main.ITaskActivity;
import com.example.ruireutov.organiser.task.filters.TasksFilter;
import com.example.ruireutov.organiser.task.TaskDetailsData;


public class TaskListControl implements ITaskListControl {
    private Context context;
    private ITaskListUiControl uiControl;
    private DatabaseControl dbControl;
    private Cursor listData;
    private ITaskActivity taskActivity;


    TaskListControl(Context context, ITaskListUiControl uiControl, ITaskActivity taskActivity){
        this.context = context;
        this.uiControl = uiControl;
        this.taskActivity = taskActivity;
        this.dbControl = DatabaseControl.getInstance(this.context);
        this.dbControl.open();
    }

    @Override
    public void newTask() {
        this.taskActivity.showTaskCreation();
    }

    @Override
    public void showDetails(int position) {
        if(this.listData.moveToPosition(position)) {
            TaskDetailsData data = new TaskDetailsData(this.listData);
            this.taskActivity.showDetails(data);
        }
    }

    @Override
    public void onDestroy() {
        this.listData.close();
        this.dbControl.close();
    }

    @Override
    public void getTaskList(TasksFilter filter) {
        this.listData = this.dbControl.getTasks(filter);
        this.uiControl.updateList(this.listData);
    }
}

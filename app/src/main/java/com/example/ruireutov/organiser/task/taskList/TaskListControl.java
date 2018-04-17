package com.example.ruireutov.organiser.task.taskList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.main.ITaskActivity;
import com.example.ruireutov.organiser.task.filters.TaskFilter;
import com.example.ruireutov.organiser.task.TaskDetailsData;

import java.util.HashSet;


public class TaskListControl implements ITaskListControl {
    private ITaskListUiControl uiControl;
    private DatabaseControl dbControl;
    private ITaskActivity taskActivity;


    TaskListControl(Context context, ITaskListUiControl uiControl, ITaskActivity taskActivity){
        this.uiControl = uiControl;
        this.taskActivity = taskActivity;
        this.dbControl = DatabaseControl.getInstance(context);
        this.dbControl.open();
    }

    private Cursor taskListDataRequest() {
        SharedPreferences settings = this.uiControl.getSharedPreferences();
        TaskFilter filter = new TaskFilter(
                settings.getBoolean(TaskDefines.SHOW_OVERDUE, false),
                settings.getBoolean(TaskDefines.SHOW_COMPLETED, false),
                settings.getString(TaskDefines.TASK_STARTS, ""),
                settings.getString(TaskDefines.TASK_ENDS, ""),
                settings.getStringSet(TaskDefines.SELECTED_CATEGORIES, new HashSet<String>()),
                settings.getStringSet(TaskDefines.SELECTED_PRIORITIES, new HashSet<String>())
        );

        return this.dbControl.getTasks(filter);
    }

    @Override
    public void newTask() {
        this.taskActivity.showTaskCreation();
    }

    @Override
    public void showDetails(TaskDetailsData data) {
        this.taskActivity.showDetails(data);
    }

    @Override
    public void closeTask(TaskDetailsData data, int pos) {
        this.dbControl.closeTask(data.getId());
        SharedPreferences settings = this.uiControl.getSharedPreferences();
        boolean showCompleted = settings.getBoolean(TaskDefines.SHOW_COMPLETED, false);
        Cursor c = this.taskListDataRequest();
        if(showCompleted) {
           this.uiControl.onNewListData(c);
        } else {
            this.uiControl.onListItemRemoval(c, pos);
        }
    }

    @Override
    public void removeTask(TaskDetailsData data, int pos) {
        this.dbControl.removeTask(data.getId());
        Cursor c = this.taskListDataRequest();
        this.uiControl.onListItemRemoval(c, pos);
    }

    @Override
    public void onDestroy() {
        this.dbControl.close();
    }

    @Override
    public void getTaskList() {
        this.uiControl.onNewListData(this.taskListDataRequest());
    }
}

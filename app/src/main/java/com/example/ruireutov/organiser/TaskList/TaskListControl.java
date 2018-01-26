package com.example.ruireutov.organiser.TaskList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.TaskDetails.TaskDetailsActivity;
import com.example.ruireutov.organiser.TaskDetailsData;

import java.util.HashMap;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public class TaskListControl implements ITaskListControl {
    private Context context;
    private ITaskListUiControl uiControl;
    private DatabaseControl dbControl;
    private Cursor listData;


    TaskListControl(Context context, ITaskListUiControl uiControl){
        this.context = context;
        this.uiControl = uiControl;
        this.dbControl = DatabaseControl.getInstance(this.context);
        this.dbControl.open();
    }

    @Override
    public void newTask() {
        Intent intent = new Intent(this.context, TaskDetailsActivity.class);
        this.context.startActivity(intent);
    }

    @Override
    public void showDetails(int position) {
        Intent intent = new Intent(this.context, TaskDetailsActivity.class);
        if(this.listData.moveToPosition(position)) {
            TaskDetailsData data = new TaskDetailsData(this.listData);
            intent.putExtra(TaskDetailsData.TASK_DETAILS_NAME, data);
            this.context.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        this.listData.close();
        this.dbControl.close();
    }

    @Override
    public void getTaskList(SharedPreferences prefs) {
        HashMap<String, String[]> filter = new HashMap<>();
        
        this.listData = this.dbControl.getTasks();
        this.uiControl.updateList(this.listData);
    }
}

package com.example.ruireutov.organiser.ToDoList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.TaskDetails.TaskDetailsActivity;
import com.example.ruireutov.organiser.TaskDetailsData;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public class ToDoListControl implements IToDoListControl {
    private Context context;
    private IToDoListUiControl uiControl;
    private DatabaseControl dbControl;
    private Cursor listData;


    ToDoListControl(Context context, IToDoListUiControl uiControl){
        this.context = context;
        this.uiControl = uiControl;
        this.dbControl = DatabaseControl.getInstance(this.context);
        dbControl.open();
        this.listData = this.dbControl.getToDoList();
        this.uiControl.updateList(this.listData);
    }

    @Override
    public void newTask() {
        Intent intent = new Intent(this.context, TaskDetailsActivity.class);
        this.context.startActivity(intent);
    }

    @Override
    public void showDetails(int position) {
        Intent intent = new Intent(this.context, TaskDetailsActivity.class);
        this.listData.moveToPosition(position);
        TaskDetailsData data = new TaskDetailsData(this.listData);
        intent.putExtra(TaskDetailsData.TASK_DETAILS_NAME, data);
        this.context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        this.dbControl.close();
    }
}

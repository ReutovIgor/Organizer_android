package com.example.ruireutov.organiser.ToDoList;

import android.content.Context;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseControl;

import java.util.HashMap;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public class ToDoListControl implements IToDoListControl {
    private Context context;
    private IToDoListUiControl uiControl;
    private DatabaseControl dbControl;


    ToDoListControl(Context context, IToDoListUiControl uiControl){
        this.context = context;
        this.uiControl = uiControl;
        this.dbControl = DatabaseControl.getInstance(this.context);
        dbControl.open();
        this.uiControl.updateList(this.dbControl.getToDoList());
    }

    @Override
    public void addTask(HashMap<String, String> task) {

    }

    @Override
    public void getDetails() {

    }

    @Override
    public void onDestroy() {
        this.dbControl.close();
    }
}

package com.example.ruireutov.organiser.ToDoList;

import java.util.HashMap;

/**
 * Created by ruireutov on 29-Nov-17.
 */

public interface IToDoListControl {
    void newTask();
    void showDetails(int position);
    void onDestroy();
}

package com.example.ruireutov.organiser.task.taskList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;

/**
 * Created by ruireutov on 29-Nov-17.
 */

public interface ITaskListUiControl {
    SharedPreferences getSharedPreferences();
    void onNewListData(Cursor cursor);
    void onListItemRemoval(Cursor cursor, int position);
}

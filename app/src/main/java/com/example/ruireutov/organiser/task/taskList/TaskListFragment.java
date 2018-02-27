package com.example.ruireutov.organiser.task.taskList;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.task.ListCursorAdapter;
import com.example.ruireutov.organiser.task.main.TaskActivity;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.TaskListFilter;

public class TaskListFragment extends Fragment implements ITaskListUiControl, ITaskListActivityControl{

    private ListView listView;
    private ListCursorAdapter cursorAdapter;
    private ITaskListControl listControl;
    private Button newTaskButton;


    public TaskListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        this.listView = view.findViewById(R.id.toDoListView_list);
        this.listView.setOnItemClickListener(new ListViewEventListener());

        this.newTaskButton = view.findViewById(R.id.new_task_button);
        this.newTaskButton.setOnClickListener(new ElementClickListener());

        TaskActivity activity = (TaskActivity) getActivity();
        this.listControl = new TaskListControl(activity, this, activity);

        return view;
    }

    @Override
    public void updateList(Cursor cursor) {
        if (this.cursorAdapter == null) {
            this.cursorAdapter = new ListCursorAdapter(getActivity(), cursor, 0);
            this.listView.setAdapter(this.cursorAdapter);
        } else {
            this.cursorAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateTaskListData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.listControl.onDestroy();
    }

    @Override
    public void updateTaskListData() {
        this.listControl.getTaskList(getFilters());
    }

    private TaskListFilter getFilters() {

        SharedPreferences settings = this.getActivity().getSharedPreferences(TaskDefines.PREFS_NAME, 0);
        boolean b1 = settings.getBoolean(TaskDefines.SHOW_OVERDUE, false);
        boolean b2 = settings.getBoolean(TaskDefines.SHOW_COMPLETED, false);
        TaskListFilter filter = new TaskListFilter(
                settings.getBoolean(TaskDefines.SHOW_OVERDUE, false),
                settings.getBoolean(TaskDefines.SHOW_COMPLETED, false),
                "",
                "",
                null,
                null
        );

        return filter;
    }

    private void listItemClick(int id) {
        this.listControl.showDetails(id);
    }

    private void createNewTask() {
        this.listControl.newTask();
    }

    private class ListViewEventListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listItemClick(position);
        }
    }

    private class ElementClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.new_task_button:
                    createNewTask();
                    break;
            }
        }
    }
}

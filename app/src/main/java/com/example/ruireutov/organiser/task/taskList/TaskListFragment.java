package com.example.ruireutov.organiser.task.taskList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.RecyclerViewItemTouchListener;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.TaskDetailsData;
import com.example.ruireutov.organiser.task.main.TaskActivity;

public class TaskListFragment extends Fragment implements ITaskListUiControl, ITaskListActivityControl{

    private RecyclerView taskListView;
    private TaskListAdapter taskListViewAdapter;
    private ITaskListControl listControl;


    public TaskListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        this.taskListView = view.findViewById(R.id.to_do_list);

        this.taskListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(this.taskListView.getContext(), RecyclerView.VERTICAL);
        this.taskListView.addItemDecoration(divider);
        this.taskListView.addOnItemTouchListener(new RecyclerViewItemTouchListener(this.getContext(), new RecyclerViewItemTouchListener.OnTouchActionListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int pos = ((TaskListAdapter.ViewHolder) viewHolder).onClick();
                TaskDetailsData data = taskListViewAdapter.getTaskData(pos);
                listControl.showDetails(data);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                int pos = ((TaskListAdapter.ViewHolder) viewHolder).onLongClick();
                TaskDetailsData data = taskListViewAdapter.getTaskData(pos);
                listControl.closeTask(data, pos);
            }

            @Override
            public void onItemSwipe(RecyclerView.ViewHolder viewHolder, float dx) {
                ((TaskListAdapter.ViewHolder) viewHolder).onSwipe(dx);
            }

            @Override
            public void onItemRelease(RecyclerView.ViewHolder viewHolder) {
                int pos = ((TaskListAdapter.ViewHolder) viewHolder).onRelease();
                if(pos != RecyclerView.NO_POSITION) {
                    TaskDetailsData data = taskListViewAdapter.getTaskData(pos);
                    listControl.removeTask(data, pos);
                }
            }
        }));
        this.taskListViewAdapter = new TaskListAdapter(new MatrixCursor(new String[]{}, 0), this.getContext());
        this.taskListView.setAdapter(this.taskListViewAdapter);

        FloatingActionButton newTaskButton = view.findViewById(R.id.new_task_button);
        newTaskButton.setOnClickListener(new ElementClickListener());

        TaskActivity activity = (TaskActivity) getActivity();
        this.listControl = new TaskListControl(activity, this, activity);

        return view;
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
    public void onNewListData(Cursor cursor) {
        this.taskListViewAdapter.setListData(cursor);
        this.taskListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemRemoval(Cursor cursor, int position) {
        this.taskListViewAdapter.setListData(cursor);
        this.taskListViewAdapter.notifyItemRemoved(position);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return this.getActivity().getSharedPreferences(TaskDefines.PREFS_NAME, 0);
    }

    @Override
    public void updateTaskListData() {
        this.listControl.getTaskList();
    }

    private class ElementClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.new_task_button:
                    listControl.newTask();
                    break;
            }
        }
    }
}

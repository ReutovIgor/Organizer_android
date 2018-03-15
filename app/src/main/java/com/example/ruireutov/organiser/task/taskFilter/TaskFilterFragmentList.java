package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.main.TaskActivity;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Set;

public class TaskFilterFragmentList extends Fragment implements  ITaskFilterUIControl, ITaskFilterListNotification {
    ITaskFilterControl filterControl;
    CheckBox showOverdue;
    CheckBox showCompleted;
    TextView endBy;
    TaskFilterList priorityFilter;
    TaskFilterList categoryFilter;
    boolean requiresUpdate;

    public TaskFilterFragmentList() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_filter, container, false);

        this.showOverdue = view.findViewById(R.id.show_overdue_filter);
        this.showCompleted = view.findViewById(R.id.show_completed_filter);
        this.endBy = view.findViewById(R.id.end_by_filter);
        this.priorityFilter = new TaskFilterList(
                getContext(),
                (FlexboxLayout) view.findViewById(R.id.priority_filters_list),
                getActivity().getLayoutInflater(),
                this,
                TaskDefines.SELECTED_PRIORITIES);

        this.categoryFilter = new TaskFilterList(
                getContext(),
                (FlexboxLayout) view.findViewById(R.id.category_filters_list),
                getActivity().getLayoutInflater(),
                this,
                TaskDefines.SELECTED_CATEGORIES);

        TaskActivity activity = (TaskActivity) getActivity();
        this.filterControl = new TaskFilterControl(activity, this, activity);

        return view;
    }

//    public void onFiltersUpdate() {
//        if(this.requiresUpdate) {
//            this.requiresUpdate = false;
//            this.filterControl.getTaskFilters();
//        }
//    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return this.getActivity().getSharedPreferences(TaskDefines.PREFS_NAME, 0);
    }

    @Override
    public Set<String> getSelectedPriorities() {
        return this.priorityFilter.getSelectedItems();
    }

    @Override
    public Set<String> getSelectedCategories() {
        return this.categoryFilter.getSelectedItems();
    }

    @Override
    public void updateShowOverdue(boolean state) {
        this.showOverdue.setChecked(state);
    }

    @Override
    public void updateShowCompleted(boolean state) {
        this.showCompleted.setChecked(state);
    }

    @Override
    public  void updatePriorityFilters(Cursor c) {
        this.priorityFilter.updateList(c);
    }

    @Override
    public  void updateCategoryFilters(Cursor c) {
        this.categoryFilter.updateList(c);
    }

    @Override
    public void onGroupItemSelect(String key, String name) {
        switch(key) {
            case TaskDefines.SELECTED_CATEGORIES:
                this.filterControl.addCategory(name);
                break;
            case TaskDefines.SELECTED_PRIORITIES:
                this.filterControl.addPriority(name);
                break;
        }
    }

    @Override
    public void onGroupItemDeselect(String key, String name) {
        switch(key) {
            case TaskDefines.SELECTED_CATEGORIES:
                this.filterControl.removeCategory(name);
                break;
            case TaskDefines.SELECTED_PRIORITIES:
                this.filterControl.removePriority(name);
                break;
        }
    }
}

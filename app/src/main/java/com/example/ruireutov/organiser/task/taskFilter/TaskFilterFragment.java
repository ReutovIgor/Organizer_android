package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.TaskListFilter;
import com.google.android.flexbox.FlexboxLayout;

public class TaskFilterFragment extends Fragment {
    TaskFilterList priorityFilter;
    TaskFilterList categoryFilter;
    public TaskFilterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_filter, container, false);

        this.priorityFilter = new TaskFilterList(
                getContext(),
                (FlexboxLayout) view.findViewById(R.id.priority_filters_list),
                getActivity().getLayoutInflater());

        this.categoryFilter = new TaskFilterList(
                getContext(),
                (FlexboxLayout) view.findViewById(R.id.category_filters_list),
                getActivity().getLayoutInflater());

        DatabaseControl dbControl = DatabaseControl.getInstance(getActivity());
        dbControl.open();
        this.priorityFilter.updateList(dbControl.getPrioritiesFilter(this.getFilters()));
        this.categoryFilter.updateList(dbControl.getCategoriesFilter(this.getFilters()));

        return view;
    }

    public void updatePriorityFilters(Cursor cursor) {

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

}

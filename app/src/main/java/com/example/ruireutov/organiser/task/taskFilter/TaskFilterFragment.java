package com.example.ruireutov.organiser.task.taskFilter;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.main.TaskActivity;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Set;

public class TaskFilterFragment extends Fragment implements  ITaskFilterUIControl {
    ITaskFilterControl filterControl;
    TaskFilterList priorityFilter;
    TaskFilterList categoryFilter;
    boolean requiresUpdate;

    public TaskFilterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_filter, container, false);


        Button b1 = view.findViewById(R.id.close_filters_button);
        Button b2 = view.findViewById(R.id.apply_filters_button);
        Button b3 = view.findViewById(R.id.reset_filters_button);

        b1.setOnClickListener(new ButtonClickListener());
        b2.setOnClickListener(new ButtonClickListener());
        b3.setOnClickListener(new ButtonClickListener());

        this.requiresUpdate = true;
        this.priorityFilter = new TaskFilterList(
                getContext(),
                (FlexboxLayout) view.findViewById(R.id.priority_filters_list),
                getActivity().getLayoutInflater());

        this.categoryFilter = new TaskFilterList(
                getContext(),
                (FlexboxLayout) view.findViewById(R.id.category_filters_list),
                getActivity().getLayoutInflater());

        TaskActivity activity = (TaskActivity) getActivity();
        this.filterControl = new TaskFilterControl(activity, this, activity);
        this.updateFilters();

        return view;
    }

    public void updateFilters() {
        if(this.requiresUpdate) {
            this.requiresUpdate = false;
            SharedPreferences settings = this.getSharedPreferences();
            this.filterControl.getPriorityFilters(settings);
            this.filterControl.getCategoryFilters(settings);
        }
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return this.getActivity().getSharedPreferences(TaskDefines.PREFS_NAME, 0);
    }

    @Override
    public Set<String> getSelectedPriorities() {
        return null;
    }

    @Override
    public Set<String> getSelectedCategories() {
        return null;
    }

    @Override
    public  void updatePriorityFilters(Cursor c) {
        this.priorityFilter.updateList(c);
    }

    @Override
    public  void updateCategoryFilters(Cursor c) {
        this.categoryFilter.updateList(c);
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_filters_button:
                    //close filters
                break;
                case R.id.apply_filters_button:
                    //apply new filters
                break;
                case R.id.reset_filters_button:
                    //remove all used filters
                break;
            }
        }
    }
}

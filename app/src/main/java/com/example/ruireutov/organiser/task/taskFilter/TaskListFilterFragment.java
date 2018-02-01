package com.example.ruireutov.organiser.task.taskFilter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruireutov.organiser.R;

public class TaskListFilterFragment extends Fragment {


    public TaskListFilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_filter, container, false);
        //Toolbar myToolbar = view.findViewById(R.id.toDoListView_toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);

        return view;
    }

}

package com.example.ruireutov.organiser;

import android.content.Context;
import android.util.Log;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 27-Oct-17.
 */

public class MainViewControl implements MainViewNotification{
    private BaseExpandableListAdapter expListAdapter;
    private ExpandableListView expListView;
    private List<String> groupHeaders;
    private HashMap<String, List<String>> groupItems;
    private List<String> notReadyComponents;

    //constants
    private static final String MAIN_VIEW_TAG = "MainViewControl";


    MainViewControl(Context context, ExpandableListView view) {
        this.expListView = view;
        this.groupHeaders = new ArrayList<>();
        this.groupItems = new HashMap<>();
        this.notReadyComponents = new ArrayList<>();

        //read config in the future
        this.notReadyComponents.add("toDoList");

        this.expListAdapter = new ExpListAdapter(context, this.groupHeaders, this.groupItems);
        this.expListView.setAdapter(this.expListAdapter);
    }

    private void appReady() {
        if(this.notReadyComponents.isEmpty()) {
            this.expListAdapter.notifyDataSetChanged();
        } else {
            Log.v(MAIN_VIEW_TAG, "Not all components are ready");
        }
    }

    @Override
    public void onComponentReady(String component) {
        this.notReadyComponents.remove(component);
        this.appReady();
    }

    @Override
    public void onDataChange(String component, List<String> data) {
        this.groupItems.remove(component);
        this.groupItems.put(component, data);
        this.appReady();
    }

    @Override
    public void onDataChange() {

    }
}

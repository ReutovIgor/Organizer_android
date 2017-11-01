package com.example.ruireutov.organiser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public class MainViewUIControl {
    private Context context;
    private ExpListAdapter expListAdapter;
    private ExpandableListView expListView;
    private List<String> groupHeaders;
    private HashMap<String, List<String>> groupItems;

    //    MainViewControl(Context context, ExpandableListView view) {

    MainViewUIControl(Context context, ExpandableListView view) {
        this.context = context;

        //get view
        //LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.activity_main, null);
        //find exp list
        //this.expListView = view.findViewById(R.id.mainListView);
        this.expListView = view;
        this.groupHeaders = new ArrayList<>();
        this.groupItems = new HashMap<>();
        //init exp list adapter
        //this.expListAdapter = new ExpListAdapter(context, new ArrayList<String>(), new HashMap<String, List<String>>());
        this.expListAdapter = new ExpListAdapter(context, this.groupHeaders, this.groupItems);
        //add adapter to exp list
        this.expListView.setAdapter(this.expListAdapter);
    }

    public void setData(List<String> groupHeaders, HashMap<String, List<String>> groupItems) {
        for (String header: groupHeaders) {
            this.groupHeaders.remove(header);
            this.groupHeaders.add(header);
            this.groupItems.remove(header);
            this.groupItems.put(header, groupItems.get(header));
        }
        this.expListAdapter.notifyDataSetChanged();
        //this.expListAdapter.setData(groupHeaders, groupItems);
    }

}

package com.example.ruireutov.organiser;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 27-Oct-17.
 */

public class ExpListControl {
    private BaseExpandableListAdapter expListAdapter;
    private ExpandableListView expListView;
    private List<String> groupHeaders;
    private HashMap<String, List<String>> groupItems;


    ExpListControl(Context context, ExpandableListView view) {
        this.expListView = view;
        this.groupHeaders = new ArrayList<>();
        this.groupItems = new HashMap<>();

        this.getData();

        this.expListAdapter = new ExpListAdapter(context, this.groupHeaders, this.groupItems);
        this.expListView.setAdapter(this.expListAdapter);
    }

    private void getData() {
        this.groupHeaders.add("group1");

        List<String> strs = new ArrayList<>();
        strs.add("line1");
        strs.add("line2");
        strs.add("line3");
        strs.add("line4");
        strs.add("line5");

        this.groupItems.put("group1", strs);
    }
}

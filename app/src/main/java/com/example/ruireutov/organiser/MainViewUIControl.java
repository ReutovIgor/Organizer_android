package com.example.ruireutov.organiser;

import android.content.Context;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public class MainViewUIControl {
    private ExpListAdapter expListAdapter;

    MainViewUIControl(Context context, ExpandableListView view) {
        this.expListAdapter = new ExpListAdapter(context, new ArrayList<String>(), new HashMap<String, List<String>>());
        view.setAdapter(this.expListAdapter);
    }

    public void setData(List<String> groupHeaders, HashMap<String, List<String>> groupItems) {
        this.expListAdapter.setData(groupHeaders, groupItems);
    }

}

package com.example.ruireutov.organiser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewUIControl mainViewUIControl;
    private MainViewControl mainViewControl;
    private ExpandableListView expListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.expListView = (ExpandableListView) findViewById(R.id.mainListView);
        this.mainViewUIControl = new MainViewUIControl(this, this.expListView);
        this.mainViewControl = new MainViewControl(this, this.mainViewUIControl);

        List<String> groupHeaders = new ArrayList<>();
        groupHeaders.add("group1");
        List<String> strs = new ArrayList<>();
        strs.add("line1");
        strs.add("line2");
        strs.add("line3");
        strs.add("line4");
        strs.add("line5");

        HashMap<String, List<String>> groupItems = new HashMap<>();
        groupItems.put("group1", strs);
        this.mainViewControl.onDataChange("group1", strs);
//
//        this.listAdapter = new ExpListAdapter(this, this.groupHeaders, this.groupItems);
//        this.listView.setAdapter(this.listAdapter);
    }
}

package com.example.ruireutov.organiser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpListControl mainViewControl;
    private ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.expListView = (ExpandableListView) findViewById(R.id.mainListView);
        this.mainViewControl = new ExpListControl(this, expListView);

//

//        this.groupHeaders = new ArrayList<>();
////        this.groupHeaders.add("group1");
////        List<String> strs = new ArrayList<>();
////        strs.add("line1");
////        strs.add("line2");
////        strs.add("line3");
////        strs.add("line4");
////        strs.add("line5");
//
//        this.groupItems = new HashMap<>();
//        //this.groupItems.put("group1", strs);
//
//        this.listAdapter = new ExpListAdapter(this, this.groupHeaders, this.groupItems);
//        this.listView.setAdapter(this.listAdapter);
    }
}

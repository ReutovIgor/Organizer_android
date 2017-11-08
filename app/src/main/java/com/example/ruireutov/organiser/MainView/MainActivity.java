package com.example.ruireutov.organiser.MainView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewUIControl mainViewUIControl;
    private MainViewControl mainViewControl;
    private ExpandableListView expListView;
    private SideMenuBar sideMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.mainView_drawer);
        ListView drawerList = findViewById(R.id.mainView_navList);
        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "MainView");

        this.expListView = findViewById(R.id.mainListView);
        this.mainViewUIControl = new MainViewUIControl(this, this.expListView);
        this.mainViewControl = new MainViewControl(this.mainViewUIControl);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sideMenuBar.hideSidebar();
    }
}
